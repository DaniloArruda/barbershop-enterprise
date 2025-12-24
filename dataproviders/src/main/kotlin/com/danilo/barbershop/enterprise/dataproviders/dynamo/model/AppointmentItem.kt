package com.danilo.barbershop.enterprise.dataproviders.dynamo.model

import com.danilo.com.danilo.barbershop.enterprise.domain.Appointment
import com.danilo.com.danilo.barbershop.enterprise.domain.AppointmentStatus
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

/**
 * DynamoDB representation of an Appointment.
 *
 * Bean-style class required by DynamoDbEnhancedClient.
 */
@DynamoDbBean
class AppointmentItem() {

    private var pk: String = ""
    private var sk: String = ""
    private var barberId: String = ""
    private var customerId: String = ""
    private var taskId: String = ""
    private var startAt: String = ""
    private var endAt: String = ""
    private var status: String = ""

    @DynamoDbPartitionKey
    fun getPk(): String = pk
    fun setPk(value: String) {
        pk = value
    }

    @DynamoDbSortKey
    fun getSk(): String = sk
    fun setSk(value: String) {
        sk = value
    }

    @DynamoDbSecondaryPartitionKey(indexNames = ["barber-appointments"])
    fun getBarberId(): String = barberId
    fun setBarberId(value: String) {
        barberId = value
    }

    @DynamoDbSecondaryPartitionKey(indexNames = ["customer-appointments"])
    fun getCustomerId(): String = customerId
    fun setCustomerId(value: String) {
        customerId = value
    }

    fun getTaskId(): String = taskId
    fun setTaskId(value: String) {
        taskId = value
    }

    fun getStartAt(): String = startAt
    fun setStartAt(value: String) {
        startAt = value
    }

    fun getEndAt(): String = endAt
    fun setEndAt(value: String) {
        endAt = value
    }

    fun getStatus(): String = status
    fun setStatus(value: String) {
        status = value
    }

    fun toDomain(): Appointment = Appointment(
        id = extractAppointmentIdFromPk(pk),
        barberId = UUID.fromString(barberId),
        customerId = UUID.fromString(customerId),
        taskId = UUID.fromString(taskId),
        startAt = ZonedDateTime.parse(startAt, dateTimeFormatter),
        endAt = ZonedDateTime.parse(endAt, dateTimeFormatter),
        status = AppointmentStatus.valueOf(status),
    )

    companion object {
        private const val APPOINTMENT_PREFIX = "APPOINTMENT#"

        fun fromDomain(appointment: Appointment): AppointmentItem =
            AppointmentItem().apply {
                pk = "$APPOINTMENT_PREFIX${appointment.id}"
                sk = "APPOINTMENT"
                barberId = appointment.barberId.toString()
                customerId = appointment.customerId.toString()
                taskId = appointment.taskId.toString()
                startAt = appointment.startAt.format(dateTimeFormatter)
                endAt = appointment.endAt.format(dateTimeFormatter)
                status = appointment.status.name
            }

        private fun extractAppointmentIdFromPk(pk: String): UUID {
            val idPart = if (pk.startsWith(APPOINTMENT_PREFIX)) {
                pk.removePrefix(APPOINTMENT_PREFIX)
            } else {
                pk
            }
            return UUID.fromString(idPart)
        }
    }
}