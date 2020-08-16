package com.app.holderprefetcher.prefetcher

import android.app.Activity
import android.util.SparseIntArray
import android.widget.FrameLayout
import androidx.recyclerview.widget.ALLOW_THREAD_GAP_WORK
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.viewType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
internal class ViewHolderCreator(
    activity: Activity,
    private val coroutineScope: CoroutineScope,
    private val holderConsumer: (holder: RecyclerView.ViewHolder, creationTimeNs: Long) -> Unit
) {

    private val fakeParent by lazy { FrameLayout(activity) }
    private val createdOutsideChannel = Channel<ViewType>(1)
    private val enqueueChannel = Channel<ViewHolderWrapper>(1)
    private val createItemChannel = Channel<ViewHolderWrapper>(1)
    private val itemsCreated = SparseIntArray()
    private val itemsQueued = SparseIntArray()

    fun prepare() {
        coroutineScope.launch {
            createdOutsideChannel.consumeEach {
                createdOutside(it.viewType)
            }
        }

        coroutineScope.launch {
            enqueueChannel.consumeEach  {
                enqueueBatch(it.holderCreator, it.viewType, it.itemsCount)
            }
        }

        coroutineScope.launch {
            createItemChannel.consumeEach {
                createItem(it.holderCreator, it.viewType)
            }
        }
    }

    fun clear() {
        clearAndCancel()
        coroutineScope.cancel()
    }

    fun setPrefetchBound(holderCreator: HolderCreator, viewType: Int, count: Int) {
        coroutineScope.launch {
            enqueueChannel.send(ViewHolderWrapper(holderCreator, viewType, count))
        }
    }

    fun itemCreatedOutside(viewType: Int) {
        coroutineScope.launch {
            createdOutsideChannel.send(ViewType(viewType))
        }
    }

    private fun createdOutside(viewType: Int) {
        itemsCreated.put(viewType, itemsCreated[viewType] + 1)
    }

    private fun enqueueBatch(holderCreator: HolderCreator, viewType: Int, count: Int) {
        if (itemsQueued[viewType] >= count) return
        itemsQueued.put(viewType, count)

        val created = itemsCreated[viewType]
        if (created >= count) return

        enqueueItemCreation(holderCreator, viewType)
    }

    private suspend fun createItem(holderCreator: HolderCreator, viewType: Int) {
        val created = itemsCreated[viewType] + 1
        val queued = itemsQueued[viewType]
        if (created > queued) return

        val holder: RecyclerView.ViewHolder
        val start: Long
        val end: Long

        try {
            start = nanoTimeIfNeed()
            holder = holderCreator(fakeParent, viewType)
            end = nanoTimeIfNeed()
        } catch (e: Exception) {
            return
        }

        holder.viewType = viewType
        itemsCreated.put(viewType, created)

        withContext(Dispatchers.Main) {
            holderConsumer(holder, end - start)
        }
        if (created < queued) enqueueItemCreation(holderCreator, viewType)
    }

    private fun enqueueItemCreation(holderCreator: HolderCreator, viewType: Int) {
        coroutineScope.launch {
            createItemChannel.send(ViewHolderWrapper(holderCreator, viewType))
        }
    }

    private fun clearAndCancel() {
        createItemChannel.cancel()
        enqueueChannel.cancel()
        createdOutsideChannel.cancel()
        itemsQueued.clear()
        itemsCreated.clear()
    }

    private fun nanoTimeIfNeed() = if (ALLOW_THREAD_GAP_WORK) System.nanoTime() else 0L

    private class ViewHolderWrapper(
        val holderCreator: HolderCreator,
        val viewType: Int,
        val itemsCount: Int = 0
    )
}

private inline class ViewType(val viewType: Int)
