package com.example.stackoverflowbrowser.mvi.search

import com.example.stackoverflowbrowser.data.source.base.IssuesDataSource
import com.example.stackoverflowbrowser.test_util.ComparableException
import com.example.stackoverflowbrowser.test_util.factory.createIssue
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class IssuesSearchViewModelTest {

    val dataSource = mock<IssuesDataSource>()
    val viewModel = IssuesSearchViewModel(IssuesSearchInteractor(dataSource))

    @Before
    fun setUp() {
        mockSchedulers()
    }

    @Test
    fun `Query intent emits expected states when data source emits issues`() {
        whenever(dataSource.getPage("testQuery", 1, 20)).thenReturn(
            Observable.just(listOf(createIssue(0), createIssue(1), createIssue(2)))
        )
        val statesObserver = viewModel.states.test()
        viewModel.processIntents(Observable.just(IssuesSearchIntent.Query("testQuery")))
        statesObserver.assertValues(
            IssuesSearchViewState.initial,
            IssuesSearchViewState.initial.copy(isLoadingFirstPage = true),
            IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0), createIssue(1), createIssue(2)))
        )
    }

    @Test
    fun `Query intent emits expected states when data source emits error`() {
        whenever(dataSource.getPage("testQuery", 1, 20)).thenReturn(Observable.error(ComparableException(123)))
        val statesObserver = viewModel.states.test()
        viewModel.processIntents(Observable.just(IssuesSearchIntent.Query("testQuery")))
        statesObserver.assertValues(
            IssuesSearchViewState.initial,
            IssuesSearchViewState.initial.copy(isLoadingFirstPage = true),
            IssuesSearchViewState.initial.copy(error = ComparableException(123))
        )
    }

    private fun mockSchedulers() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
    }
}