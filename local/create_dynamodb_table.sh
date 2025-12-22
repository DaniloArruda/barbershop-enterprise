#!/usr/bin/env bash

set -euo pipefail

AWS_REGION="us-east-1"
DDB_ENDPOINT="http://localhost:8000"
TABLE_NAME="barbershop-enterprise"

echo "Creating DynamoDB table: ${TABLE_NAME} (if not exists)..."

aws dynamodb create-table \
  --endpoint-url "${DDB_ENDPOINT}" \
  --region "${AWS_REGION}" \
  --table-name "${TABLE_NAME}" \
  --attribute-definitions \
    AttributeName=pk,AttributeType=S \
    AttributeName=sk,AttributeType=S \
    AttributeName=barberId,AttributeType=S \
    AttributeName=customerId,AttributeType=S \
  --key-schema \
    AttributeName=pk,KeyType=HASH \
    AttributeName=sk,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST \
  --global-secondary-indexes '[
    {
      "IndexName": "barber-appointments",
      "KeySchema": [
        {"AttributeName": "barberId", "KeyType": "HASH"}
      ],
      "Projection": {
        "ProjectionType": "INCLUDE",
        "NonKeyAttributes": ["customerId", "taskId", "startAt", "endAt"]
      }
    },
    {
      "IndexName": "customer-appointments",
      "KeySchema": [
        {"AttributeName": "customerId", "KeyType": "HASH"}
      ],
      "Projection": {
        "ProjectionType": "ALL"
      }
    }
  ]' \
  >/dev/null 2>&1 || echo "Table ${TABLE_NAME} already exists or creation failed (check logs if needed)."

echo "DynamoDB table bootstrap finished."
