package repository

import com.danilo.Appointment

fun interface AppointmentRepository {
    fun save(appointment: Appointment)
}
