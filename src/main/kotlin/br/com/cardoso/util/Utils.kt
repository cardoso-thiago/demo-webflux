package br.com.cardoso.util

import java.util.*
import java.util.concurrent.ThreadLocalRandom

class Utils {
    companion object {
        fun generateNonBlockingUUID() =
            UUID(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong()).toString()
    }
}

