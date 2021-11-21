#!/usr/bin/env bash
mv main.java Watermelon.java
javac Watermelon.java
ret=$?
if [ $ret -ne 0 ]
then
  exit 2
fi
ulimit -s 500
timeout --signal=SIGTERM 3 java Watermelon < Watermelon-1.txt
exit $?
