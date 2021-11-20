#!/usr/bin/env bash
ulimit -s 1
timeout --signal=SIGTERM 10s python3 main.py
exit $?
