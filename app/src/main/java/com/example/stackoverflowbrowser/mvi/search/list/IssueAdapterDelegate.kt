package com.example.stackoverflowbrowser.mvi.search.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.stackoverflowbrowser.R
import com.example.stackoverflowbrowser._base.list.ListItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate

class IssueAdapterDelegate : AdapterDelegate<List<ListItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        IssueViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.viewholder_issue, null)
        )

    override fun isForViewType(items: List<ListItem>, position: Int) =
        items[position].type == ISSUE_ITEM_TYPE

    override fun onBindViewHolder(
        items: List<ListItem>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as IssueViewHolder).bind(items[position] as IssueItem)

}