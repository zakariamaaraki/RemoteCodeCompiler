#!/usr/bin/env bash
mv main.java Test1.java
javac Test1.java
ret=$?
if [ $ret -ne 0 ]
then
  exit 2
fi
ulimit -s 500
timeout --signal=SIGTERM 10 java Test1
exit $?
