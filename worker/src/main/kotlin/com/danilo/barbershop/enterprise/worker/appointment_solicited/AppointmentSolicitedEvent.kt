package com.danilo.barbershop.enterprise.worker.appointment_solicited

import com.danilo.barbershop.enterprise.application.usecase.appointment_solicited.AppointmentSolicitedRequest

data class AppointmentSolicitedEvent(
    val customerId: String,
    val barberId: String,
    val taskId: String,
    val dateTime: String
) {
    fun toRequest(): AppointmentSolicitedRequest {
        return AppointmentSolicitedRequest()
    }
}