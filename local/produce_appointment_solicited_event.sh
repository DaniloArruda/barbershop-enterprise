#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
EVENT_FILE="$SCRIPT_DIR/appointment_solicited_event.json"

if [ ! -f "$EVENT_FILE" ]; then
  echo "Error: Event file not found at $EVENT_FILE"
  exit 1
fi

echo "Loading event from: $EVENT_FILE"
echo "Producing event to appointment.solicited topic..."

EVENT_PAYLOAD=$(cat "$EVENT_FILE" | jq -c '.')

echo "$EVENT_PAYLOAD" | docker exec -i kafka kafka-console-producer \
  --topic appointment.solicited \
  --bootstrap-server localhost:9092

echo ""
echo "✓ Event sent successfully!"
echo ""
echo "Event details:"
cat "$EVENT_FILE" | jq '.'
