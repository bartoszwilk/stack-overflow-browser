package com.example.stackoverflowbrowser.di

import com.example.stackoverflowbrowser.data.source.base.IssuesDataSource
import com.example.stackoverflowbrowser.data.source.base.remote.RemoteIssuesDataSource
import com.example.stackoverflowbrowser.mvi.search.IssuesSearchInteractor
import com.example.stackoverflowbrowser.mvi.search.IssuesSearchViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import pl.wp.videostar.viper.web.WebStarter

val issuesSearchModule = module {

    factory<IssuesDataSource> { RemoteIssuesDataSource() }

    single { IssuesSearchInteractor(get()) }

    single { WebStarter() }

    viewModel { IssuesSearchViewModel(get()) }
}