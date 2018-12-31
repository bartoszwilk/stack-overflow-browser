package com.example.stackoverflowbrowser.test_util

import org.junit.runners.model.InitializationError
import org.robolectric.RobolectricTestRunner

open class RestMockRobolectricTestRunner @Throws(InitializationError::class)
constructor(klass: Class<*>) : RobolectricTestRunner(klass) {

    public override fun getTestLifecycleClass() = RestMockTestLifecycle::class.java
}