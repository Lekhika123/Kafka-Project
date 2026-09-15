# Kafka Course Purchase Monitoring Project

A full-stack event-driven monitoring project that demonstrates how a course purchase event travels from a React frontend to a Spring Boot producer, through Apache Kafka, to a Spring Boot consumer. The application exposes custom metrics using Micrometer, which are collected by Prometheus and visualized in Grafana.

## Project Architecture

```text
React Landing Page
        |
        | Buy Course button
        v
Spring Boot Producer
        |
        | Publishes course purchase event
        v
Apache Kafka
        |
        | Topic: course-events
        v
Spring Boot Consumer
        |
        | Processes event and increments metric
        v
Micrometer + Spring Boot Actuator
        |
        | /actuator/prometheus
        v
Prometheus
        |
        v
Grafana Dashboard
```

## Features

- React landing page with a **Buy Course** button
- Spring Boot Kafka producer
- Apache Kafka event broker
- Spring Boot Kafka consumer
- Custom purchase counter using Micrometer
- Spring Boot Actuator endpoints
- Prometheus metrics collection
- Grafana monitoring dashboard
- Docker Compose setup for Kafka, Prometheus, and Grafana
- Automatic dashboard refresh
- Monitoring of course purchase activity and Kafka consumer partitions

## Technologies Used

### Frontend

- React
- JavaScript
- Vite

### Backend

- Java 21
- Spring Boot 3.5.5
- Spring Kafka
- Gradle

### Messaging

- Apache Kafka 4.0.1
- Kafka topic: `course-events`

### Monitoring

- Spring Boot Actuator
- Micrometer
- Prometheus
- Grafana

### DevOps and Tools

- Docker
- Docker Compose
- Visual Studio Code
- PowerShell

## Project Structure

```text
Kafka Project/
│
├── landing-page/
│   └── React frontend
│
├── Producer-broker-consumer/
│   ├── app/
│   │   └── src/
│   │       └── main/
│   │           ├── java/
│   │           │   └── org/
│   │           │       └── example/
│   │           │           ├── App.java
│   │           │           ├── producer/
│   │           │           └── consumer/
│   │           └── resources/
│   │               └── application.properties
│   └── build.gradle
│
├── prometheus/
│   └── prometheus.yml
│
└── docker-compose.yml
```

> The exact package and folder names may vary depending on the local project structure.

## How the Project Works

1. The user clicks the **Buy Course** button on the React landing page.
2. React sends a request to the Spring Boot backend.
3. The Spring Boot producer creates a course purchase event.
4. The producer publishes the event to the Kafka topic `course-events`.
5. The Kafka consumer listens to the topic.
6. When the consumer receives an event, it prints the course and student details.
7. The consumer increments the custom Micrometer counter:

```text
course_purchase_total
```

8. Spring Boot Actuator exposes the metric through:

```text
http://localhost:8080/actuator/prometheus
```

9. Prometheus collects the metric from the Spring Boot application.
10. Grafana reads the metric from Prometheus and displays it in a dashboard.

## Example Course Purchase Event

```json
{
  "course": "Java Full Stack",
  "student": "Himanshi"
}
```

The actual event fields depend on the `CoursePurchase` class used in the project.

## Custom Metric

The project defines a custom counter named:

```text
course_purchase_total
```

This counter increases whenever the Kafka consumer successfully processes a course purchase event.

Example Prometheus output:

```text
course_purchase_total 2
```

This means that two course purchase events have been processed by the consumer since the application started or since the metric was reset.

## Kafka Configuration

The Spring Boot application connects to Kafka using:

```properties
spring.kafka.bootstrap-servers=localhost:19092
```

The application uses the following topic:

```text
course-events
```

The consumer group is:

```text
course-consumer-group
```

## Spring Boot Configuration

The important monitoring configuration in `application.properties` is:

```properties
management.endpoints.web.exposure.include=health,info,metrics,prometheus
```

The Prometheus endpoint is:

```text
http://localhost:8080/actuator/prometheus
```

## Docker Compose Services

The `docker-compose.yml` file runs these services:

| Service | Purpose | Port |
|---|---|---:|
| Kafka | Event broker | `19092` |
| Prometheus | Metrics collection | `9090` |
| Grafana | Metrics visualization | `3000` |

### Start Docker Services

From the project root, run:

```powershell
docker compose up -d
```

### Check Running Containers

```powershell
docker ps
```

### Stop Docker Services

```powershell
docker compose down
```

## Run the Spring Boot Application

Open PowerShell in the backend project directory:

```powershell
cd "D:\Java Fullstack\Kafka Project\Producer-broker-consumer"
```

Build the project:

```powershell
.\gradlew.bat clean
.\gradlew.bat build
```

