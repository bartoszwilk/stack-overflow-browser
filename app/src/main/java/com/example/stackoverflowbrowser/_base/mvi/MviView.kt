package com.example.stackoverflowbrowser._base.mvi

import io.reactivex.Observable

interface MviView<I : MviIntent, S : MviViewState> {

    val intents: Observable<I>

    fun render(state: S)
}
