# Gene Browser

## Local Development

### Prerequisites

- Docker and Docker Compose installed
- (Optional) Kubernetes cluster and kubectl for K8s deployment

### Running with Docker Compose

The easiest way to run the project locally is using Docker Compose:

```bash
./start-local.sh
```

This will:
- Pull the latest service image from `ghcr.io/danromfork/gene-browser/gene-browser:latest`
- Start Postgres, Zookeeper, Kafka, and the Gene Browser service
- Configure all services to work together

To stop the services:

```bash
./stop-local.sh
```

**Service Endpoints:**
- Gene Browser Service: http://localhost:8080
- Postgres: localhost:5432
- Kafka: localhost:9092
- Zookeeper: localhost:2181

**Useful Commands:**
- View logs: `docker compose logs -f`
- View specific service logs: `docker compose logs -f gene-browser-service`
- Stop services: `docker compose down`
- Stop and remove volumes: `docker compose down -v`

### Running with Kubernetes

To deploy to a Kubernetes cluster:

```bash
./start-k8s.sh
```

This will create all necessary resources in the `gene-browser` namespace.

To stop and clean up:

```bash
./stop-k8s.sh
```

**Accessing Services:**
- Port forward the service: `kubectl port-forward svc/gene-browser-service 8080:8080 -n gene-browser`
- View logs: `kubectl logs -f deployment/gene-browser-service -n gene-browser`

### Configuration

The service reads database configuration from environment variables:
- `DATABASE_HOST` (default: localhost)
- `DATABASE_PORT` (default: 5432)
- `DATABASE_NAME` (default: gene_browser)
- `DATABASE_USER` (default: postgres)
- `DATABASE_PASSWORD` (default: postgres)

These are automatically set in both Docker Compose and Kubernetes deployments.
