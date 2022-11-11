#!/usr/bin/env bash

ulimit -s [(${compiler.memoryLimit})]
timeout -t [(${compiler.timeLimit})] -s SIGTERM [(${compiler.executionCommand})]
exit $?    
