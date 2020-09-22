#!/usr/bin/env bash
ulimit -s 100
timeout --signal=SIGTERM 10 python3 main.py < input.txt
exit $?
