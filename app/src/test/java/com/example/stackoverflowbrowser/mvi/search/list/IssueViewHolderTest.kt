package com.example.stackoverflowbrowser.mvi.search.list

import android.view.LayoutInflater
import androidx.test.core.app.ApplicationProvider
import com.example.stackoverflowbrowser.R
import com.example.stackoverflowbrowser.data.entity.Issue
import com.example.stackoverflowbrowser.data.entity.Owner
import kotlinx.android.synthetic.main.viewholder_issue.view.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.stopKoin
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class IssueViewHolderTest {

    val itemView by lazy {
        LayoutInflater
            .from(ApplicationProvider.getApplicationContext())
            .inflate(R.layout.viewholder_issue, null)
    }
    val viewHolder by lazy { IssueViewHolder(itemView) }
    val issueItem = IssueItem(Issue("testTitle", 123, Owner("testName", null)))

    @After
    fun tearDown() {
        stopKoin() //temporary workaround
    }

    @Test
    fun `bind binds title to expected view`() {
        viewHolder.bind(issueItem)
        assertEquals("testTitle", itemView.title.text)
    }

    @Test
    fun `bind binds answers count to expected view`() {
        viewHolder.bind(issueItem)
        assertEquals("123", itemView.answersCount.text)
    }

    @Test
    fun `bind binds user name to expected view`() {
        viewHolder.bind(issueItem)
        assertEquals("testName", itemView.username.text)
    }
}