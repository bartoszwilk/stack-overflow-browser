package com.example.stackoverflowbrowser.mvi.search

import com.example.stackoverflowbrowser.mvi.search.list.IssueListItem
import com.example.stackoverflowbrowser.mvi.search.list.IssuesAdapter
import com.example.stackoverflowbrowser.mvi.search.list.LoadingListItem
import com.example.stackoverflowbrowser.test_util.factory.createIssue
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.tomasznajda.ktx.android.gone
import com.tomasznajda.ktx.android.isGone
import com.tomasznajda.ktx.android.isVisible
import com.tomasznajda.ktx.android.visible
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.standalone.inject
import org.koin.test.AutoCloseKoinTest
import org.koin.test.declareMock
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowToast
import org.robolectric.shadows.support.v4.Shadows
import pl.wp.videostar.viper.web.WebStarter

@RunWith(RobolectricTestRunner::class)
class IssuesSearchActivityTest : AutoCloseKoinTest() {

    val controller = Robolectric.buildActivity(IssuesSearchActivity::class.java)
    val activity: IssuesSearchActivity = controller.get()
    val viewModel: IssuesSearchViewModel by activity.viewModel()
    val webStarter: WebStarter by inject()
    val stateChanges = PublishSubject.create<IssuesSearchViewState>()
    val adapter by lazy { activity.issueList.adapter as IssuesAdapter }

    @Before
    fun setUp() {
        declareMock<IssuesSearchViewModel>()
        declareMock<WebStarter>()
        whenever(viewModel.states).thenReturn(stateChanges)
        controller.setup()
    }

    @Test
    fun `searchView query change emits expected intent`() {
        val testObserver = activity.intents.test()
        testObserver.assertNoValues()
        activity.searchView.setQuery("testQuery", false)
        testObserver.assertValue(IssuesSearchIntent.Query("testQuery"))
    }

    @Test
    fun `refreshLayout refreshing emits expected intent`() {
        activity.searchView.setQuery("testQuery", false)
        val testObserver = activity.intents.test()
        testObserver.assertNoValues()
        Shadows.shadowOf(activity.refreshLayout).onRefreshListener.onRefresh()
        testObserver.assertValue(IssuesSearchIntent.Refresh("testQuery"))
    }

    @Test
    fun `issue clicks starts web activity with expected url`() {
        stateChanges.onNext(
            IssuesSearchViewState.initial.copy(
                searchResults = listOf(createIssue(0), createIssue(1), createIssue(2))
            )
        )
        activity.issueList.layoutManager?.findViewByPosition(1)?.performClick()
        verify(webStarter).start(activity, "url1")
    }

    @Test
    fun `state change hides loading when error occurred`() {
        activity.loadingView.visible()
        stateChanges.onNext(IssuesSearchViewState.initial.copy(error = Exception()))
        assertTrue(activity.loadingView.isGone)
    }

    @Test
    fun `state change disables refresh layout refreshing when error occurred`() {
        activity.refreshLayout.isRefreshing = true
        stateChanges.onNext(IssuesSearchViewState.initial.copy(error = Exception()))
        assertFalse(activity.refreshLayout.isRefreshing)
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
    fun `state change disables refresh layout refreshing when results are empty`() {
        activity.refreshLayout.isRefreshing = true
        stateChanges.onNext(IssuesSearchViewState.initial.copy(searchResults = emptyList()))
        assertFalse(activity.refreshLayout.isRefreshing)
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
    fun `state change disables refresh layout refreshing when results are not empty`() {
        activity.refreshLayout.isRefreshing = true
        stateChanges.onNext(IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0))))
        assertFalse(activity.refreshLayout.isRefreshing)
    }

    @Test
    fun `state change shows issue list when search results are not empty`() {
        activity.issueList.gone()
        stateChanges.onNext(IssuesSearchViewState.initial.copy(searchResults = listOf(createIssue(0))))
        assertTrue(activity.issueList.isVisible)
    }

    @Test
    fun `state change sets expected loading state on adapter when next page is loading`() {
        adapter.isLoading = false
        stateChanges.onNext(IssuesSearchViewState.initial.copy(isLoadingNextPage = true))
        assertTrue(adapter.isLoading)
    }

    @Test
    fun `state change sets expected loading state on adapter when next page is not being loaded`() {
        adapter.isLoading = true
        stateChanges.onNext(IssuesSearchViewState.initial.copy(isLoadingNextPage = false))
        assertFalse(adapter.isLoading)
    }

    @Test
    fun `state change adds loading item at the of the list when next page is loading`() {
        assertEquals(0, adapter.items.size)
        stateChanges.onNext(
            IssuesSearchViewState.initial.copy(
                isLoadingNextPage = true,
                searchResults = listOf(createIssue(0), createIssue(1), createIssue(2), createIssue(3))
            )
        )
        assertTrue(adapter.items.last() is LoadingListItem)
    }

    @Test
    fun `state change does not add loading item to list when next page is not being loaded`() {
        assertEquals(0, adapter.items.size)
        stateChanges.onNext(
            IssuesSearchViewState.initial.copy(
                isLoadingNextPage = false,
                searchResults = listOf(createIssue(0), createIssue(1), createIssue(2), createIssue(3))
            )
        )
        assertFalse(adapter.items.last() is LoadingListItem)
    }

    @Test
    fun `state change adds issue items to list when search results are not empty`() {
        assertEquals(0, adapter.items.size)
        stateChanges.onNext(
            IssuesSearchViewState.initial.copy(
                searchResults = listOf(createIssue(0), createIssue(1))
            )
        )
        assertEquals(listOf(createIssue(0), createIssue(1)).map(::IssueListItem), adapter.items)
    }

}