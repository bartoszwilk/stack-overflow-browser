package com.example.stackoverflowbrowser.data.source.remote.issue.mapper

import com.example.stackoverflowbrowser.data.entity.Issue
import com.example.stackoverflowbrowser.data.source.base.Mapper
import com.example.stackoverflowbrowser.data.source.remote.issue.model.IssueModel

class IssueModelToIssueMapper : Mapper<IssueModel, Issue>() {

    private val ownerModelToOwnerMapper = OwnerModelToOwnerMapper()

    override fun mapOrReturnNull(from: IssueModel) =
        from.owner
            ?.let { ownerModelToOwnerMapper.mapOrReturnNull(it) }
            ?.let { from.filterNulls() to it }
            ?.let { (issue, owner) -> Issue(issue!!.title!!, issue.answersCount!!, owner) }

    private fun IssueModel.filterNulls() =
        if (title != null && answersCount != null) this else null
}