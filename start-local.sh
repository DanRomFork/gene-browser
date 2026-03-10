#!/bin/bash

set -e

echo "🚀 Starting Gene Browser local environment..."
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Error: Docker is not running. Please start Docker and try again."
    exit 1
fi

# Pull the latest service image
echo "📦 Pulling latest service image from GitHub Container Registry..."
docker pull ghcr.io/danromfork/gene-browser/gene-browser:latest || {
    echo "⚠️  Warning: Could not pull image. It might not exist yet or you may need to authenticate."
    echo "   Continuing with local image if available..."
}

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
