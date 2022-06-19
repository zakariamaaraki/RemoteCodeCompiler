#!/usr/bin/env bash

rename=[(${compiler.rename})]
compile=[(${compiler.compile})]

if [ "$rename" = true ];
then
  mv [(${compiler.defaultName})] [(${compiler.fileName})]
fi
if [ "$compile" = true ];
then
  [(${compiler.compilationCommand})] 1> /dev/null
  ret=$?
  if [ $ret -ne 0 ];
  then
    exit [(${compiler.compilationErrorStatusCode})]
  fi
fi
ulimit -s [(${compiler.memoryLimit})]
timeout -s SIGTERM [(${compiler.timeLimit})] [(${compiler.executionCommand})]
exit $?    
