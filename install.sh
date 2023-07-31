#!/bin/bash
mvn clean install -Dmaven.test.skip=true -pl '!Client'
docker-compose up -d