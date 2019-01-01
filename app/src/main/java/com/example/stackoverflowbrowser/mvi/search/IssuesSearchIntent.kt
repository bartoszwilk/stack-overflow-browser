package com.example.stackoverflowbrowser.mvi.search

import com.example.stackoverflowbrowser._base.mvi.MviIntent

sealed class IssuesSearchIntent : MviIntent {

    data class Query(val query: String) : IssuesSearchIntent()
}