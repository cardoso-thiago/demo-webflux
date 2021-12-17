package br.com.cardoso.controller

import br.com.cardoso.model.TokenModel
import br.com.cardoso.service.WebCallService
import br.com.cardoso.util.Utils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/test")
class TestController(private val webCallService: WebCallService) {

    companion object {
        val LOG: Logger = LogManager.getLogger()
    }

    @GetMapping("/send")
    fun send(): Mono<String> {
        return webCallService.call()
    }

    @PostMapping("/send/post")
    fun sendPost(@RequestBody requestBody: String): Mono<String> {
        return webCallService.callPost(requestBody)
    }

    @GetMapping("/send/block")
    fun sendBlock(): Mono<String> {
        return webCallService.blockCall()
    }

    @GetMapping("/get")
    fun get(
        @RequestHeader("filter_header") filterHeader: String,
        @RequestHeader("token_header") token: String
    ): Mono<String> {
        LOG.info("Recebido o filter_header na requisição: {}", filterHeader)
        LOG.info("Recebido o token na requisição: {}", token)
        return Mono.fromCallable { "Olá!" }
    }

    @PostMapping("/post")
    fun post(
        @RequestHeader("filter_header") filterHeader: String,
        @RequestHeader("token_header") token: String,
        @RequestBody requestBody: String
    ): Mono<String> {
        LOG.info("Recebido o filter_header na requisição: {}", filterHeader)
        LOG.info("Recebido o token na requisição: {}", token)
        LOG.info("Recebido o body na requisição: {}", requestBody)
        return Mono.fromCallable { "Olá!" }
    }

    @GetMapping("/token")
    fun token(): TokenModel {
        return TokenModel(Utils.generateNonBlockingUUID())
    }
}