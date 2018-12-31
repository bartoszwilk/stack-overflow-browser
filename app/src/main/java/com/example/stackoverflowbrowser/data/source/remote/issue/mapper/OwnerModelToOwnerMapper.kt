package com.example.stackoverflowbrowser.data.source.remote.issue.mapper

import com.example.stackoverflowbrowser.data.entity.Owner
import com.example.stackoverflowbrowser.data.source.base.Mapper
import com.example.stackoverflowbrowser.data.source.remote.issue.model.OwnerModel

class OwnerModelToOwnerMapper : Mapper<OwnerModel, Owner>() {

    override fun mapOrReturnNull(from: OwnerModel) =
        from.filterNulls()?.let { Owner(it.name!!, it.avatarUrl) }

    private fun OwnerModel.filterNulls() = if (name != null) this else null
}