package com.danilo.barbershop.enterprise.application.usecases.appointment_solicited

import com.danilo.barbershop.enterprise.application.producer.AppointmentProducer
import com.danilo.barbershop.enterprise.application.repository.AppointmentRepository
import com.danilo.barbershop.enterprise.domain.Appointment

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