package br.com.cardoso.controller

import br.com.cardoso.service.WebCallService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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

    @GetMapping("/get")
    fun get(@RequestHeader("filter_header") header: String): Mono<String> {
        LOG.info("Recebido o header na requisição: {}", header)
        return Mono.fromCallable { "Olá!" }
    }
}