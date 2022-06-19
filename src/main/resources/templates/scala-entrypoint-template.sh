#!/usr/bin/env bash

mv [(${compiler.defaultName})] [(${compiler.fileName})]
[(${compiler.compilationCommand})] 1> /dev/null
ret=$?
if [ $ret -ne 0 ];
then
  exit [(${compiler.compilationErrorStatusCode})]
fi
ulimit -s [(${compiler.memoryLimit})]
timeout -t [(${compiler.timeLimit})] -s SIGTERM [(${compiler.executionCommand})]
exit $?    
