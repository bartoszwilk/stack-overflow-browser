package com.example.stackoverflowbrowser.mvi.search

import com.example.stackoverflowbrowser._base.mvi.MviInteractor
import com.example.stackoverflowbrowser.data.source.base.IssuesDataSource
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

const val SEARCH_RESULTS_PAGE_SIZE = 20
const val INITIAL_SEARCH_PAGE = 1
const val INITIAL_LOAD_MORE_PAGE = 2

class IssuesSearchInteractor(issuesDataSource: IssuesDataSource) :
    MviInteractor<IssuesSearchAction, IssuesSearchTaskResult> {

    override val actionProcessor: ObservableTransformer<IssuesSearchAction, IssuesSearchTaskResult> =
        ObservableTransformer { issuesSearchActions ->
            issuesSearchActions.publish { sharedEvents ->
                Observable.merge(
                    sharedEvents.ofType(IssuesSearchAction.Search::class.java).compose(issuesSearchProcessor),
                    sharedEvents.ofType(IssuesSearchAction.LoadMore::class.java).compose(loadMoreIssuesProcessor)
                )
            }
        }

    private val issuesSearchProcessor = ObservableTransformer<IssuesSearchAction.Search, IssuesSearchTaskResult> {
        it
            .filter { it.query.length >= 3 }
            .flatMap { action ->
            issuesDataSource
                .getPage(action.query, INITIAL_SEARCH_PAGE, SEARCH_RESULTS_PAGE_SIZE)
                .map<IssuesSearchTaskResult>(IssuesSearchTaskResult::SuccessFirstPage)
                .onErrorReturn(IssuesSearchTaskResult::Error)
                .startWith(IssuesSearchTaskResult.LoadingFirstPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private val loadMoreIssuesProcessor =
        ObservableTransformer<IssuesSearchAction.LoadMore, IssuesSearchTaskResult> { searchActions ->
            searchActions
                .map { it to INITIAL_LOAD_MORE_PAGE }
                .scan { previousBundle, action -> Pair(action.first, previousBundle.second + 1) }
                .flatMap { (action, page) ->
                    issuesDataSource
                        .getPage(action.query, page, SEARCH_RESULTS_PAGE_SIZE)
                        .map<IssuesSearchTaskResult>(IssuesSearchTaskResult::SuccessNextPage)
                        .onErrorReturn(IssuesSearchTaskResult::Error)
                        .startWith(IssuesSearchTaskResult.LoadingNextPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                }
        }
}