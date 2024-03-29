package com.example.stackoverflowbrowser.data.entity

data class Issue(
    val title: String,
    val answersCount: Int,
    val score: Int,
    val isResolved: Boolean,
    val url: String,
    val owner: Owner
)