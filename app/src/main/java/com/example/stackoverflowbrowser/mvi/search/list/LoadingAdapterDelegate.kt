package com.example.stackoverflowbrowser.mvi.search.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.stackoverflowbrowser.R
import com.example.stackoverflowbrowser._base.list.ListItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate

class LoadingAdapterDelegate : AdapterDelegate<List<ListItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        LoadingViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.viewholder_loading, null)
        )

    override fun isForViewType(items: List<ListItem>, position: Int) =
        items[position].type == LOADING_ITEM_TYPE

    override fun onBindViewHolder(
        items: List<ListItem>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        //Purposefully left blank. There is nothing to bind within this view holder
    }

}