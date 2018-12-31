package com.example.stackoverflowbrowser.di

import com.example.stackoverflowbrowser.data.source.base.IssuesDataSource
import com.example.stackoverflowbrowser.data.source.base.remote.RemoteIssuesDataSource
import com.example.stackoverflowbrowser.mvi.search.IssuesSearchInteractor
import com.example.stackoverflowbrowser.mvi.search.IssuesSearchViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


val issuesSearchModule = module {

    factory<IssuesDataSource> { RemoteIssuesDataSource() }

    single { IssuesSearchInteractor(get()) }

    viewModel { IssuesSearchViewModel(get()) }
}