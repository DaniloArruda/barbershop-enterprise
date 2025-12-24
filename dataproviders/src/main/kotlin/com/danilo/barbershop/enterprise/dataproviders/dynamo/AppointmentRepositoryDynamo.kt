package com.danilo.barbershop.enterprise.dataproviders.dynamo

import com.danilo.com.danilo.barbershop.enterprise.domain.Appointment
import com.danilo.barbershop.enterprise.application.repository.AppointmentRepository
import com.danilo.barbershop.enterprise.dataproviders.dynamo.model.AppointmentItem
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Component
class AppointmentRepositoryDynamo(
    private val dynamoDbEnhancedClient: DynamoDbEnhancedClient,
) : AppointmentRepository {

    private val tableName = "barbershop-enterprise"

    private val table by lazy {
        dynamoDbEnhancedClient.table(
            tableName,
            TableSchema.fromBean(AppointmentItem::class.java)
        )
    }

    override fun save(appointment: Appointment) {
        val item = AppointmentItem.fromDomain(appointment)
        table.putItem(item)
    }
}