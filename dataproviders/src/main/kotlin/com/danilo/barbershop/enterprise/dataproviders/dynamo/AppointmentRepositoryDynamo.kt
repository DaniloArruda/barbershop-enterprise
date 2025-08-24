package com.danilo.barbershop.enterprise.dataproviders.dynamo

import com.danilo.com.danilo.barbershop.enterprise.domain.Appointment
import com.danilo.barbershop.enterprise.application.repository.AppointmentRepository
import org.springframework.stereotype.Component

@Component
class AppointmentRepositoryDynamo : AppointmentRepository {
    override fun save(appointment: Appointment) {
        println("Appointment saved in DynamoDB")
    }
}