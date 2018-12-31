package com.example.stackoverflowbrowser.data.source.remote.issue.request

import com.example.stackoverflowbrowser.data.source.remote.issue.model.IssuesResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchPagedIssuesApiRequest {

    @GET("/2.2/search")
    fun getIssues(
        @Query("intitle") title: String,
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int,
        @Query("site") site: String
    ): Observable<IssuesResponseModel>
}