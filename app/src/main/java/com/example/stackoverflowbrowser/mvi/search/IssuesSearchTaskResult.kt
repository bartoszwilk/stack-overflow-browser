package com.example.stackoverflowbrowser.mvi.search

import com.example.stackoverflowbrowser._base.mvi.MviResult
import com.example.stackoverflowbrowser.data.entity.Issue

sealed class IssuesSearchTaskResult : MviResult {

    object LoadingFirstPage : IssuesSearchTaskResult()
    object LoadingNextPage : IssuesSearchTaskResult()
    data class SuccessFirstPage(val items: List<Issue>) : IssuesSearchTaskResult()
    data class SuccessNextMore(val items: List<Issue>) : IssuesSearchTaskResult()
    data class Error(val error: Throwable) : IssuesSearchTaskResult()
}