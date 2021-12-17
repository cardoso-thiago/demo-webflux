package br.com.cardoso.service

import br.com.cardoso.web.WebTargetCustom
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class WebCallService {

    private val webTargetCustom = WebTargetCustom()

    fun call(): Mono<String> {
        val client = webTargetCustom.buildWebClient()
        return client.get().uri("/test/get").retrieve().bodyToMono(String::class.java)
    }

    fun blockCall(): Mono<String> {
        val client = webTargetCustom.buildBlockWebClient()
        return client.get().uri("/test/get").retrieve().bodyToMono(String::class.java)
    }
}