package com.danilo.barbershop.enterprise.application.producer

import com.danilo.barbershop.enterprise.domain.Appointment

interface AppointmentProducer {
    fun publishAppointmentCreated(appointment: Appointment)
    fun publishAppointmentRejected(appointment: Appointment)
}
