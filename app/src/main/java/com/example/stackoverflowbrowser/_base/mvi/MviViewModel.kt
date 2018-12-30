package com.example.stackoverflowbrowser._base.mvi

import io.reactivex.Observable

interface MviViewModel<I : MviIntent, S : MviViewState> {

    fun processIntents(intents: Observable<I>)

    val states: Observable<S>
}
