package br.com.cardoso.body

import org.reactivestreams.Publisher
import org.springframework.core.ResolvableType
import org.springframework.http.MediaType
import org.springframework.http.ReactiveHttpOutputMessage
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.lang.Nullable
import reactor.core.publisher.Mono
import java.util.function.Consumer

internal class WriteToConsumer<T>(consumer: Consumer<T>) : HttpMessageWriter<T> {

    private val consumer: Consumer<T>
    private val mediaTypes: List<MediaType>
    override fun getWritableMediaTypes(): List<MediaType> {
        return mediaTypes
    }

    override fun canWrite(elementType: ResolvableType, @Nullable mediaType: MediaType?): Boolean {
        return true
    }

    override fun write(
        inputStream: Publisher<out T>,
        elementType: ResolvableType,
        mediaType: MediaType?,
        message: ReactiveHttpOutputMessage,
        hints: MutableMap<String, Any>
    ): Mono<Void> {
        inputStream.subscribe(OneValueConsumption(consumer))
        return Mono.empty()
    }

    init {
        this.consumer = consumer
        mediaTypes = listOf(MediaType.ALL)
    }
}