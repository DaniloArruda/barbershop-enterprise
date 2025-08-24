package com.danilo.barbershop.enterprise.worker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.danilo.barbershop.enterprise"])
class BarbershopEnterpriseWorker

fun main(args: Array<String>) {
    runApplication<BarbershopEnterpriseWorker>(*args)
}