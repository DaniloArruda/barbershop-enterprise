# Kafka Local Setup Guide

## Overview
The worker module now uses Kafka for consuming appointment events instead of REST endpoints.

## Prerequisites
- Docker and Docker Compose installed
- Java 21 (already configured in project)

## Starting Kafka Locally

### Quick Start (Recommended)
Initialize the complete local environment with a single command:
```bash
make init-local
```

This will:
1. Start **Zookeeper** on port `2181`
2. Start **Kafka** on port `9092`
3. Start **Kafka UI** on port `9090` (Web interface: http://localhost:9090)
4. Create the `appointment.solicited` topic with 3 partitions

### Manual Setup (Alternative)

If you prefer to run steps manually:

**1. Start Kafka and Zookeeper**
```bash
docker-compose up -d
```

**2. Create Kafka Topics**
```bash
chmod +x local/create_kafka_topics.sh
./local/create_kafka_topics.sh
```

**3. Verify Kafka is Running**
```bash
docker ps
```

You should see three containers: `zookeeper`, `kafka`, and `kafka-ui`

**4. View Kafka UI**
Open your browser and navigate to: http://localhost:9090

## Running the Worker Application

### Start the Worker
```bash
./gradlew :worker:bootRun
```

The worker will:
- Connect to Kafka on `localhost:9092`
- Listen to the `appointment.solicited` topic
- Process messages using the `AppointmentSolicitedUseCase`

## Testing the Kafka Consumer

### Option 1: Using Make Command (Recommended)
Send a test event with a single command:
```bash
make produce-appointment-solicited-event
```

This will load the event payload from `local/appointment_solicited_event.json` and produce it to the `appointment.solicited` topic.

**Customizing the Event:**
Edit `local/appointment_solicited_event.json` to change the event data:
```bash
# Edit the JSON file with your preferred editor
vim local/appointment_solicited_event.json
```

The default event structure:
```json
{
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "barberId": "223e4567-e89b-12d3-a456-426614174000",
  "taskId": "323e4567-e89b-12d3-a456-426614174000",
  "startAt": "2024-12-22T14:00:00-03:00",
  "endAt": "2024-12-22T15:00:00-03:00"
}
```

### Option 2: Using Kafka UI
1. Go to http://localhost:9090
2. Navigate to "Topics"
3. Create topic `appointment.solicited` (if not auto-created)
4. Click "Produce Message"
5. Send a test message with this JSON:

```json
{
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "barberId": "223e4567-e89b-12d3-a456-426614174000",
  "taskId": "323e4567-e89b-12d3-a456-426614174000",
  "startAt": "2024-12-22T14:00:00-03:00",
  "endAt": "2024-12-22T15:00:00-03:00"
}
```

### Option 3: Using Kafka Console Producer
```bash
docker exec -it kafka kafka-console-producer \
  --topic appointment.solicited \
  --bootstrap-server localhost:9092
```

Then paste the JSON message above and press Enter.

### Option 4: Using kafka-console-producer with value
```bash
docker exec -it kafka bash -c "echo '{\"customerId\":\"123e4567-e89b-12d3-a456-426614174000\",\"barberId\":\"223e4567-e89b-12d3-a456-426614174000\",\"taskId\":\"323e4567-e89b-12d3-a456-426614174000\",\"startAt\":\"2024-12-21T14:00:00-03:00\",\"endAt\":\"2024-12-21T15:00:00-03:00\"}' | kafka-console-producer --topic appointment.solicited --bootstrap-server localhost:9092"
```

## Managing Local Environment

### Available Makefile Commands

**Initialize local environment** (starts everything + creates topics):
```bash
make init-local
```

**Start worker application**:
```bash
make start-worker
```

**Produce test event** (sends sample appointment event):
```bash
make produce-appointment-solicited-event
```

**Stop local environment**:
```bash
make stop-local
```

**Clean local environment** (removes volumes):
```bash
make clean-local
```

### Manual Docker Commands

**Stop containers**:
```bash
docker-compose down
```

**Stop and remove volumes**:
```bash
docker-compose down -v
```

## Configuration

### Kafka Consumer Properties
Located in: `worker/src/main/resources/application.yml`

Key configurations:
- **Bootstrap Servers**: `localhost:9092`
- **Consumer Group**: `barbershop-worker-group`
- **Topic**: `appointment.solicited`
- **Auto Offset Reset**: `earliest` (processes all messages from the beginning)

### Modifying Kafka Settings
Edit `application.yml` to change:
- Bootstrap servers
- Consumer group ID
- Deserialization settings
- Auto-offset reset behavior

## Troubleshooting

### Connection Refused
- Ensure Kafka is running: `docker ps`
- Check logs: `docker logs kafka`

### Messages Not Being Consumed
- Verify topic exists: http://localhost:9090
- Check worker logs for errors
- Ensure consumer group is properly configured

### Reset Consumer Offset
```bash
docker exec -it kafka kafka-consumer-groups \
  --bootstrap-server localhost:9092 \
  --group barbershop-worker-group \
  --reset-offsets --to-earliest \
  --topic appointment.solicited \
  --execute
```

## Architecture Changes

### Before (REST)
```
HTTP POST /appointment/solicit → AppointmentSolicitedListener → UseCase
```

### After (Kafka)
```
Kafka Topic (appointment.solicited) → AppointmentSolicitedListener → UseCase
```

## Related Files
- **Listener**: `worker/src/main/kotlin/com/danilo/barbershop/enterprise/worker/appointment_solicited/AppointmentSolicitedListener.kt`
- **Config**: `worker/src/main/kotlin/com/danilo/barbershop/enterprise/worker/config/KafkaConsumerConfig.kt`
- **Event**: `worker/src/main/kotlin/com/danilo/barbershop/enterprise/worker/appointment_solicited/AppointmentSolicitedEvent.kt`
- **Properties**: `worker/src/main/resources/application.yml`
- **Docker Compose**: `docker-compose.yml`
