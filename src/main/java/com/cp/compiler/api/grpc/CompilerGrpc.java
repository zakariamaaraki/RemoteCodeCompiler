package com.cp.compiler.api.grpc;

import com.cp.compiler.contract.CompilerServiceGrpc;
import com.cp.compiler.contract.RemoteCodeCompilerRequest;
import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.mappers.grpc.CompilerRequestMapper;
import com.cp.compiler.mappers.grpc.CompilerResponseMapper;
import com.cp.compiler.services.api.CompilerFacade;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@Slf4j
@GrpcService
public class CompilerGrpc extends CompilerServiceGrpc.CompilerServiceImplBase {

    private CompilerFacade compiler;
    private CompilerRequestMapper compilerRequestMapper;
    private CompilerResponseMapper compilerResponseMapper;

    /**
     * Instantiates a new Compiler controller.
     *
     * @param compiler               the compiler
     * @param compilerRequestMapper
     * @param compilerResponseMapper
     */
    public CompilerGrpc(CompilerFacade compiler,
                        CompilerRequestMapper compilerRequestMapper,
                        CompilerResponseMapper compilerResponseMapper) {
        this.compiler = compiler;
        this.compilerRequestMapper = compilerRequestMapper;
        this.compilerResponseMapper = compilerResponseMapper;
    }

    @Override
    public void compile(com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest request,
                        io.grpc.stub.StreamObserver<com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse> responseObserver) {

        RemoteCodeCompilerRequest mappedRequest = compilerRequestMapper.toCompilerRequest(request);

        Execution execution = null;
        try {
            execution = ExecutionFactory.createExecution(
                    mappedRequest.getSourcecodeFile(),
                    mappedRequest.getConvertedTestCases(),
                    mappedRequest.getTimeLimit(),
                    mappedRequest.getMemoryLimit(),
                    mappedRequest.getLanguage());

            ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compiler.compile(
                    execution,
                    false,
                    null,
                    null);

            responseObserver.onNext(compilerResponseMapper.ToCompilerResponseProto(responseEntity.getBody()));
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error occurred", e);
            responseObserver.onError(e);
        }
    }
}
