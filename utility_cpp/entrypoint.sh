#!/usr/bin/env bash
g++ main.cpp -o exec
ret=$?
if [ $ret -ne 0 ]
then
  exit 2
fi
ulimit -s 500
timeout --signal=SIGTERM 3 ./exec  < physEdOnline-2.txt
exit $?
