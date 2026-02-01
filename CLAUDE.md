# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A multi-module Kotlin Spring Boot application for a barbershop enterprise system built on event-driven architecture. The system uses **Kafka** for event processing and **DynamoDB** for persistence.

**Technology Stack:**
- Kotlin 1.9.25
- Spring Boot 3.5.5
- Java 21
- Gradle (Kotlin DSL)
- Kafka 7.5.0 (event streaming)
- DynamoDB Local (local persistence)

## Module Architecture

The project uses a layered architecture across four modules:

### 1. **domain** (`domain/`)
- Core business entities (Appointment, Barber)
- No Spring dependencies
- Pure Kotlin classes

### 2. **application** (`application/`)
- Use cases and business orchestration
- Application services with Spring DI
- Repository and producer interfaces (abstraction)
- Examples: `AppointmentSolicitedUseCase`

### 3. **dataproviders** (`dataproviders/`)
- External system implementations (Kafka, DynamoDB)
- Kafka producers: `AppointmentProducerKafka`
- DynamoDB adapters: `AppointmentRepositoryDynamo`
- DynamoDB models with `*Item` suffix (e.g., `AppointmentItem`)

### 4. **worker** (`worker/`)
- Spring Boot application entry point
- Kafka consumers and event listeners
- `AppointmentSolicitedListener` consumes `appointment.solicited` topic
- Application configuration and startup

## Development Commands

### Build & Compile
```bash
# Build entire project
./gradlew build

# Build specific module
./gradlew :module_name:build

# Build without tests
./gradlew build -x test
```

### Testing
```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :module_name:test

# Run single test
./gradlew :module_name:test --tests ClassName.methodName
```

### Local Development
```bash
# Initialize local environment (Kafka + DynamoDB + topics + tables)
make init-local

# Start worker application (connects to local Kafka)
make start-worker

# Send test event to Kafka
make produce-appointment-solicited-event

# Stop local environment
make stop-local

# Clean up volumes (destructive)
make clean-local
```

### View Local Services
- **Kafka UI**: http://localhost:9090
- **DynamoDB Local**: http://localhost:8000
- **Kafka Bootstrap**: localhost:9092
- **Zookeeper**: localhost:2181

## Architecture Patterns

### Event-Driven Flow
```
Kafka Topic (appointment.solicited)
    ↓
AppointmentSolicitedListener (worker module)
    ↓
AppointmentSolicitedUseCase (application module)
    ↓
AppointmentRepository.save() (dataproviders → DynamoDB)
```

### Use Case Pattern
Each use case:
1. Receives a request DTO
2. Creates domain entity
3. Persists via repository interface
4. Publishes event via producer interface

Example: `AppointmentSolicitedUseCase`

### Dependency Abstraction
- `AppointmentRepository` - interface in application, implemented in dataproviders
- `AppointmentProducer` - interface in application, implemented in dataproviders
- Enables loose coupling and easier testing

## Database Design (DynamoDB)

**Table**: `barbershop-enterprise`
- **Primary Key**: `pk` (partition), `sk` (sort)
- **Global Secondary Indexes**:
  - `barber-appointments`: partition key = `barberId`
  - `customer-appointments`: partition key = `customerId`

**Data Models** (in dataproviders):
- `AppointmentItem` - DynamoDB representation of appointments
- Avoid scan operations for cost efficiency

## Configuration

### Kafka Consumer (`worker/src/main/resources/application.yml`)
- **Topic**: `appointment.solicited`
- **Consumer Group**: `barbershop-worker-group`
- **Bootstrap Servers**: `localhost:9092`
- **Auto Offset Reset**: `earliest`

### Local Infrastructure (`docker-compose.yml`)
- Zookeeper: port 2181
- Kafka: port 9092 (localhost), 29092 (internal)
- Kafka UI: port 9090
- DynamoDB Local: port 8000

## Code Guidelines

From `agents.md`:
- Update configuration docs when changing Kafka settings
- DynamoDB models belong in dataproviders module, `dynamo` package
- Use `*Item` suffix for DynamoDB model classes (e.g., `AppointmentItem`)
- Avoid DynamoDB scan operations (use queries instead for cost)

## Testing Kafka Locally

### Option 1: Make Command (Recommended)
```bash
make produce-appointment-solicited-event
```
Uses event data from `local/appointment_solicited_event.json`

### Option 2: Kafka UI
Navigate to http://localhost:9090, select topic, produce message with JSON

### Option 3: Kafka Console
```bash
docker exec -it kafka kafka-console-producer \
  --topic appointment.solicited \
  --bootstrap-server localhost:9092
```

## Troubleshooting

**Kafka not starting**: Check `docker logs kafka` and ensure port 9092 is available

**Messages not consumed**: Verify topic exists in Kafka UI, check worker logs

**Reset consumer offset**:
```bash
docker exec -it kafka kafka-consumer-groups \
  --bootstrap-server localhost:9092 \
  --group barbershop-worker-group \
  --reset-offsets --to-earliest \
  --topic appointment.solicited \
  --execute
```

## Local Files Reference

- Setup guide: `LOCAL_SETUP.md`
- Development guidelines: `agents.md`
- DynamoDB schema: `dynamo-data-model.json`
- Test event sample: `local/appointment_solicited_event.json`
- Kafka setup scripts: `local/create_kafka_topics.sh`, `local/create_dynamodb_table.sh`
