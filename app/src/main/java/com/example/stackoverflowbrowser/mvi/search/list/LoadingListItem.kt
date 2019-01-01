package com.example.stackoverflowbrowser.mvi.search.list

import com.example.stackoverflowbrowser._base.list.ListItem

val LOADING_ITEM_TYPE = LoadingListItem::class.java.hashCode()

object LoadingListItem : ListItem {

    override val type = LOADING_ITEM_TYPE
}