package com.danilo.barbershop.enterprise.application.usecase.appointment_solicited

import java.time.ZonedDateTime
import java.util.UUID

class AppointmentSolicitedRequest(
    val barberId: UUID,
    val customerId: UUID,
    val taskId: UUID,
    val startAt: ZonedDateTime,
    val endAt: ZonedDateTime,
) {

}
