#!/bin/bash

set -e

echo "🛑 Stopping Gene Browser local environment..."

docker compose down

echo ""
echo "✅ Services stopped successfully!"
echo ""
echo "💡 To remove volumes as well, run: docker compose down -v"
