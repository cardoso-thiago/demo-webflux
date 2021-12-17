package br.com.cardoso.body

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.*
import java.util.function.Consumer

internal class OneValueConsumption<T>(consumer: Consumer<T>?) : Subscriber<T> {
    private val consumer: Consumer<T>
    private var remainedAccepts: Int

    override fun onSubscribe(s: Subscription) {
        s.request(1)
    }

    override fun onNext(o: T) {
        remainedAccepts -= if (remainedAccepts > 0) {
            consumer.accept(o)
            1
        } else {
            throw RuntimeException("No more values can be consumed")
        }
    }

    override fun onError(t: Throwable?) {
        throw RuntimeException("Single value was not consumed", t)
    }

    override fun onComplete() {
        // nothing
    }

    init {
        this.consumer = Objects.requireNonNull(consumer)!!
        remainedAccepts = 1
    }
}