package com.example.stackoverflowbrowser.mvi.search.list

import android.view.LayoutInflater
import androidx.test.core.app.ApplicationProvider
import com.example.stackoverflowbrowser.R
import com.example.stackoverflowbrowser.data.entity.Issue
import com.example.stackoverflowbrowser.test_util.factory.createIssue
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.viewholder_issue.view.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class IssueViewHolderTest : AutoCloseKoinTest() {

    val itemView by lazy {
        LayoutInflater
            .from(ApplicationProvider.getApplicationContext())
            .inflate(R.layout.viewholder_issue, null)
    }
    val issueClicks = PublishSubject.create<Issue>()
    val viewHolder by lazy { IssueViewHolder(itemView, issueClicks) }
    val issueItem = IssueListItem(createIssue(123))

    @Test
    fun `bind binds title to expected view`() {
        viewHolder.bind(issueItem)
        assertEquals("title123", itemView.title.text)
    }

    @Test
    fun `bind binds answers count to expected view`() {
        viewHolder.bind(issueItem)
        assertEquals("123 comments", itemView.answersCount.text)
    }

    @Test
    fun `bind binds user name to expected view`() {
        viewHolder.bind(issueItem)
        assertEquals("name123", itemView.username.text)
    }

    @Test
    fun `bind binds score to expected view`() {
        viewHolder.bind(issueItem)
        assertEquals("123", itemView.score.text)
    }

    @Test
    fun `bind binds answersCount to expected view when answers is zero`() {
        viewHolder.bind(IssueListItem(createIssue(0)))
        assertEquals("0 comments", itemView.answersCount.text)
    }

    @Test
    fun `bind binds answersCount to expected view when answersCount is singular number`() {
        viewHolder.bind(IssueListItem(createIssue(1)))
        assertEquals("1 comment", itemView.answersCount.text)
    }

    @Test
    fun `bind binds answersCount to expected view when answersCount is plural number`() {
        viewHolder.bind(issueItem)
        assertEquals("123 comments", itemView.answersCount.text)
    }

    @Test
    fun `bind observers for itemView clicks`() {
        viewHolder.bind(issueItem)
        val testObserver = viewHolder.issueClicks.test()
        testObserver.assertNoValues()
        itemView.performClick()
        testObserver.assertValue(createIssue(123))
    }
}