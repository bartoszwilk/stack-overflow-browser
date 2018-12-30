package com.example.stackoverflowbrowser.mvi.search

import android.arch.lifecycle.ViewModel
import com.example.stackoverflowbrowser._base.mvi.MviViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class IssuesSearchViewModel(private val interactor: IssuesSearchInteractor) :
    MviViewModel<IssuesSearchIntent, IssuesSearchViewState>,
    ViewModel() {

    private val intentEvents = PublishSubject.create<IssuesSearchIntent>()

    override fun processIntents(intents: Observable<IssuesSearchIntent>) =
        intents.subscribe(intentEvents)

    override val states: Observable<IssuesSearchViewState> =
        intentEvents
            .map { it.mapToAction() }
            .compose(interactor.actionProcessor)
            .cast(IssuesSearchTaskResult::class.java)
            .scan(IssuesSearchViewState.initial, IssuesSearchViewStateReducer())
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)

    private fun IssuesSearchIntent.mapToAction(): IssuesSearchAction = TODO("not implemented")
}