## Guidelines

- For all changes in the local kafka configurations, update the KAFKA_SETUP.md file
- Class representing models from the database should be in the dataproviders module
  - If database is dynamodb:
    - The class should be in the dynamo package
    - The class should have a name with suffix "Item"
      - For example, for the Appointment entity, the class should be named AppointmentItem