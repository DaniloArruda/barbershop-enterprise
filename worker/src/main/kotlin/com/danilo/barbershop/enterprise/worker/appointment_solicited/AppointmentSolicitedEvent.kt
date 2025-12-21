package com.danilo.barbershop.enterprise.worker.appointment_solicited

import com.danilo.barbershop.enterprise.application.usecase.appointment_solicited.AppointmentSolicitedRequest
import java.time.ZonedDateTime
import java.util.UUID

data class AppointmentSolicitedEvent(
    val customerId: String,
    val barberId: String,
    val taskId: String,
    val startAt: String,
    val endAt: String,
) {
    fun toRequest(): AppointmentSolicitedRequest {
        return AppointmentSolicitedRequest(
            barberId = UUID.fromString(barberId),
            customerId = UUID.fromString(customerId),
            taskId = UUID.fromString(taskId),
            startAt = ZonedDateTime.parse(startAt),
            endAt = ZonedDateTime.parse(endAt),
        )
    }
}