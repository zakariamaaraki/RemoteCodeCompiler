#!/usr/bin/env bash

ulimit -s [(${compiler.memoryLimit})]
timeout -s SIGTERM [(${compiler.timeLimit})] [(${compiler.executionCommand})]
exit $?    
