package com.example.stackoverflowbrowser.data.exception

data class MappingException(val model: Any) : Exception() {

    override val message = "Couldn't map ${model.javaClass.name} to it's entity. Received data: $model"
}