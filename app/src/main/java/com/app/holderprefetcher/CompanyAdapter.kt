package com.app.holderprefetcher

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.coroutines.CoroutineContext

class CompanyAdapter(private val items: List<Model>): RecyclerView.Adapter<CompanyViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return (position / 20).coerceAtMost(7)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        return ViewHolderFactory.createHolder(parent, viewType)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(items[position].someText)
    }
}