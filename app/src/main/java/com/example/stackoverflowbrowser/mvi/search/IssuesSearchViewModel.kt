package com.example.stackoverflowbrowser.mvi.search

import android.arch.lifecycle.ViewModel
import com.example.stackoverflowbrowser._base.mvi.MviViewModel
import com.example.stackoverflowbrowser.util.extension.ofNotType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

class IssuesSearchViewModel(interactor: IssuesSearchInteractor) :
    MviViewModel<IssuesSearchIntent, IssuesSearchViewState>,
    ViewModel() {

    private val intentEvents = PublishSubject.create<IssuesSearchIntent>()
    private val duplicatedQueryIntentsFilter = ObservableTransformer<IssuesSearchIntent, IssuesSearchIntent> {
        it.publish { sharedIntentEvents ->
            Observable.merge(
                sharedIntentEvents.ofType(IssuesSearchIntent.Query::class.java).distinctUntilChanged(),
                sharedIntentEvents.ofNotType(IssuesSearchIntent.Query::class.java)
            )
        }
    }

    override val states: Observable<IssuesSearchViewState> =
        intentEvents
            .compose(duplicatedQueryIntentsFilter)
            .map { it.mapToAction() }
            .compose(interactor.actionProcessor)
            .cast(IssuesSearchTaskResult::class.java)
            .scan(IssuesSearchViewState.initial, IssuesSearchViewStateReducer())
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)

    override fun processIntents(intents: Observable<IssuesSearchIntent>) =
        intents.subscribe(intentEvents)

    private fun IssuesSearchIntent.mapToAction() =
        when {
            this is IssuesSearchIntent.Query -> IssuesSearchAction.Search(query)
            this is IssuesSearchIntent.Refresh -> IssuesSearchAction.Search(query)
            this is IssuesSearchIntent.LoadNextPage -> IssuesSearchAction.LoadMore(query)
            else -> throw IllegalArgumentException("No such intent: $this")
        }

}