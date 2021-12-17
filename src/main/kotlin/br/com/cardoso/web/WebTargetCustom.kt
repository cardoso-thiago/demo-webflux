package br.com.cardoso.web

import br.com.cardoso.model.TokenModel
import br.com.cardoso.util.Utils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

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
                    filters.add(1, addTokenRequest())
                    filters.add(2, logResponse())
                }
            }
            .build()
    }

    private fun logRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
            LOG.info("Vai adicionar header na requisição: {} {}", clientRequest.method(), clientRequest.url())
            Mono.just(
                ClientRequest.from(clientRequest)
                    .header("filter_header", Utils.generateNonBlockingUUID())
                    .build()
            )
        }
    }

    private fun addTokenRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
            LOG.info("Já deve possuir o filter_header: {} ", clientRequest.headers()["filter_header"])
            LOG.info("Vai adicionar token na requisição: {} {}", clientRequest.method(), clientRequest.url())
            val client = WebClient.builder().baseUrl("http://localhost:8080").build()
            client.get().uri("/test/token").exchangeToMono { response ->
                response.bodyToMono(TokenModel::class.java).map { tokenModel ->
                    ClientRequest.from(clientRequest)
                        .header("token_header", tokenModel.tokenValue)
                        .build()
                }
            }
        }
    }

    private fun logResponse(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
            LOG.info("Código de retorno: {}", clientResponse.statusCode())
            Mono.just(clientResponse)
        }
    }
}