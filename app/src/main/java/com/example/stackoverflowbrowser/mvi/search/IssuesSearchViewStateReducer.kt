package com.example.stackoverflowbrowser.mvi.search

import io.reactivex.functions.BiFunction

class IssuesSearchViewStateReducer : BiFunction<IssuesSearchViewState, IssuesSearchTaskResult, IssuesSearchViewState> {

    override fun apply(previousState: IssuesSearchViewState, taskResult: IssuesSearchTaskResult) =
        when (taskResult) {
            is IssuesSearchTaskResult.SuccessFirstPage -> previousState.copy(
                isLoadingFirstPage = false,
                searchResults = taskResult.items
            )
            is IssuesSearchTaskResult.Error -> previousState.copy(
                isLoadingFirstPage = false,
                error = taskResult.error
            )
            is IssuesSearchTaskResult.LoadingFirstPage -> previousState.copy(isLoadingFirstPage = true)
            else -> throw IllegalArgumentException("Unhandled task result: $taskResult")
        }
}