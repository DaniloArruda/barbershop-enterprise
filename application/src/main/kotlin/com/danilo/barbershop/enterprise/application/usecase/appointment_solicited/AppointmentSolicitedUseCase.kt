package com.danilo.barbershop.enterprise.application.usecase.appointment_solicited

import com.danilo.com.danilo.barbershop.enterprise.domain.Appointment
import org.springframework.stereotype.Service
import com.danilo.barbershop.enterprise.application.producer.AppointmentProducer
import com.danilo.barbershop.enterprise.application.repository.AppointmentRepository

@Service
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