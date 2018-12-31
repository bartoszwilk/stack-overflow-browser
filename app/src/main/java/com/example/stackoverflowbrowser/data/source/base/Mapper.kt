package com.example.stackoverflowbrowser.data.source.base

import com.example.stackoverflowbrowser.data.exception.MappingException

abstract class Mapper<in From : Any, out To> {

    abstract fun mapOrReturnNull(from: From): To?

    fun mapOrThrow(from: From): To = mapOrReturnNull(from) ?: throw MappingException(from)

    fun mapOrSkip(from: From): To? = mapOrReturnNull(from)

    fun mapOrThrow(from: Iterable<From?>) = map(from, this::mapAndAppendOrThrow)

    fun mapOrSkip(from: Iterable<From?>) = map(from, this::mapAndAppendOrSkip)

    private fun map(from: Iterable<From?>, mapAndAppendTo: (From, MutableList<To>) -> Unit) =
        with(mutableListOf<To>()) {
            from.filterNotNull().forEach { mapAndAppendTo(it, this) }
            return@with this
        } as List<To>

    private fun mapAndAppendOrThrow(it: From, to: MutableList<To>) {
        to.add(mapOrThrow(it))
    }

    private fun mapAndAppendOrSkip(it: From, to: MutableList<To>) {
        mapOrReturnNull(it)?.let { to.add(it) }
    }

}