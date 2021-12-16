package br.com.cardoso.web

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.*

class WebTargetCustom {

    companion object {
        val LOG: Logger = LogManager.getLogger()
    }

    fun buildWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:8080")
            .filters { filters ->
                run {
                    filters.add(0, logRequest())
                    filters.add(1, logResponse())
                }
            }
            .build()
    }

    private fun logRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
            LOG.info("Vai adicionar header na requisição: {} {}", clientRequest.method(), clientRequest.url())
            Mono.just(ClientRequest.from(clientRequest)
                .header("filter_header", UUID.randomUUID().toString())
                .build())
        }
    }

    private fun logResponse(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
            LOG.info("Código de retorno: {}", clientResponse.statusCode())
            Mono.just(clientResponse)
        }
    }
}