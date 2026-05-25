#!/bin/bash
# Run this script once to generate Gradle wrapper JARs for all projects.
# Requires: gradle installed (brew install gradle / sdk install gradle 8.7)

echo "Generating Gradle wrappers..."

PROJECTS=(
  "task1-photocatalog"
  "task2-nobelprizes"
  "task3-auth"
  "task4-nobelserver"
  "task5-nobelserver-db"
  "task6-nobelclient-own"
  "task7-ble"
)

for proj in "${PROJECTS[@]}"; do
  echo "→ $proj"
  (cd "$proj" && gradle wrapper --gradle-version 8.7 --distribution-type bin)
done

echo "✅ Done! Now open any project folder in Android Studio."
