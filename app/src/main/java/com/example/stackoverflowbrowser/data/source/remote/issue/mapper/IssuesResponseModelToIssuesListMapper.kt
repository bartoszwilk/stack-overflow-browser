package com.example.stackoverflowbrowser.data.source.remote.issue.mapper

import com.example.stackoverflowbrowser.data.entity.Issue
import com.example.stackoverflowbrowser.data.source.base.Mapper
import com.example.stackoverflowbrowser.data.source.remote.issue.model.IssuesResponseModel

class IssuesResponseModelToIssuesListMapper : Mapper<IssuesResponseModel, List<Issue>>() {

    private val issueModelToIssueMapper = IssueModelToIssueMapper()

    override fun mapOrReturnNull(from: IssuesResponseModel) =
        from.items?.let(issueModelToIssueMapper::mapOrSkip)
}