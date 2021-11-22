#!/usr/bin/env bash
mv main.java hello.txt
javac hello.txt
ret=$?
if [ $ret -ne 0 ]
then
  exit 2
fi
ulimit -s 100
timeout --signal=SIGTERM 10 java hell
exit $?
