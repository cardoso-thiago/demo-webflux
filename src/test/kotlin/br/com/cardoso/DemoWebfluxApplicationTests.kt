package br.com.cardoso

import br.com.cardoso.controller.TestController
import br.com.cardoso.service.WebCallService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [TestController::class])
@Import(WebCallService::class)
class DemoWebfluxApplicationTests {

    @Autowired
    lateinit var webClient: WebTestClient

    @Test
    fun testSend() {
        webClient.get().uri("/test/send")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
    }
}
