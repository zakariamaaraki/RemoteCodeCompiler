#!/usr/bin/env bash

profiles="";

addProfile () {
  if [ "$profiles" = "" ];
  then
    profiles=$1;
  else
    profiles=$profiles","$1;
  fi
}

if [ ! -z "$ENABLE_KAFKA_MODE" ] && [ "$ENABLE_KAFKA_MODE" = true ];
then
  addProfile "kafka"
fi

if [ ! -z "$ENABLE_RABBITMQ_MODE" ] && [ "$ENABLE_RABBITMQ_MODE" = true ];
then
  addProfile "rabbitmq"
fi

if [ ! -z "$LOGSTASH_LOGGING" ] && [ "$LOGSTASH_LOGGING" = true ];
then
  addProfile "logstash"
fi

if [ ! -z "$ROLLING_FILE_LOGGING" ] && [ "$ROLLING_FILE_LOGGING" = true ];
then
  addProfile "rollingFile"
fi

if [ ! -z "$PULL_IMAGES_BEFORE_STARTUP" ] && [ "$PULL_IMAGES_BEFORE_STARTUP" = true ];
then
  # pull all images before starting the container to make first requests faster
  echo "Pulling all images..."
  docker pull gcc
  docker pull mono
  docker pull golang
  docker pull openjdk:11.0.6-jdk-slim
  docker pull zenika/kotlin
  docker pull python:3
  docker pull rust
  docker pull denvazh/scala
  docker pull ruby
  docker pull haskell
fi

echo "Starting the compiler with the following profiles: "$profiles

java -jar -Dspring.profiles.active=$profiles compiler.jar

