package com.example.stackoverflowbrowser.mvi.search

import com.example.stackoverflowbrowser.test_util.factory.createIssue
import com.nhaarman.mockitokotlin2.whenever
import com.tomasznajda.ktx.android.gone
import com.tomasznajda.ktx.android.isGone
import com.tomasznajda.ktx.android.isVisible
import com.tomasznajda.ktx.android.visible
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.test.AutoCloseKoinTest
import org.koin.test.declareMock
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
class IssuesSearchActivityTest : AutoCloseKoinTest() {

    val controller = Robolectric.buildActivity(IssuesSearchActivity::class.java)
    val activity: IssuesSearchActivity = controller.get()
    val viewModel: IssuesSearchViewModel by activity.viewModel()
    val stateChanges = PublishSubject.create<IssuesSearchViewState>()

    @Before
    fun setUp() {
        declareMock<IssuesSearchViewModel>()
        whenever(viewModel.states).thenReturn(stateChanges)
        controller.setup()
    }

    @Test
    fun `searchView query change emits expected intent`() {
        val testObserver = activity.intents.test()
        activity.searchView.setQuery("testQuery", false)
        testObserver.assertValue(IssuesSearchIntent.Query("testQuery"))
    }

    @Test
    fun `state change hides loading when error occurred`() {
        activity.loadingView.visible()
        stateChanges.onNext(IssuesSearchViewState.initial.copy(error = Exception()))
        assertTrue(activity.loadingView.isGone)
    }

    @Test
    fun `state change shows error when error occurred`() {
        stateChanges.onNext(IssuesSearchViewState.initial.copy(error = Exception("123")))
        Assert.assertEquals("123", ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun `state change shows loading view when first page is loading`() {
        activity.loadingView.gone()
        stateChanges.onNext(IssuesSearchViewState.initial.copy(isLoadingFirstPage = true))
        assertTrue(activity.loadingView.isVisible)
    }

    @Test
    fun `state change hides issue list view when first page is loading`() {
        activity.issueList.visible()
        stateChanges.onNext(IssuesSearchViewState.initial.copy(isLoadingFirstPage = true))
        assertTrue(activity.issueList.isGone)
    }

    @Test
    fun `state change hides empty results info when first page is loading`() {
        activity.emptyResultsInfo.visible()
        stateChanges.onNext(IssuesSearchViewState.initial.copy(isLoadingFirstPage = true))
        assertTrue(activity.emptyResultsInfo.isGone)
    }

    @Test
    fun `state change shows empty results info when search results are empty`() {
        activity.emptyResultsInfo.gone()
        stateChanges.onNext(IssuesSearchViewState.initial.copy(searchResults = emptyList()))
        assertTrue(activity.emptyResultsInfo.isVisible)
    }

    @Test
    fun `state change hides loading view when search results are empty`() {
        activity.loadingView.visible()
        stateChanges.onNext(IssuesSearchViewState.initial.copy(searchResults = emptyList()))
        assertTrue(activity.loadingView.isGone)
    }

    @Test
    fun `state change hides empty results info when search results are not empty`() {
        activity.emptyResultsInfo.gone()
        stateChanges.onNext(IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0))))
        assertTrue(activity.loadingView.isGone)
    }

    @Test
    fun `state change hides loading view when search results are not empty`() {
        activity.loadingView.visible()
        stateChanges.onNext(IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0))))
        assertTrue(activity.loadingView.isGone)
    }

    @Test
    fun `state change shows issue list when search results are not empty`() {
        activity.issueList.gone()
        stateChanges.onNext(IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0))))
        assertTrue(activity.issueList.isVisible)
    }

}