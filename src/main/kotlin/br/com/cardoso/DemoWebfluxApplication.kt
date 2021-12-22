package br.com.cardoso

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux
import reactor.blockhound.BlockHound

@SpringBootApplication
@EnableWebFlux
class DemoWebfluxApplication

fun main(args: Array<String>) {
//    BlockHound.install()
    runApplication<DemoWebfluxApplication>(*args)
}
