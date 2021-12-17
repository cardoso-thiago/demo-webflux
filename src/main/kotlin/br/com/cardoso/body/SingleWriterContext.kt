package br.com.cardoso.body

import org.springframework.http.codec.HttpMessageWriter
import org.springframework.http.server.reactive.ServerHttpRequest

import org.springframework.web.reactive.function.BodyInserter
import java.util.*

internal class SingleWriterContext(writer: HttpMessageWriter<*>) : BodyInserter.Context {
    private val singleWriterList: List<HttpMessageWriter<*>>
    override fun messageWriters(): List<HttpMessageWriter<*>> {
        return singleWriterList
    }

    override fun serverRequest(): Optional<ServerHttpRequest> {
        return Optional.empty()
    }

    override fun hints(): Map<String, Any> {
        return mapOf()
    }

    init {
        singleWriterList = java.util.List.of(writer)
    }
}