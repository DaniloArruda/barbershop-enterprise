package producer

import com.danilo.Appointment

interface AppointmentProducer {
    fun publishAppointmentCreated(appointment: Appointment)
    fun publishAppointmentRejected(appointment: Appointment)
}
