package com.danilo.barbershop.enterprise.dataproviders.kafka

import com.danilo.com.danilo.barbershop.enterprise.domain.Appointment
import com.danilo.barbershop.enterprise.application.producer.AppointmentProducer
import org.springframework.stereotype.Component

@Component
class AppointmentProducerKafka : AppointmentProducer {
    override fun publishAppointmentCreated(appointment: Appointment) {
        println("Appointment created event published to Kafka")
    }

    override fun publishAppointmentRejected(appointment: Appointment) {
        println("Appointment rejected event published to Kafka")
    }
}