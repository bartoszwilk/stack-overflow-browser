package com.example.stackoverflowbrowser.data.source.base.remote

import com.example.stackoverflowbrowser.data.source.base.IssuesDataSource
import com.example.stackoverflowbrowser.data.source.remote.issue.mapper.IssuesResponseModelToIssuesListMapper
import com.example.stackoverflowbrowser.data.source.remote.issue.request.SearchPagedIssuesApiRequest
import com.example.stackoverflowbrowser.data.source.remote.util.RetrofitProvider

class RemoteIssuesDataSource :
    IssuesDataSource {

    private val mapper = IssuesResponseModelToIssuesListMapper()

    private val retrofit = RetrofitProvider().retrofit

    override fun getPage(title: String, page: Int, pageSize: Int) =
        retrofit
            .create(SearchPagedIssuesApiRequest::class.java)
            .getIssues(title, page, pageSize, "stackoverflow")
            .map(mapper::mapOrThrow)!! //inferred
}