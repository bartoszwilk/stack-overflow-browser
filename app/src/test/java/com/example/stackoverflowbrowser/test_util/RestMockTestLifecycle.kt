package com.example.stackoverflowbrowser.test_util

import io.appflate.restmock.RESTMockServerStarter
import io.appflate.restmock.android.AndroidLocalFileParser
import io.appflate.restmock.android.AndroidLogger
import org.robolectric.DefaultTestLifecycle
import org.robolectric.RuntimeEnvironment
import java.lang.reflect.Method

open class RestMockTestLifecycle : DefaultTestLifecycle() {

    override fun beforeTest(method: Method?) {
        RESTMockServerStarter.startSync(AndroidLocalFileParser(RuntimeEnvironment.application), AndroidLogger())
        super.beforeTest(method)
    }
}