package com.example.stackoverflowbrowser.data.source.remote.issue.model

import com.google.gson.annotations.SerializedName

data class OwnerModel(
    @SerializedName("display_name") val name: String?,
    @SerializedName("profile_image") val avatarUrl: String?
)