#!/usr/bin/env bash
mv main.java Test5.java
javac Test5.java
ret=$?
if [ $ret -ne 0 ]
then
  exit 2
fi
ulimit -s 1
timeout --signal=SIGTERM 10 java Test5
exit $?
