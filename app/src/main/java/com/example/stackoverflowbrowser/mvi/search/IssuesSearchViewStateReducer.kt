package com.example.stackoverflowbrowser.mvi.search

import io.reactivex.functions.BiFunction

class IssuesSearchViewStateReducer : BiFunction<IssuesSearchViewState, IssuesSearchTaskResult, IssuesSearchViewState> {

    override fun apply(previousState: IssuesSearchViewState, taskResult: IssuesSearchTaskResult): IssuesSearchViewState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}