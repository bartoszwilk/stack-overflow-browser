package com.example.stackoverflowbrowser.data.remote

import com.example.stackoverflowbrowser.data.entity.Issue
import com.example.stackoverflowbrowser.data.entity.Owner
import com.example.stackoverflowbrowser.data.source.base.remote.RemoteIssuesDataSource
import com.example.stackoverflowbrowser.test_util.RestMockRobolectricTestRunner
import com.example.stackoverflowbrowser.util.constants.STACK_OVERFLOW_BASE_API_URL
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.utils.QueryParam
import io.appflate.restmock.utils.RequestMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(RestMockRobolectricTestRunner::class)
class RemoteIssuesDataSourceTest {

    lateinit var dataSource: RemoteIssuesDataSource
    val FILE_RESPONSE_SUCCESS = "issues_search_results/result_success.json"

    @Before
    fun setUp() {
        STACK_OVERFLOW_BASE_API_URL = RESTMockServer.getUrl()
        dataSource = RemoteIssuesDataSource()
    }

    @Test
    fun `getPage streams expected issues`() {
        val expectedParams = arrayOf(
            QueryParam("intitle", "android"),
            QueryParam("site", "stackoverflow"),
            QueryParam("page", "5"),
            QueryParam("pagesize", "20")
        )
        RESTMockServer.whenGET(RequestMatchers.hasExactQueryParameters(*expectedParams))
            .thenReturnFile(200, FILE_RESPONSE_SUCCESS)
        val expectedIssues = listOf(
            Issue("testTitle1", 1, 3, false, "url1", Owner("Ricky Zheng", "profileImage1")),
            Issue("testTitle2", 2, 1, true, "url2", Owner("Mark tester", null)),
            Issue("testTitle3", 0, 4, false, "url3", Owner("bibscy", "profileImage3"))
        )
        dataSource.getPage("android", 5, 20)
            .test()
            .assertValue(expectedIssues)
            .assertNoErrors()
    }
}