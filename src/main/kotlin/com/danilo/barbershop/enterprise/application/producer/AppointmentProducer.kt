package com.danilo.barbershop.enterprise.application.producer

import com.danilo.Appointment

interface AppointmentProducer {
    fun publishAppointmentCreated(appointment: Appointment)
    fun publishAppointmentRejected(appointment: Appointment)
}
