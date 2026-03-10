#!/bin/bash

set -e

echo "🛑 Stopping Gene Browser on Kubernetes..."

# Delete deployments and services
kubectl delete -f k8s/gene-browser-service.yaml --ignore-not-found=true
kubectl delete -f k8s/kafka.yaml --ignore-not-found=true
kubectl delete -f k8s/zookeeper.yaml --ignore-not-found=true
kubectl delete -f k8s/postgres.yaml --ignore-not-found=true

# Optionally delete namespace (this will delete everything)
read -p "Do you want to delete the namespace and all resources? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    kubectl delete namespace gene-browser --ignore-not-found=true
    echo "✅ Namespace deleted."
else
    echo "✅ Resources deleted. Namespace 'gene-browser' kept."
fi

echo ""
echo "✅ Cleanup completed!"
