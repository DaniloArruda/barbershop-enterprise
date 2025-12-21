package com.danilo.com.danilo.barbershop.enterprise.domain

import java.time.ZonedDateTime
import java.util.UUID

class Appointment(
    val id: UUID = UUID.randomUUID(),
    val barberId: UUID,
    val customerId: UUID,
    val taskId: UUID,
    val startAt: ZonedDateTime,
    val endAt: ZonedDateTime,
    val status: AppointmentStatus = AppointmentStatus.CREATED,
) {

}

enum class AppointmentStatus {
    CREATED,
    COMPLETED,
    CANCELLED,
}
