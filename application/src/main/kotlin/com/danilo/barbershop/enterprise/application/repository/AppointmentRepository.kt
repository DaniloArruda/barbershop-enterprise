package com.danilo.barbershop.enterprise.application.repository

import com.danilo.com.danilo.barbershop.enterprise.domain.Appointment

fun interface AppointmentRepository {
    fun save(appointment: Appointment)
}
