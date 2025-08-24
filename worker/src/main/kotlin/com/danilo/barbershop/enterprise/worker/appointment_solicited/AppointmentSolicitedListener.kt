package com.danilo.barbershop.enterprise.worker.appointment_solicited

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.danilo.barbershop.enterprise.application.usecase.appointment_solicited.AppointmentSolicitedUseCase
import org.springframework.web.bind.annotation.RequestMapping

@RestController
@RequestMapping("/appointment")
class AppointmentSolicitedListener(
    private val useCase: AppointmentSolicitedUseCase
) {

    @PostMapping("/solicit")
    fun process(@RequestBody event: AppointmentSolicitedEvent) {
        println("Processing appointment for customer ${event.customerId} with barber ${event.barberId} at ${event.dateTime}")
        val appointmentSolicitedRequest = event.toRequest()

        useCase.execute(appointmentSolicitedRequest)
    }
}