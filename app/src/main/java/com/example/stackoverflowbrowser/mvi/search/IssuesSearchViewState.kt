package com.example.stackoverflowbrowser.mvi.search

import com.example.stackoverflowbrowser._base.mvi.MviViewState
import com.example.stackoverflowbrowser.data.entity.Issue

data class IssuesSearchViewState(
    val isLoadingFirstPage: Boolean,
    val isLoadingNextPage: Boolean,
    val searchQuery: String?,
    val searchResults: List<Issue>?,
    val error: Throwable?
) : MviViewState {

    companion object {
        val initial = IssuesSearchViewState(false, false, null, null, null)
    }
}