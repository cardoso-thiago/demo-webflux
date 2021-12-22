package br.com.cardoso.service

import br.com.cardoso.web.WebTargetCustom
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class WebCallService(private val environment: Environment) {

    private val webTargetCustom = WebTargetCustom()

    fun call(): Mono<String> {
        val client = webTargetCustom.buildWebClient(getPort()!!)
        return client.get().uri("/test/get").retrieve().bodyToMono(String::class.java)
    }

    fun callPost(body: String): Mono<String> {
        val client = webTargetCustom.buildWebClient(getPort()!!)
        return client.post().uri("/test/post").body(Mono.just(body), String::class.java).retrieve()
            .bodyToMono(String::class.java)
    }

    fun blockCall(): Mono<String> {
        val client = webTargetCustom.buildBlockWebClient(getPort()!!)
        return client.get().uri("/test/get").retrieve().bodyToMono(String::class.java)
    }

    fun getPort() =
        if (System.getProperty("server.port") == null) environment.getProperty("server.port") else System.getProperty("server.port")
}