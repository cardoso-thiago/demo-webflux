package br.com.cardoso.body

import org.springframework.http.ReactiveHttpOutputMessage

import org.springframework.web.reactive.function.BodyInserter

import java.util.concurrent.atomic.AtomicReference

internal class SimpleValueReceiver<T>(private val clazz: Class<T>) : InsertionReceiver<T> {

    private val reference: AtomicReference<Any> = AtomicReference(DUMMY)

    override fun receiveValue(bodyInserter: BodyInserter<*, out ReactiveHttpOutputMessage?>?): T {
        if (bodyInserter != null) {
            demandValueFrom(bodyInserter)
        }
        return receivedValue()
    }

    private fun demandValueFrom(bodyInserter: BodyInserter<*, out ReactiveHttpOutputMessage>) {
        val inserter = bodyInserter as BodyInserter<*, ReactiveHttpOutputMessage>
        inserter.insert(
            MinimalHttpOutputMessage.INSTANCE,
            SingleWriterContext(WriteToConsumer { newValue: T -> reference.set(newValue) })
        )
    }

    private fun receivedValue(): T {
        val value = reference.get()
        reference.set(DUMMY)
        val validatedValue: T = if (value === DUMMY) {
            throw RuntimeException("Value was not received, Check your inserter worked properly")
        } else if (!clazz.isAssignableFrom(value.javaClass)) {
            throw RuntimeException(
                "Value has unexpected type ("
                        + value.javaClass.typeName
                        + ") instead of (" + clazz.typeName + ")"
            )
        } else {
            clazz.cast(value)
        }
        return validatedValue
    }

    companion object {
        private val DUMMY = Any()
    }
}
