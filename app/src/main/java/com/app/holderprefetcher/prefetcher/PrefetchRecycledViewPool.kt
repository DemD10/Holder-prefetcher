package com.app.holderprefetcher.prefetcher

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.attachToPreventViewPoolFromClearing
import androidx.recyclerview.widget.factorInCreateTime
import androidx.recyclerview.widget.viewType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class PrefetchRecycledViewPool(activity: Activity, coroutineScope: CoroutineScope) : RecyclerView.RecycledViewPool(), HolderPrefetcher {

    private val viewHolderCreator = ViewHolderCreator(activity, coroutineScope, ::putViewFromCreator)

    override fun setViewsCount(viewType: Int, count: Int, holderCreator: (fakeParent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder) {
        require(count > 0)
        viewHolderCreator.setPrefetchBound(holderCreator, viewType, count)
    }

    fun prepare() {
        viewHolderCreator.prepare()
        attachToPreventViewPoolFromClearing()
    }

    override fun putRecycledView(scrap: RecyclerView.ViewHolder) {
        val viewType = scrap.itemViewType
        setMaxRecycledViews(viewType, 20)
        super.putRecycledView(scrap)
    }

    override fun getRecycledView(viewType: Int): RecyclerView.ViewHolder? {
        val holder = super.getRecycledView(viewType)
        if (holder == null) {
            viewHolderCreator.itemCreatedOutside(viewType)
        }
        return holder
    }

    override fun clear() {
        viewHolderCreator.clear()
        super.clear()
    }

    private fun putViewFromCreator(scrap: RecyclerView.ViewHolder, creationTimeNs: Long) {
        factorInCreateTime(scrap.viewType, creationTimeNs)
        putRecycledView(scrap)
    }
}