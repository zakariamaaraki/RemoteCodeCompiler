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

  images=("gcc" "mono" "golang" "openjdk:11.0.6-jdk-slim" "zenika/kotlin" "python:3" "rust" "denvazh/scala" "ruby" "haskell")

  # pull all images before starting the container to make first requests faster
  echo "Pulling all images..."

  for i in "${images[@]}"
  do
    docker pull "$i"
  done

fi

echo "Starting the compiler with the following profiles: "$profiles

java -jar -Dspring.profiles.active=$profiles compiler.jar

