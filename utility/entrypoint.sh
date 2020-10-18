#!/usr/bin/env bash
mv main.java test.java
javac test.java
ret=$?
if [ $ret -ne 0 ]
then
  exit 2
fi
ulimit -s 100
timeout --signal=SIGTERM 1 java test < input.txt
exit $?
