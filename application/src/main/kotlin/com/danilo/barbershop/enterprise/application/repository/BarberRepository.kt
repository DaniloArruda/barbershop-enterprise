package com.danilo.barbershop.enterprise.application.repository

import java.time.ZonedDateTime
import java.util.UUID

interface BarberRepository {
    fun isBarberAvailable(barberId: UUID, startAt: ZonedDateTime, endAt: ZonedDateTime): Boolean
}