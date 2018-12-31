package com.example.stackoverflowbrowser.data.source.remote.issue.model

import com.google.gson.annotations.SerializedName

data class IssueModel(
    val title: String?,
    @SerializedName("answer_count") val answersCount: Int?,
    val owner: OwnerModel?
)