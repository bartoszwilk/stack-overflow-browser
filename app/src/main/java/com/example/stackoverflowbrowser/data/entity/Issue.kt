package com.example.stackoverflowbrowser.data.entity

data class Issue(
    val title: String,
    val answersCount: Int,
    val owner: Owner
)