package com.example.stackoverflowbrowser.mvi.search.list

import android.view.LayoutInflater
import androidx.test.core.app.ApplicationProvider
import com.example.stackoverflowbrowser.R
import com.example.stackoverflowbrowser.test_util.factory.createIssue
import kotlinx.android.synthetic.main.viewholder_issue.view.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class IssueViewHolderTest: AutoCloseKoinTest() {

    val itemView by lazy {
        LayoutInflater
            .from(ApplicationProvider.getApplicationContext())
            .inflate(R.layout.viewholder_issue, null)
    }
    val viewHolder by lazy { IssueViewHolder(itemView) }
    val issueItem = IssueListItem(createIssue(123))

    @After
    fun tearDown() {
        stopKoin() //temporary workaround
    }

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
}