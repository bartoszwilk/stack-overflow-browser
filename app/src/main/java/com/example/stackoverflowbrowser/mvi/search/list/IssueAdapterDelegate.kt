package com.example.stackoverflowbrowser.mvi.search.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.stackoverflowbrowser.R
import com.example.stackoverflowbrowser._base.list.ListItem
import com.example.stackoverflowbrowser.data.entity.Issue
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import io.reactivex.subjects.Subject

class IssueAdapterDelegate(val issueClicks: Subject<Issue>) : AdapterDelegate<List<ListItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        IssueViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.viewholder_issue, parent, false),
            issueClicks
        )

    override fun isForViewType(items: List<ListItem>, position: Int) =
        items[position].type == ISSUE_ITEM_TYPE

    override fun onBindViewHolder(
        items: List<ListItem>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as IssueViewHolder).bind(items[position] as IssueListItem)

}