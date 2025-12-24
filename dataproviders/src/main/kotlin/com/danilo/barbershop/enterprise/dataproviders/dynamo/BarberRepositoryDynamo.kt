package com.danilo.barbershop.enterprise.dataproviders.dynamo

import com.danilo.barbershop.enterprise.application.repository.BarberRepository
import com.danilo.barbershop.enterprise.dataproviders.dynamo.model.AppointmentItem
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import java.time.ZonedDateTime
import java.util.UUID

@Component
class BarberRepositoryDynamo(
    private val dynamoDbEnhancedClient: DynamoDbEnhancedClient,
) : BarberRepository {

    private val tableName = "barbershop-enterprise"

    private val table by lazy {
        dynamoDbEnhancedClient.table(
            tableName,
            TableSchema.fromBean(AppointmentItem::class.java)
        )
    }

    override fun isBarberAvailable(
        barberId: UUID,
        startAt: ZonedDateTime,
        endAt: ZonedDateTime
    ): Boolean {
        val index = table.index("barber-appointments")

        val queryRequest = QueryConditional.keyEqualTo(
            Key.builder()
                .partitionValue(barberId.toString())
                .build()
        )

        val existingAppointments = index.query(queryRequest)
            .flatMap { it.items() }
            .map { it.toDomain() }

        return existingAppointments.none { existing ->
            existing.startAt.isBefore(endAt) && existing.endAt.isAfter(startAt)
        }
    }
}