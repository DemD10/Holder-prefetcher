@file:Suppress("PackageDirectoryMismatch")

package androidx.recyclerview.widget

import com.app.holderprefetcher.prefetcher.PrefetchRecycledViewPool
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal fun RecyclerView.RecycledViewPool.attachToPreventViewPoolFromClearing() {
    this.attach()
}

@ExperimentalCoroutinesApi
internal fun PrefetchRecycledViewPool.factorInCreateTime(viewType: Int, createTimeNs: Long) = (this as RecyclerView.RecycledViewPool).factorInCreateTime(viewType, createTimeNs)

internal val ALLOW_THREAD_GAP_WORK = RecyclerView.ALLOW_THREAD_GAP_WORK

internal var RecyclerView.ViewHolder.viewType: Int
    get() = itemViewType
    set(value) {
        mItemViewType = value
    }



