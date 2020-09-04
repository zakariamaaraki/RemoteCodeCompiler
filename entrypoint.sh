#!/usr/bin/env bash
ls -R
java -Dspring.profiles.active=container -jar compiler.jar 
