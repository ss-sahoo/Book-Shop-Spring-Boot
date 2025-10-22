#!/bin/bash

# PostgreSQL Setup Script for Library Management System
# This script will help you set up the database

echo "╔════════════════════════════════════════════════╗"
echo "║  PostgreSQL Setup for Library Management  ║"
echo "╚════════════════════════════════════════════════╝"
echo ""

# Check if PostgreSQL is running
echo "🔍 Checking PostgreSQL status..."
if pg_isready -h localhost -p 5432 > /dev/null 2>&1; then
    echo "✅ PostgreSQL is running"
else
    echo "❌ PostgreSQL is not running"
    echo "📝 Please start PostgreSQL first:"
    echo "   sudo systemctl start postgresql"
    echo "   OR"
    echo "   docker run --name library-postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:15"
    exit 1
fi

echo ""
echo "📝 We need to create the 'library_db' database"
echo ""
echo "Please enter your PostgreSQL password when prompted"
echo "(Default user is usually 'postgres' with password 'postgres')"
echo ""

# Try to create database
echo "🔧 Creating database..."
psql -U postgres -h localhost -c "CREATE DATABASE library_db;" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ Database 'library_db' created successfully!"
else
    echo "ℹ️  Database might already exist or you need to enter password"
    echo ""
    echo "Please run this command manually:"
    echo "  psql -U postgres -h localhost"
    echo ""
    echo "Then in the psql prompt, run:"
    echo "  CREATE DATABASE library_db;"
    echo "  \\q"
fi

echo ""
echo "╔════════════════════════════════════════════════╗"
echo "║              Setup Complete!                   ║"
echo "╚════════════════════════════════════════════════╝"
echo ""
echo "Next steps:"
echo "1. Make sure database is created: psql -U postgres -h localhost -l"
echo "2. Run the application: mvn spring-boot:run"
echo "3. Check the API: curl http://localhost:8080/api/v1/books"
echo ""
echo "📚 Read POSTGRESQL_SETUP.md for detailed instructions"
echo ""

