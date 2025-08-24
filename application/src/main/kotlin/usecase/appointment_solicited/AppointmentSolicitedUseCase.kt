package usecase.appointment_solicited

import com.danilo.Appointment
import producer.AppointmentProducer
import repository.AppointmentRepository

class AppointmentSolicitedUseCase(
    private val appointmentRepository: AppointmentRepository,
    private val appointmentProducer: AppointmentProducer,
) {

    fun execute(appointmentSolicitedRequest: AppointmentSolicitedRequest) {
        val appointment = Appointment()

        appointmentRepository.save(appointment)
        appointmentProducer.publishAppointmentCreated(appointment)
    }
}