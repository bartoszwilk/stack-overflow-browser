package com.example.stackoverflowbrowser.data.source.base

import com.example.stackoverflowbrowser.data.entity.Issue
import io.reactivex.Observable

interface IssuesDataSource {

    fun getPage(title: String, page: Int, pageSize: Int): Observable<List<Issue>>
}