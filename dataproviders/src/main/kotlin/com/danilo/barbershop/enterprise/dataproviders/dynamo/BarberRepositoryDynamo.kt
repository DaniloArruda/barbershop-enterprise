package com.danilo.barbershop.enterprise.dataproviders.dynamo

import com.danilo.barbershop.enterprise.application.repository.BarberRepository
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.UUID

@Component
class BarberRepositoryDynamo : BarberRepository {
    override fun isBarberAvailable(
        barberId: UUID,
        startAt: ZonedDateTime,
        endAt: ZonedDateTime
    ): Boolean {
        println("Barber $barberId is available between $startAt and $endAt")
        return true
    }
}