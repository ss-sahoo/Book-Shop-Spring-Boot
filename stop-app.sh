#!/bin/bash

echo "🛑 Stopping Library Management System..."

# Stop Spring Boot process
pkill -f "spring-boot:run"

# Stop process on port 8080
lsof -ti:8080 | xargs kill -9 2>/dev/null

sleep 2

echo "✅ Application stopped"
echo ""

