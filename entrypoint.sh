#!/usr/bin/env bash

profiles="";

if [ ! -z "$ENABLE_KAFKA_MODE" ] && [ "$ENABLE_KAFKA_MODE" = true ];
then
  profiles="kafka";
fi

if [ ! -z "$ENABLE_RABBITMQ_MODE" ] && [ "$ENABLE_RABBITMQ_MODE" = true ];
then
  if [ $profiles = "" ];
  then
    profiles="rabbitmq";
  else
    profiles=$profiles",rabbitmq";
  fi
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

java -jar -Dspring.profiles.active=$profiles compiler.jar

