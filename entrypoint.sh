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

java -jar -Dspring.profiles.active=$profiles compiler.jar

