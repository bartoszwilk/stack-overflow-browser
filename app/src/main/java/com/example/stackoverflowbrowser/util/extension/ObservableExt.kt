package com.example.stackoverflowbrowser.util.extension

import io.reactivex.Observable

fun <T> Observable<T>.ofNotType(klass: Class<*>) = filter { klass.isInstance(it).not() }!!