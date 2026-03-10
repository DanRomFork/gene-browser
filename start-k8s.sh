#!/bin/bash

set -e

echo "🚀 Starting Gene Browser on Kubernetes..."
echo ""

# Check if kubectl is available
if ! command -v kubectl &> /dev/null; then
    echo "❌ Error: kubectl is not installed. Please install kubectl first."
    exit 1
fi

# Check if we can connect to a cluster
if ! kubectl cluster-info &> /dev/null; then
    echo "❌ Error: Cannot connect to Kubernetes cluster. Please check your kubeconfig."
    exit 1
fi

# Apply manifests in order
echo "📦 Creating namespace..."
kubectl apply -f k8s/namespace.yaml

echo ""
echo "🐘 Deploying Postgres..."
kubectl apply -f k8s/postgres.yaml

echo ""
echo "🦘 Deploying Zookeeper..."
kubectl apply -f k8s/zookeeper.yaml

echo ""
echo "📨 Deploying Kafka..."
kubectl apply -f k8s/kafka.yaml

echo ""
echo "🧬 Deploying Gene Browser Service..."
kubectl apply -f k8s/gene-browser-service.yaml

echo ""
echo "⏳ Waiting for deployments to be ready..."
kubectl wait --for=condition=available --timeout=300s deployment/postgres -n gene-browser || true
kubectl wait --for=condition=available --timeout=300s deployment/zookeeper -n gene-browser || true
kubectl wait --for=condition=available --timeout=300s deployment/kafka -n gene-browser || true
kubectl wait --for=condition=available --timeout=300s deployment/gene-browser-service -n gene-browser || true

echo ""
echo "✅ Deployment completed!"
echo ""
echo "📊 Pod status:"
kubectl get pods -n gene-browser

echo ""
echo "📝 Service endpoints:"
kubectl get svc -n gene-browser

echo ""
echo "💡 Useful commands:"
echo "   - View logs: kubectl logs -f deployment/gene-browser-service -n gene-browser"
echo "   - Port forward: kubectl port-forward svc/gene-browser-service 8080:8080 -n gene-browser"
echo "   - Stop services: ./stop-k8s.sh"
echo ""
