package com.example.stackoverflowbrowser.mvi.search.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.stackoverflowbrowser._base.list.ListItem
import com.example.stackoverflowbrowser.data.entity.Issue
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class IssuesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isLoading = false
    private val _issueClicks = PublishSubject.create<Issue>()
    val issueClicks: Observable<Issue> = _issueClicks
    var items: List<ListItem> = emptyList()
        set(value) {
            field = value.toMutableList().apply {
                if (isLoading) add(LoadingListItem)
            }
            notifyDataSetChanged()
        }

    private val delegatesManager: AdapterDelegatesManager<List<ListItem>> =
        AdapterDelegatesManager<List<ListItem>>().apply {
            addDelegate(ISSUE_ITEM_TYPE, IssueAdapterDelegate(_issueClicks))
            addDelegate(LOADING_ITEM_TYPE, LoadingAdapterDelegate())
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        delegatesManager.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
        delegatesManager.onBindViewHolder(items, position, viewHolder)

    override fun getItemViewType(position: Int) =
        delegatesManager.getItemViewType(items, position)

    override fun getItemCount() = items.size
}