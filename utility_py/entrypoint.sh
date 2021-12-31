#!/usr/bin/env bash
ulimit -s 500
timeout --signal=SIGTERM 3s python3 main.py < makeEven-1.txt
exit $?
