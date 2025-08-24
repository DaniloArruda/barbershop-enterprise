package com.danilo.barbershop.enterprise.application.repository

import com.danilo.Appointment

fun interface AppointmentRepository {
    fun save(appointment: Appointment)
}
