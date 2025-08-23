package com.danilo.barbershop.enterprise.application.usecases.appointment_solicited

import com.danilo.barbershop.enterprise.application.producer.AppointmentProducer
import com.danilo.barbershop.enterprise.application.repository.AppointmentRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class AppointmentSolicitedUseCaseTest {

    @Test
    fun `should execute appointment solicited use case`() {
        // given
        val appointmentRepository = mock<AppointmentRepository>()
        val appointmentProducer = mock<AppointmentProducer>()
        val useCase = AppointmentSolicitedUseCase(
            appointmentRepository = appointmentRepository,
            appointmentProducer = appointmentProducer,
        )
        val request = AppointmentSolicitedRequest()

        // when
        useCase.execute(request)

        // then
        verify(appointmentRepository).save(any())
        verify(appointmentProducer).publishAppointmentCreated(any())
    }
}