package com.example.stackoverflowbrowser.di

import com.example.stackoverflowbrowser.mvi.search.IssuesSearchInteractor
import com.example.stackoverflowbrowser.mvi.search.IssuesSearchViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val issuesSearchModule = module {

    single { IssuesSearchInteractor() }

    viewModel { IssuesSearchViewModel(get()) }
}