package com.example.stackoverflowbrowser.mvi.search

import com.example.stackoverflowbrowser._base.mvi.MviInteractor
import io.reactivex.ObservableTransformer

class IssuesSearchInteractor: MviInteractor<IssuesSearchAction, IssuesSearchTaskResult> {

    override val actionProcessor: ObservableTransformer<IssuesSearchAction, IssuesSearchTaskResult>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}