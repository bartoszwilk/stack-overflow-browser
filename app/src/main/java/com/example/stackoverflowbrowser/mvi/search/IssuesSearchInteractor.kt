package com.example.stackoverflowbrowser.mvi.search

import com.example.stackoverflowbrowser._base.mvi.MviInteractor
import com.example.stackoverflowbrowser.data.source.base.IssuesDataSource
import io.reactivex.ObservableTransformer

class IssuesSearchInteractor(issuesDataSource: IssuesDataSource) :
    MviInteractor<IssuesSearchAction, IssuesSearchTaskResult> {

    override val actionProcessor: ObservableTransformer<IssuesSearchAction, IssuesSearchTaskResult>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}