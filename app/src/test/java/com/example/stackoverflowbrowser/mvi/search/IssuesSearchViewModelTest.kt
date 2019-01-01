package com.example.stackoverflowbrowser.mvi.search

import com.example.stackoverflowbrowser.data.entity.Issue
import com.example.stackoverflowbrowser.data.source.base.IssuesDataSource
import com.example.stackoverflowbrowser.test_util.ComparableException
import com.example.stackoverflowbrowser.test_util.factory.createIssue
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class IssuesSearchViewModelTest {

    val dataSource = mock<IssuesDataSource>()
    val viewModel = IssuesSearchViewModel(IssuesSearchInteractor(dataSource))
    val intentEvents = PublishSubject.create<IssuesSearchIntent>()

    @Before
    fun setUp() {
        mockSchedulers()
        viewModel.processIntents(intentEvents)
    }

    @Test
    fun `Query intent emits expected states when data source emits issues`() {
        mockIssuesResults(
            query = "testQuery",
            page = 1,
            issues = listOf(createIssue(0), createIssue(1), createIssue(2))
        )
        val statesObserver = viewModel.states.test()
        intentEvents.onNext(IssuesSearchIntent.Query("testQuery"))
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
        intentEvents.onNext(IssuesSearchIntent.Query("testQuery"))
        statesObserver.assertValues(
            IssuesSearchViewState.initial,
            IssuesSearchViewState.initial.copy(isLoadingFirstPage = true),
            IssuesSearchViewState.initial.copy(error = ComparableException(123))
        )
    }

    @Test
    fun `Query intent does not affect on states when query text has length less than 3`() {
        mockIssuesResults(query = "te", page = 1, issues = listOf(createIssue(0), createIssue(1), createIssue(2)))
        val statesObserver = viewModel.states.test()
        intentEvents.onNext(IssuesSearchIntent.Query("te"))
        statesObserver.assertValues(IssuesSearchViewState.initial)
    }

    @Test
    fun `LoadNextPage intent emits expected states when data source emits issues`() {
        mockIssuesResults(query = "testQuery", page = 1, issues = listOf(createIssue(0)))
        mockIssuesResults(
            query = "testQuery",
            page = 2,
            issues = listOf(createIssue(1), createIssue(2), createIssue(3))
        )
        val statesObserver = viewModel.states.test()
        intentEvents.onNext(IssuesSearchIntent.Query("testQuery"))
        intentEvents.onNext(IssuesSearchIntent.LoadNextPage("testQuery"))
        statesObserver.assertValues(
            IssuesSearchViewState.initial,
            IssuesSearchViewState.initial.copy(isLoadingFirstPage = true),
            IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0))),
            IssuesSearchViewState.initial.copy(isLoadingNextPage = true, searchResults = listOf(createIssue(0))),
            IssuesSearchViewState.initial.copy(
                searchResults = listOf(
                    createIssue(0),
                    createIssue(1),
                    createIssue(2),
                    createIssue(3)
                )
            )
        )
    }

    @Test
    fun `LoadNextPage intent emits expected states when intent was emitted multiple times`() {
        mockIssuesResults(query = "testQuery", page = 1, issues = listOf(createIssue(0)))
        mockIssuesResults(query = "testQuery", page = 2, issues = listOf(createIssue(1), createIssue(2)))
        mockIssuesResults(query = "testQuery", page = 3, issues = listOf(createIssue(3), createIssue(4)))
        mockIssuesResults(query = "testQuery", page = 4, issues = listOf(createIssue(5), createIssue(6)))
        val statesObserver = viewModel.states.test()
        intentEvents.onNext(IssuesSearchIntent.Query("testQuery"))
        intentEvents.onNext(IssuesSearchIntent.LoadNextPage("testQuery"))
        statesObserver.assertValues(
            IssuesSearchViewState.initial,
            IssuesSearchViewState.initial.copy(isLoadingFirstPage = true),
            IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0))),
            IssuesSearchViewState.initial.copy(isLoadingNextPage = true, searchResults = listOf(createIssue(0))),
            IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0), createIssue(1), createIssue(2)))
        )
        intentEvents.onNext(IssuesSearchIntent.LoadNextPage("testQuery"))
        statesObserver.assertValues(
            IssuesSearchViewState.initial,
            IssuesSearchViewState.initial.copy(isLoadingFirstPage = true),
            IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0))),
            IssuesSearchViewState.initial.copy(isLoadingNextPage = true, searchResults = listOf(createIssue(0))),
            IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0), createIssue(1), createIssue(2))),
            IssuesSearchViewState.initial.copy(
                isLoadingNextPage = true,
                searchResults = listOf(createIssue(0), createIssue(1), createIssue(2))
            ),
            IssuesSearchViewState.initial.copy(
                searchResults = listOf(
                    createIssue(0), createIssue(1), createIssue(2), createIssue(3), createIssue(4)
                )
            )
        )
        intentEvents.onNext(IssuesSearchIntent.LoadNextPage("testQuery"))
        statesObserver.assertValues(
            IssuesSearchViewState.initial,
            IssuesSearchViewState.initial.copy(isLoadingFirstPage = true),
            IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0))),
            IssuesSearchViewState.initial.copy(isLoadingNextPage = true, searchResults = listOf(createIssue(0))),
            IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0), createIssue(1), createIssue(2))),
            IssuesSearchViewState.initial.copy(
                isLoadingNextPage = true,
                searchResults = listOf(createIssue(0), createIssue(1), createIssue(2))
            ),
            IssuesSearchViewState.initial.copy(
                searchResults = listOf(
                    createIssue(0),
                    createIssue(1),
                    createIssue(2),
                    createIssue(3),
                    createIssue(4)
                )
            ),
            IssuesSearchViewState.initial.copy(
                isLoadingNextPage = true,
                searchResults = listOf(
                    createIssue(0),
                    createIssue(1),
                    createIssue(2),
                    createIssue(3),
                    createIssue(4)
                )
            ),
            IssuesSearchViewState.initial.copy(
                searchResults = listOf(
                    createIssue(0),
                    createIssue(1),
                    createIssue(2),
                    createIssue(3),
                    createIssue(4),
                    createIssue(5),
                    createIssue(6)
                )
            )
        )
    }

    @Test
    fun `Refresh intent emits expected states when more than one page was loaded`() {
        mockIssuesResults(query = "testQuery", page = 1, issues = listOf(createIssue(0)))
        mockIssuesResults(query = "testQuery", page = 2, issues = listOf(createIssue(1)))
        val statesObserver = viewModel.states.test()
        intentEvents.onNext(IssuesSearchIntent.Query("testQuery"))
        intentEvents.onNext(IssuesSearchIntent.LoadNextPage("testQuery"))
        intentEvents.onNext(IssuesSearchIntent.Refresh("testQuery"))
        statesObserver.assertValues(
            IssuesSearchViewState.initial,
            IssuesSearchViewState.initial.copy(isLoadingFirstPage = true),
            IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0))),
            IssuesSearchViewState.initial.copy(isLoadingNextPage = true, searchResults = listOf(createIssue(0))),
            IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0), createIssue(1))),
            IssuesSearchViewState.initial.copy(
                isLoadingFirstPage = true,
                searchResults = listOf(createIssue(0), createIssue(1))
            ),
            IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0)))
        )
    }

    fun mockIssuesResults(query: String, page: Int, issues: List<Issue>) =
        whenever(dataSource.getPage(query, page, 20)).thenReturn(Observable.just(issues))

    fun mockSchedulers() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
    }
}