package com.example.stackoverflowbrowser.mvi.search.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.example.stackoverflowbrowser.R
import com.example.stackoverflowbrowser.data.entity.Issue
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.viewholder_issue.view.*

class IssueViewHolder(
    itemView: View,
    val issueClicks: Subject<Issue>
) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: IssueListItem) {
        val issue = item.issue
        with(itemView) {
            title.text = issue.title
            answersCount.text = itemView.context.resources.getQuantityString(
                R.plurals.comments_amount,
                issue.answersCount,
                issue.answersCount
            )
            username.text = issue.owner.name
            score.text = issue.score.toString()
            score.isSelected = issue.isResolved
            Glide.with(itemView).load(issue.owner.avatarUrl).into(avatar)
            clicks().map { issue }.subscribe(issueClicks)
        }
    }
}