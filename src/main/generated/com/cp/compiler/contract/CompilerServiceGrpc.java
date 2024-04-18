package com.cp.compiler.contract;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.60.1)",
    comments = "Source: compiler.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class CompilerServiceGrpc {

  private CompilerServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "contract.CompilerService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest,
      com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse> getCompileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Compile",
      requestType = com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest.class,
      responseType = com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest,
      com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse> getCompileMethod() {
    io.grpc.MethodDescriptor<com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest, com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse> getCompileMethod;
    if ((getCompileMethod = CompilerServiceGrpc.getCompileMethod) == null) {
      synchronized (CompilerServiceGrpc.class) {
        if ((getCompileMethod = CompilerServiceGrpc.getCompileMethod) == null) {
          CompilerServiceGrpc.getCompileMethod = getCompileMethod =
              io.grpc.MethodDescriptor.<com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest, com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Compile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CompilerServiceMethodDescriptorSupplier("Compile"))
              .build();
        }
      }
    }
    return getCompileMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CompilerServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CompilerServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CompilerServiceStub>() {
        @java.lang.Override
        public CompilerServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CompilerServiceStub(channel, callOptions);
        }
      };
    return CompilerServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CompilerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CompilerServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CompilerServiceBlockingStub>() {
        @java.lang.Override
        public CompilerServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CompilerServiceBlockingStub(channel, callOptions);
        }
      };
    return CompilerServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CompilerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CompilerServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CompilerServiceFutureStub>() {
        @java.lang.Override
        public CompilerServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CompilerServiceFutureStub(channel, callOptions);
        }
      };
    return CompilerServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void compile(com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest request,
        io.grpc.stub.StreamObserver<com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCompileMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service CompilerService.
   */
  public static abstract class CompilerServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return CompilerServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service CompilerService.
   */
  public static final class CompilerServiceStub
      extends io.grpc.stub.AbstractAsyncStub<CompilerServiceStub> {
    private CompilerServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CompilerServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CompilerServiceStub(channel, callOptions);
    }

    /**
     */
    public void compile(com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest request,
        io.grpc.stub.StreamObserver<com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCompileMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service CompilerService.
   */
  public static final class CompilerServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<CompilerServiceBlockingStub> {
    private CompilerServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CompilerServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CompilerServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse compile(com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCompileMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service CompilerService.
   */
  public static final class CompilerServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<CompilerServiceFutureStub> {
    private CompilerServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CompilerServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CompilerServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse> compile(
        com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCompileMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_COMPILE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_COMPILE:
          serviceImpl.compile((com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest) request,
              (io.grpc.stub.StreamObserver<com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getCompileMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerRequest,
              com.cp.compiler.contract.CompilerProto.RemoteCodeCompilerResponse>(
                service, METHODID_COMPILE)))
        .build();
  }

  private static abstract class CompilerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CompilerServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.cp.compiler.contract.CompilerProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CompilerService");
    }
  }

  private static final class CompilerServiceFileDescriptorSupplier
      extends CompilerServiceBaseDescriptorSupplier {
    CompilerServiceFileDescriptorSupplier() {}
  }

  private static final class CompilerServiceMethodDescriptorSupplier
      extends CompilerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    CompilerServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CompilerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CompilerServiceFileDescriptorSupplier())
              .addMethod(getCompileMethod())
              .build();
        }
      }
    }
    return result;
  }
}
