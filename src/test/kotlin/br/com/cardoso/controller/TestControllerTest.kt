package br.com.cardoso.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestControllerTest {

    @Autowired
    lateinit var webClient: WebTestClient

    @LocalServerPort
    var randomServerPort = 0

    @Test
    @DisplayName("Should get Olá")
    fun shouldGetOla() {
        System.setProperty("server.port", randomServerPort.toString())

        val responseBody = webClient.get().uri("/test/send")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult().responseBody
        assertEquals("Olá!", responseBody)
    }
}