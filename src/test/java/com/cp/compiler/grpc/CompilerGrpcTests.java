package com.cp.compiler.grpc;


import com.cp.compiler.api.grpc.CompilerGrpc;
import com.cp.compiler.contract.CompilerProto;
import com.cp.compiler.contract.Language;
import com.cp.compiler.contract.RemoteCodeCompilerRequest;
import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.executions.languages.JavaExecution;
import com.cp.compiler.mappers.grpc.CompilerRequestMapper;
import com.cp.compiler.mappers.grpc.CompilerResponseMapper;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.services.api.CompilerFacade;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompilerGrpcTests {

    @Mock
    private CompilerFacade compilerFacade;

    @Mock
    private CompilerRequestMapper requestMapper;

    @Mock
    private CompilerResponseMapper responseMapper;

    @Mock
    private StreamObserver<CompilerProto.RemoteCodeCompilerResponse> responseObserver;

    @InjectMocks
    private CompilerGrpc compilerGrpc;

    @Test
    void testCompile() throws IOException {
        // Mocking request
        CompilerProto.RemoteCodeCompilerRequest request = CompilerProto.RemoteCodeCompilerRequest.newBuilder().build();

        // Mocking mapped request
        RemoteCodeCompilerRequest mappedRequest = new RemoteCodeCompilerRequest(
                "sourceCode",
                Language.JAVA,
                15,
                500,
                new LinkedHashMap<>());
        when(requestMapper.toCompilerRequest(any())).thenReturn(mappedRequest);

        // Register Java execution to the factory
        ExecutionFactory.registerExecution(
                Language.JAVA,
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new JavaExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));

        // Mocking response entity
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = mock(ResponseEntity.class);
        when(compilerFacade.compile(any(), anyBoolean(), any(), any())).thenReturn(responseEntity);

        // Mocking response body
        RemoteCodeCompilerResponse responseBody = new RemoteCodeCompilerResponse();
        when(responseEntity.getBody()).thenReturn(responseBody);

        // Mocking response mapper
        com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse grpcResponse =
                com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse.newBuilder().build();
        when(responseMapper.ToCompilerResponseProto(any())).thenReturn(grpcResponse);

        // Calling the method under test
        compilerGrpc.compile(request, responseObserver);

        // Verifying interactions
        verify(responseObserver).onNext(grpcResponse);
        verify(responseObserver).onCompleted();
    }

    /*
    * This test case ensures that when an IOException is thrown during the compilation process,
    * the onError() method of the StreamObserver is called.
    * */
    @Test
    void testCompileIOException() throws IOException {
        // Mocking request
        CompilerProto.RemoteCodeCompilerRequest request = CompilerProto.RemoteCodeCompilerRequest.newBuilder().build();

        // Mocking mapped request
        RemoteCodeCompilerRequest mappedRequest = new RemoteCodeCompilerRequest(
                "sourceCode",
                Language.JAVA,
                15,
                500,
                new LinkedHashMap<>());
        when(requestMapper.toCompilerRequest(any())).thenReturn(mappedRequest);

        // Register Java execution to the factory
        ExecutionFactory.registerExecution(
                Language.JAVA,
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new JavaExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));

        // Mocking IOException when compilerFacade.compile() is called
        when(compilerFacade.compile(any(), anyBoolean(), any(), any())).thenThrow(IOException.class);

        // Calling the method under test
        compilerGrpc.compile(request, responseObserver);

        // Verifying interactions
        verify(responseObserver, never()).onNext(any());
        verify(responseObserver, times(1)).onError(any());
        verify(responseObserver, never()).onCompleted();
    }
}
