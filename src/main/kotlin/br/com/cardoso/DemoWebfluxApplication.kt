package br.com.cardoso

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoWebfluxApplication

fun main(args: Array<String>) {
	runApplication<DemoWebfluxApplication>(*args)
}
