#!/usr/bin/env bash

if [ ! -z "$ENABLE_KAFKA_MODE" ] && [ "$ENABLE_KAFKA_MODE" = true ];
then
  java -jar -Dspring.profiles.active=kafka compiler.jar
else
  java -jar compiler.jar
fi

