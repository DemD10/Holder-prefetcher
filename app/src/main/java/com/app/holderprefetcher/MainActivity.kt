package com.app.holderprefetcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.holderprefetcher.prefetcher.PrefetchRecycledViewPool
import com.app.holderprefetcher.prefetcher.HolderPrefetcher
import kotlinx.android.synthetic.main.activity_main.companyRecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    private lateinit var viewPool: PrefetchRecycledViewPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPool = PrefetchRecycledViewPool(
            this,
            this
        ).apply {
            prepare()
        }

        with(companyRecyclerView) {
            adapter = CompanyAdapter(Model.createList(800))
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter?.notifyDataSetChanged()
            setRecycledViewPool(viewPool)
        }

        prefetchItems(viewPool)
    }

    private fun prefetchItems(holderPrefetcher: HolderPrefetcher) {
        val count = 20
        holderPrefetcher.setViewsCount(ViewHolderFactory.TYPE_GOOGLE, count) { fakeParent, viewType ->
            ViewHolderFactory.createHolder(fakeParent, viewType)
        }
        holderPrefetcher.setViewsCount(ViewHolderFactory.TYPE_FACEBOOK, count) { fakeParent, viewType ->
            ViewHolderFactory.createHolder(fakeParent, viewType)
        }
        holderPrefetcher.setViewsCount(ViewHolderFactory.TYPE_APPLE, count) { fakeParent, viewType ->
            ViewHolderFactory.createHolder(fakeParent, viewType)
        }
        holderPrefetcher.setViewsCount(ViewHolderFactory.TYPE_MICROSOFT, count) { fakeParent, viewType ->
            ViewHolderFactory.createHolder(fakeParent, viewType)
        }
        holderPrefetcher.setViewsCount(ViewHolderFactory.TYPE_AMAZON, count) { fakeParent, viewType ->
            ViewHolderFactory.createHolder(fakeParent, viewType)
        }
        holderPrefetcher.setViewsCount(ViewHolderFactory.TYPE_IBM, count) { fakeParent, viewType ->
            ViewHolderFactory.createHolder(fakeParent, viewType)
        }
        holderPrefetcher.setViewsCount(ViewHolderFactory.TYPE_INTEL, count) { fakeParent, viewType ->
            ViewHolderFactory.createHolder(fakeParent, viewType)
        }
        holderPrefetcher.setViewsCount(ViewHolderFactory.TYPE_QUALCOMM, count) { fakeParent, viewType ->
            ViewHolderFactory.createHolder(fakeParent, viewType)
        }
    }

    override fun onDestroy() {
        viewPool.clear()
        super.onDestroy()
    }
}