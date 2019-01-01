package com.example.stackoverflowbrowser.mvi.search

import io.reactivex.functions.BiFunction

class IssuesSearchViewStateReducer : BiFunction<IssuesSearchViewState, IssuesSearchTaskResult, IssuesSearchViewState> {

    override fun apply(previousState: IssuesSearchViewState, taskResult: IssuesSearchTaskResult) =
        when (taskResult) {
            is IssuesSearchTaskResult.SuccessFirstPage -> previousState.copy(
                isLoadingFirstPage = false,
                isLoadingNextPage = false,
                searchResults = taskResult.items,
                error = null
            )
            is IssuesSearchTaskResult.SuccessNextPage -> previousState.copy(
                isLoadingFirstPage = false,
                isLoadingNextPage = false,
                searchResults = previousState.searchResults?.toMutableList()?.apply { addAll(taskResult.items) },
                error = null
            )
            is IssuesSearchTaskResult.Error -> previousState.copy(
                isLoadingFirstPage = false,
                isLoadingNextPage = false,
                error = taskResult.error
            )
            is IssuesSearchTaskResult.LoadingFirstPage -> previousState.copy(
                isLoadingFirstPage = true,
                isLoadingNextPage = false,
                error = null
            )
            is IssuesSearchTaskResult.LoadingNextPage -> previousState.copy(
                isLoadingFirstPage = false,
                isLoadingNextPage = true,
                error = null
            )
            else -> throw IllegalArgumentException("Unhandled task result: $taskResult")
        }
}