package com.example.stackoverflowbrowser.util.extension

import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent

val RecyclerViewScrollEvent.isScrollDown: Boolean
    get() = dy() > 0

val RecyclerViewScrollEvent.isScrollUp: Boolean
    get() = dy() < 0