#!/bin/bash

set -e

REMOTE_IMAGE="ghcr.io/danromfork/gene-browser/gene-browser:latest"
LOCAL_IMAGE="gene-browser:local"

usage() {
    echo "Usage: ./start-local.sh [--local | --remote]"
    echo ""
    echo "  --local   Build from local sources and run"
    echo "  --remote  Pull the latest image from GitHub (default)"
    echo ""
}

MODE="remote"

if [ "$1" = "--local" ]; then
    MODE="local"
elif [ "$1" = "--remote" ] || [ -z "$1" ]; then
    MODE="remote"
elif [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
    usage
    exit 0
else
    echo "Unknown option: $1"
    usage
    exit 1
fi

echo "🚀 Starting Gene Browser local environment (mode: $MODE)..."
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Error: Docker is not running. Please start Docker and try again."
    exit 1
fi

if [ "$MODE" = "local" ]; then
    echo "🔨 Building service image from local sources..."
    sbt "project gene-browser-service" Docker/publishLocal
    # sbt-native-packager tags as gene-browser:0.1.0 and gene-browser:latest
    docker tag gene-browser:latest "$LOCAL_IMAGE"
    export SERVICE_IMAGE="$LOCAL_IMAGE"
    echo "✅ Local image built: $LOCAL_IMAGE"
else
    echo "📦 Pulling latest service image from GitHub Container Registry..."
    docker pull "$REMOTE_IMAGE" || {
        echo "⚠️  Warning: Could not pull image. It might not exist yet or you may need to authenticate."
        echo "   Continuing with local image if available..."
    }
    export SERVICE_IMAGE="$REMOTE_IMAGE"
fi

# Start services
echo ""
echo "🐳 Starting Docker Compose services (Postgres, Kafka, Zookeeper, and Gene Browser service)..."
docker compose up -d

# Wait for services to be healthy
echo ""
echo "⏳ Waiting for services to be ready..."
sleep 5

# Check service status
echo ""
echo "📊 Service status:"
docker compose ps

echo ""
echo "✅ Services started successfully!"
echo ""
echo "📝 Service endpoints:"
echo "   - Gene Browser Service: http://localhost:8080"
echo "   - Postgres: localhost:5432"
echo "   - Kafka: localhost:9092"
echo "   - Zookeeper: localhost:2181"
echo ""
echo "📋 Useful commands:"
echo "   - View logs: docker compose logs -f"
echo "   - Stop services: docker compose down"
echo "   - Stop and remove volumes: docker compose down -v"
echo ""
