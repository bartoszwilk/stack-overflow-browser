package com.example.stackoverflowbrowser.data.source.remote.util

import com.example.stackoverflowbrowser.util.constants.STACK_OVERFLOW_BASE_API_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider {

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(STACK_OVERFLOW_BASE_API_URL)
        .build()
}