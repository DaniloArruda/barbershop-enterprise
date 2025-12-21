package usecase.appointment_solicited

import com.danilo.barbershop.enterprise.application.usecase.appointment_solicited.AppointmentSolicitedRequest
import com.danilo.barbershop.enterprise.application.usecase.appointment_solicited.AppointmentSolicitedUseCase
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import com.danilo.barbershop.enterprise.application.producer.AppointmentProducer
import com.danilo.barbershop.enterprise.application.repository.AppointmentRepository
import com.danilo.barbershop.enterprise.application.repository.BarberRepository
import org.mockito.kotlin.never
import org.mockito.kotlin.whenever
import java.time.ZonedDateTime
import java.util.UUID


class AppointmentSolicitedUseCaseTest {

    @Test
    fun `should execute appointment solicited use case`() {
        // given
        val request = AppointmentSolicitedRequest(
            barberId = UUID.randomUUID(),
            customerId = UUID.randomUUID(),
            taskId = UUID.randomUUID(),
            startAt = ZonedDateTime.now(),
            endAt = ZonedDateTime.now().plusHours(1),
        )

        val appointmentRepository = mock<AppointmentRepository>()
        val appointmentProducer = mock<AppointmentProducer>()
        val barberRepository = mock<BarberRepository>()
        val useCase = AppointmentSolicitedUseCase(
            appointmentRepository = appointmentRepository,
            appointmentProducer = appointmentProducer,
            barberRepository = barberRepository,
        )

        whenever(barberRepository.isBarberAvailable(any(), any(), any())).thenReturn(true)

        // when
        useCase.execute(request)

        // then
        verify(appointmentRepository).save(any())
        verify(appointmentProducer).publishAppointmentCreated(any())
    }

    @Test
    fun `should reject appointment solicited when barber is not available`() {
        // given
        val request = AppointmentSolicitedRequest(
            barberId = UUID.randomUUID(),
            customerId = UUID.randomUUID(),
            taskId = UUID.randomUUID(),
            startAt = ZonedDateTime.now(),
            endAt = ZonedDateTime.now().plusHours(1),
        )

        val appointmentRepository = mock<AppointmentRepository>()
        val appointmentProducer = mock<AppointmentProducer>()
        val barberRepository = mock<BarberRepository>()
        val useCase = AppointmentSolicitedUseCase(
            appointmentRepository = appointmentRepository,
            appointmentProducer = appointmentProducer,
            barberRepository = barberRepository,
        )

        whenever(barberRepository.isBarberAvailable(any(), any(), any())).thenReturn(false)

        // when
        useCase.execute(request)

        // then
        verify(appointmentRepository, never()).save(any())
        verify(appointmentProducer, never()).publishAppointmentCreated(any())
        verify(appointmentProducer).publishAppointmentRejected(any())
    }

}