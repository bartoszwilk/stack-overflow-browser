package com.example.stackoverflowbrowser.mvi.search.list

import com.example.stackoverflowbrowser._base.list.ListItem
import com.example.stackoverflowbrowser.data.entity.Issue

val ISSUE_ITEM_TYPE = IssueItem::class.java.hashCode()

data class IssueItem(val issue: Issue) : ListItem {

    override val type = ISSUE_ITEM_TYPE
}