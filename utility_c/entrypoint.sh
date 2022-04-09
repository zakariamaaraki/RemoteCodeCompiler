#!/usr/bin/env bash
gcc main.c -o exec
ret=$?
if [ $ret -ne 0 ]
then
  exit 2
fi
ulimit -s 2000
timeout --signal=SIGTERM 3 ./exec  < amShZWinsABet-3.txt
exit $?
