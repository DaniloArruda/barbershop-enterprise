package com.danilo.barbershop.enterprise.application.usecase.appointment_solicited

import com.danilo.com.danilo.barbershop.enterprise.domain.Appointment
import org.springframework.stereotype.Service
import com.danilo.barbershop.enterprise.application.producer.AppointmentProducer
import com.danilo.barbershop.enterprise.application.repository.AppointmentRepository
import com.danilo.barbershop.enterprise.application.repository.BarberRepository

@Service
class AppointmentSolicitedUseCase(
    private val appointmentRepository: AppointmentRepository,
    private val appointmentProducer: AppointmentProducer,
    private val barberRepository: BarberRepository,
) {

    fun execute(appointmentSolicitedRequest: AppointmentSolicitedRequest) {
        val appointment = Appointment(
            barberId = appointmentSolicitedRequest.barberId,
            customerId = appointmentSolicitedRequest.customerId,
            taskId = appointmentSolicitedRequest.taskId,
            startAt = appointmentSolicitedRequest.startAt,
            endAt = appointmentSolicitedRequest.endAt,
        )

        val isAvailable = barberRepository.isBarberAvailable(
            appointment.barberId,
            appointment.startAt,
            appointment.endAt
        )

        if (isAvailable) {
            appointmentRepository.save(appointment)
            appointmentProducer.publishAppointmentCreated(appointment)
        } else {
            appointmentProducer.publishAppointmentRejected(appointment)
        }
    }
}