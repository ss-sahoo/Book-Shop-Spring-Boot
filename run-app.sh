#!/bin/bash

echo "╔════════════════════════════════════════════════════════════╗"
echo "║   Library Management System - Starting Application        ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Navigate to project directory
cd "/home/diracai/Music/java Spring boot"

# Check if PostgreSQL is running
echo "🔍 Checking PostgreSQL..."
if pg_isready -h localhost -p 5432 > /dev/null 2>&1; then
    echo "✅ PostgreSQL is running"
else
    echo "❌ PostgreSQL is NOT running!"
    echo "Please start PostgreSQL first"
    exit 1
fi

# Check if database exists
echo "🔍 Checking database..."
PGPASSWORD=postgres psql -U postgres -h localhost -lqt | cut -d \| -f 1 | grep -qw library_db
if [ $? -eq 0 ]; then
    echo "✅ Database 'library_db' exists"
else
    echo "⚠️  Creating database 'library_db'..."
    PGPASSWORD=postgres psql -U postgres -h localhost -c "CREATE DATABASE library_db;" > /dev/null 2>&1
    echo "✅ Database created"
fi

# Stop any existing instance
echo "🛑 Stopping any existing instances..."
lsof -ti:8080 | xargs kill -9 2>/dev/null
pkill -f "spring-boot:run" 2>/dev/null
sleep 2

echo ""
echo "🚀 Starting Spring Boot application..."
echo "📝 Logs will be saved to: spring-boot.log"
echo ""

# Start the application
mvn spring-boot:run > spring-boot.log 2>&1 &
APP_PID=$!

echo "⏳ Waiting for application to start (this may take 20-30 seconds)..."

# Wait for application to be ready
for i in {1..30}; do
    if curl -s http://localhost:8080/api/v1/actuator/health > /dev/null 2>&1; then
        echo ""
        echo "╔════════════════════════════════════════════════════════════╗"
        echo "║                                                            ║"
        echo "║     ✅ APPLICATION IS RUNNING SUCCESSFULLY! ✅             ║"
        echo "║                                                            ║"
        echo "║  🌐 Base URL: http://localhost:8080/api/v1                ║"
        echo "║  📊 Health: http://localhost:8080/api/v1/actuator/health  ║"
        echo "║  📚 Books API: http://localhost:8080/api/v1/books         ║"
        echo "║                                                            ║"
        echo "║  🎯 You can now test in Postman!                          ║"
        echo "║                                                            ║"
        echo "║  📖 See POSTMAN_COLLECTION.md for request examples        ║"
        echo "║                                                            ║"
        echo "╚════════════════════════════════════════════════════════════╝"
        echo ""
        echo "Process ID: $APP_PID"
        echo "To stop: kill $APP_PID  OR  ./stop-app.sh"
        echo ""
        echo "📝 View logs: tail -f spring-boot.log"
        echo ""
        exit 0
    fi
    echo -n "."
    sleep 2
done

echo ""
echo "❌ Application failed to start within 60 seconds"
echo "📝 Check logs: cat spring-boot.log"
echo "🔍 Common issues:"
echo "   - Database not accessible"
echo "   - Port 8080 already in use"
echo "   - Configuration errors"
exit 1

