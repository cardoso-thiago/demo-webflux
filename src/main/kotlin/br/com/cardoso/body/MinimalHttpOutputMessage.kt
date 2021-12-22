package br.com.cardoso.body

import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ReactiveHttpOutputMessage
import reactor.core.publisher.Mono
import java.util.function.Supplier

internal class MinimalHttpOutputMessage private constructor() : ReactiveHttpOutputMessage {

    override fun getHeaders(): HttpHeaders {
        return HttpHeaders.EMPTY
    }

    override fun bufferFactory(): DataBufferFactory {
        throw Exception()
    }

    override fun beforeCommit(action: Supplier<out Mono<Void>>) {
    }

    override fun isCommitted(): Boolean {
        return false
    }

    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
        return Mono.empty()
    }

    override fun writeAndFlushWith(body: Publisher<out Publisher<out DataBuffer>>): Mono<Void> {
        return Mono.empty()
    }

    override fun setComplete(): Mono<Void> {
        return Mono.empty()
    }

    companion object {
        var INSTANCE = MinimalHttpOutputMessage()
    }
}