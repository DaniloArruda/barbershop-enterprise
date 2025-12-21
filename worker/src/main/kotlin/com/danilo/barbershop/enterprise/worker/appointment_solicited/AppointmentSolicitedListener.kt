package com.danilo.barbershop.enterprise.worker.appointment_solicited

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import com.danilo.barbershop.enterprise.application.usecase.appointment_solicited.AppointmentSolicitedUseCase

@Component
class AppointmentSolicitedListener(
    private val useCase: AppointmentSolicitedUseCase
) {

    @KafkaListener(
        topics = ["appointment.solicited"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun process(event: AppointmentSolicitedEvent) {
        println("Processing appointment for customer ${event.customerId} with barber ${event.barberId} at ${event.startAt}")
        val appointmentSolicitedRequest = event.toRequest()

        useCase.execute(appointmentSolicitedRequest)
    }
}