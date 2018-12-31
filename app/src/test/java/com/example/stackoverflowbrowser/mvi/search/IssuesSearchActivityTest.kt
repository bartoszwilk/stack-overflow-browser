package com.example.stackoverflowbrowser.mvi.search

import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.test.KoinTest
import org.koin.test.declareMock
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class IssuesSearchActivityTest : KoinTest {

    val controller = Robolectric.buildActivity(IssuesSearchActivity::class.java)
    val activity: IssuesSearchActivity = controller.get()
    val viewModel: IssuesSearchViewModel by activity.viewModel()

    @Before
    fun setUp() {
        declareMock<IssuesSearchViewModel>()
        whenever(viewModel.states).thenReturn(Observable.empty())
        controller.setup()
    }

    @Test
    fun `searchView query change emits expected intent`() {
        val testObserver = activity.intents.test()
        activity.searchView.setQuery("testQuery", false)
        testObserver.assertValue(IssuesSearchIntent.Query("testQuery"))
    }

}