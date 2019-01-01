package com.example.stackoverflowbrowser.util.web

import android.app.Activity
import android.content.Intent
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import pl.wp.videostar.viper.web.WebActivity
import pl.wp.videostar.viper.web.WebStarter

@RunWith(RobolectricTestRunner::class)
class WebStarterTest : AutoCloseKoinTest() {

    val activity = Robolectric.setupActivity(Activity::class.java)

    @Test
    fun `start starts expected activity`() {
        val starter = Intent(activity, WebActivity::class.java)
        WebStarter().start(activity, "URL")
        Assert.assertTrue(Shadows.shadowOf(activity).nextStartedActivity.filterEquals(starter))
    }

    @Test
    fun `start prepare intent with expected extras`() {
        WebStarter().start(activity, "URL")
        val startedIntentUrl = Shadows.shadowOf(activity).nextStartedActivity.getStringExtra("URL_EXTRA")
        val correctUrl = "URL"
        Assert.assertEquals(startedIntentUrl, correctUrl)
    }

}