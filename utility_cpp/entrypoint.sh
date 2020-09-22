#!/usr/bin/env bash
g++ main.cpp -o exec
ret=$?
if [ $ret -ne 0 ]
then
  exit 2
fi
ulimit -s 100
timeout --signal=SIGTERM 10 ./exec  < input.txt
exit $?
