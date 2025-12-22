#!/bin/bash

echo "Waiting for Kafka to be ready..."
sleep 10

echo "Creating Kafka topics..."

docker exec kafka kafka-topics --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 3 \
  --topic appointment.solicited \
  --if-not-exists

echo "Listing all topics:"
docker exec kafka kafka-topics --list --bootstrap-server localhost:9092

echo "Kafka topics created successfully!"
