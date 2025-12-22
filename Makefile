.PHONY: init-local stop-local clean-local start-worker produce-appointment-solicited-event

init-local:
	@echo "Starting local environment..."
	docker-compose up -d
	@echo "Creating Kafka topics..."
	chmod +x local/create_kafka_topics.sh
	./local/create_kafka_topics.sh
	@echo "Creating DynamoDB tables..."
	chmod +x local/create_dynamodb_table.sh
	./local/create_dynamodb_table.sh
	@echo "Local environment is ready!"

stop-local:
	@echo "Stopping local environment..."
	docker-compose down

clean-local:
	@echo "Cleaning local environment (removing volumes)..."
	docker-compose down -v

start-worker:
	@echo "Starting worker..."
	./gradlew :worker:bootRun

produce-appointment-solicited-event:
	@echo "Producing appointment solicited event to Kafka..."
	chmod +x local/produce_appointment_solicited_event.sh
	./local/produce_appointment_solicited_event.sh
