package com.example.stackoverflowbrowser.mvi.search

import com.example.stackoverflowbrowser._base.mvi.MviInteractor
import com.example.stackoverflowbrowser.data.source.base.IssuesDataSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class IssuesSearchInteractor(issuesDataSource: IssuesDataSource) :
    MviInteractor<IssuesSearchAction, IssuesSearchTaskResult> {

    override val actionProcessor: ObservableTransformer<IssuesSearchAction, IssuesSearchTaskResult> =
        ObservableTransformer { issuesSearchActions ->
            issuesSearchActions.publish { sharedEvents ->
                sharedEvents.ofType(IssuesSearchAction.Search::class.java).compose(issuesSearchProcessor)
            }
        }

    private val issuesSearchProcessor = ObservableTransformer<IssuesSearchAction.Search, IssuesSearchTaskResult> {
        it.flatMap { action ->
            issuesDataSource
                .getPage(action.query, 1, 20)
                .map<IssuesSearchTaskResult>(IssuesSearchTaskResult::SuccessFirstPage)
                .onErrorReturn(IssuesSearchTaskResult::Error)
                .startWith(IssuesSearchTaskResult.LoadingFirstPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}