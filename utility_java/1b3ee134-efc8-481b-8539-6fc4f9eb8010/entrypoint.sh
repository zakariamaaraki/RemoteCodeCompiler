#!/usr/bin/env bash
mv main.java test.txt
javac test.txt
ret=$?
if [ $ret -ne 0 ]
then
  exit 2
fi
ulimit -s 500
timeout --signal=SIGTERM 10 java tes < test.txt
exit $?
