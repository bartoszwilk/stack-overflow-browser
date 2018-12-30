package com.example.stackoverflowbrowser

import android.app.Application
import com.example.stackoverflowbrowser.di.issuesSearchModule
import org.koin.android.ext.android.startKoin

class StackOverflowBrowserApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(issuesSearchModule))
    }
}