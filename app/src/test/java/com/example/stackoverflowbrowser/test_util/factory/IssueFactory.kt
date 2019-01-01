package com.example.stackoverflowbrowser.test_util.factory

import com.example.stackoverflowbrowser.data.entity.Issue
import com.example.stackoverflowbrowser.data.entity.Owner


fun createIssue(salt: Int) =
    Issue(
        title = "title$salt",
        answersCount = salt,
        owner = Owner("name$salt", "avatarUrl$salt"),
        score = salt,
        url = "url$salt",
        isResolved = (salt % 2 == 0)
    )
