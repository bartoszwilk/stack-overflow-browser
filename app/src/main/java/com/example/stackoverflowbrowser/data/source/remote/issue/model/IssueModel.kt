package com.example.stackoverflowbrowser.data.source.remote.issue.model

import com.google.gson.annotations.SerializedName

data class IssueModel(
    val title: String?,
    val score: Int?,
    @SerializedName("is_answered") val isResolved: Boolean?,
    @SerializedName("answer_count") val answersCount: Int?,
    @SerializedName("link") val url: String?,
    val owner: OwnerModel?
)