package com.example.stackoverflowbrowser.mvi.search

import com.example.stackoverflowbrowser._base.mvi.MviAction

sealed class IssuesSearchAction : MviAction {
    data class Search(val query: String) : IssuesSearchAction()
}