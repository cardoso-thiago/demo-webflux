package br.com.cardoso.body

import org.springframework.http.ReactiveHttpOutputMessage

import org.springframework.web.reactive.function.BodyInserter

interface InsertionReceiver<T> {
    fun receiveValue(bodyInserter: BodyInserter<*, out ReactiveHttpOutputMessage?>?): T

    companion object {
        fun <T> forClass(clazz: Class<T>): InsertionReceiver<T> {
            return SimpleValueReceiver(clazz)
        }
    }
}