Start the application:

```powershell
.\gradlew.bat bootRun
```

The Spring Boot application runs on:

```text
http://localhost:8080
```

> For this project, running the application with `bootRun` is recommended because it builds and runs the current source code.

## Prometheus Configuration

The Prometheus configuration file is located at:

```text
prometheus/prometheus.yml
```

Example configuration:

```yaml
global:
  scrape_interval: 5s

scrape_configs:
  - job_name: 'spring-boot'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['host.docker.internal:8080']
```

Prometheus collects metrics from the Spring Boot application every five seconds.

Open Prometheus at:

```text
http://localhost:9090
```

To check the Spring Boot target:

1. Open Prometheus.
2. Go to **Status → Target health**.
3. Confirm that the Spring Boot target is marked **UP**.

## Grafana Setup

Open Grafana:

```text
http://localhost:3000
```

### Add Prometheus as a Data Source

Use this URL inside Grafana:

```text
http://prometheus:9090
```

> Do not use `http://localhost:9090` as the Grafana data source URL because Grafana is running inside a Docker container.

### Suggested Dashboard Panels

The dashboard can contain the following panels:

| Panel | Metric or Query | Purpose |
|---|---|---|
| Total Course Purchases | `course_purchase_total` | Shows the current total |
| Course Purchases Over Time | `course_purchase_total` | Shows the cumulative trend |
| Purchases in Last 5 Minutes | `increase(course_purchase_total[5m])` | Shows recent purchase activity |
| Kafka Assigned Partitions | `kafka_consumer_coordinator_assigned_partitions` | Shows assigned Kafka partitions |

Set the dashboard refresh interval to:

```text
5s
```

## Testing the Complete Flow

1. Start Kafka, Prometheus, and Grafana using Docker Compose.
2. Start the Spring Boot application.
3. Start the React frontend.
4. Open the React landing page.
5. Click **Buy Course**.
6. Check the Spring Boot console for the consumed event.
7. Open the Actuator Prometheus endpoint.
8. Confirm that `course_purchase_total` has increased.
9. Open Grafana.
10. Confirm that the dashboard panels update.

Expected flow:

```text
Buy Course click
      ↓
Course purchase event created
      ↓
Event published to Kafka
      ↓
Consumer receives the event
      ↓
course_purchase_total increases
      ↓
Prometheus collects the metric
      ↓
Grafana updates the dashboard
```

## Useful URLs

| Component | URL |
|---|---|
| React frontend | `http://localhost:<frontend-port>` |
| Spring Boot application | `http://localhost:8080` |
| Actuator health | `http://localhost:8080/actuator/health` |
| Prometheus metrics | `http://localhost:8080/actuator/prometheus` |
| Prometheus | `http://localhost:9090` |
| Grafana | `http://localhost:3000` |

Replace `<frontend-port>` with the port used by the React development server.

## Troubleshooting

### Kafka connection refused

Check whether the Kafka container is running:

```powershell
docker ps
```

If it is not running, start the Docker services:

```powershell
docker compose up -d
```

Also confirm that Spring Boot uses:

```properties
spring.kafka.bootstrap-servers=localhost:19092
```

### Prometheus target is down

Check:

- Spring Boot is running.
- The Actuator endpoint is accessible.
- The endpoint is available at:

```text
http://localhost:8080/actuator/prometheus
```

- Prometheus uses:

```yaml
targets: ['host.docker.internal:8080']
```

### Grafana cannot connect to Prometheus

Use this URL in the Grafana data source configuration:

```text
http://prometheus:9090
```

### Metric does not increase

Check:

- The React request reaches Spring Boot.
- The producer publishes to `course-events`.
- The consumer listens to `course-events`.
- The Spring Boot console prints the consumed event.
- The consumer calls:

```java
purchaseCounter.increment();
```

## Learning Outcomes

This project demonstrates:

- Event-driven architecture
- Asynchronous communication using Kafka
- Kafka producers and consumers
- Spring Boot integration with Kafka
- Custom application metrics
- Monitoring using Actuator and Micrometer
- Metrics scraping with Prometheus
- Dashboard creation with Grafana
- Docker-based local infrastructure
- End-to-end debugging and testing

## Future Improvements

- Add more course and student details to the event
- Store purchase events in a database
- Add authentication and authorization
- Add Kafka producer and consumer error metrics
- Add alerting in Grafana
- Add dashboards for consumer lag and message throughput
- Add automated tests
- Deploy the application and monitoring stack to a cloud platform
- Add a CI/CD pipeline

## Author

Developed as a learning and portfolio project to understand:

- Java and Spring Boot
- Apache Kafka
- Event-driven systems
- Application monitoring
- Prometheus
- Grafana
