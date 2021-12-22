package br.com.cardoso.web

import br.com.cardoso.body.InsertionReceiver
import br.com.cardoso.model.TokenModel
import br.com.cardoso.util.Utils
import io.netty.handler.logging.LogLevel
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.transport.logging.AdvancedByteBufFormat
import java.util.*

class WebTargetCustom() {

    companion object {
        val LOG: Logger = LogManager.getLogger()
    }

    fun buildWebClient(port: String): WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:$port")
            //Habilitando logs de response e request
            .clientConnector(
                ReactorClientHttpConnector(
                    HttpClient.create()
                        .wiretap("webfluxlog", LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL)
                )
            )
            .filters { filters ->
                run {
                    filters.add(0, addFilterHeader())
                    filters.add(1, addTokenRequest(port))
                    filters.add(2, logRequest())
                    filters.add(3, logResponse())
                }
            }
            .build()
    }

    fun buildBlockWebClient(port: String): WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:$port")
            .filters { filters ->
                run {
                    filters.add(1, addBlockTokenRequest())
                }
            }
            .build()
    }

    private fun addFilterHeader(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
            LOG.info("Vai adicionar header na requisição: {} {}", clientRequest.method(), clientRequest.url())
            Mono.just(
                ClientRequest.from(clientRequest)
                    .header("filter_header", Utils.generateNonBlockingUUID())
                    .build()
            )
        }
    }

    private fun addBlockTokenRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
            LOG.info("Já deve possuir o filter_header: {} ", clientRequest.headers()["filter_header"])
            LOG.info("Vai adicionar token na requisição: {} {}", clientRequest.method(), clientRequest.url())
            Mono.just(
                ClientRequest.from(clientRequest)
                    .header("token_header", UUID.randomUUID().toString())
                    .build()
            )
        }
    }

    private fun addTokenRequest(port: String): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
            LOG.info("Já deve possuir o filter_header: {} ", clientRequest.headers()["filter_header"])
            LOG.info("Vai adicionar token na requisição: {} {}", clientRequest.method(), clientRequest.url())
            val client = WebClient.builder().baseUrl("http://localhost:$port").build()
            client.get().uri("/test/token").exchangeToMono { response ->
                response.bodyToMono(TokenModel::class.java).map { tokenModel ->
                    ClientRequest.from(clientRequest)
                        .header("token_header", tokenModel.tokenValue)
                        .build()
                }
            }
        }
    }

    private fun logRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
            try {
                val receiveValue = InsertionReceiver.forClass(String::class.java).receiveValue(clientRequest.body())
                LOG.info("Logando o body no filter: {}", receiveValue)
            } catch (e: Exception) {
                LOG.info("Body não encontrado na request.")
            }

            Mono.just(clientRequest)
        }
    }

    private fun logResponse(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
            LOG.info("Código de retorno: {}", clientResponse.statusCode())
            Mono.just(clientResponse)
        }
    }
}