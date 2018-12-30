package com.example.stackoverflowbrowser._base.mvi

import io.reactivex.ObservableTransformer

interface MviInteractor<ActionType: MviAction, ResultType: MviResult> {

    val actionProcessor: ObservableTransformer<ActionType, ResultType>
}