package users;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.24.0)",
    comments = "Source: user.proto")
public final class UserServiceGrpc {

  private UserServiceGrpc() {}

  public static final String SERVICE_NAME = "users.UserService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<users.UserOuterClass.User,
      users.UserOuterClass.Result> getSaveUsersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SaveUsers",
      requestType = users.UserOuterClass.User.class,
      responseType = users.UserOuterClass.Result.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<users.UserOuterClass.User,
      users.UserOuterClass.Result> getSaveUsersMethod() {
    io.grpc.MethodDescriptor<users.UserOuterClass.User, users.UserOuterClass.Result> getSaveUsersMethod;
    if ((getSaveUsersMethod = UserServiceGrpc.getSaveUsersMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getSaveUsersMethod = UserServiceGrpc.getSaveUsersMethod) == null) {
          UserServiceGrpc.getSaveUsersMethod = getSaveUsersMethod =
              io.grpc.MethodDescriptor.<users.UserOuterClass.User, users.UserOuterClass.Result>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SaveUsers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  users.UserOuterClass.User.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  users.UserOuterClass.Result.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("SaveUsers"))
              .build();
        }
      }
    }
    return getSaveUsersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<users.UserOuterClass.UserRequest,
      users.UserOuterClass.User> getGetAllUsersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllUsers",
      requestType = users.UserOuterClass.UserRequest.class,
      responseType = users.UserOuterClass.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<users.UserOuterClass.UserRequest,
      users.UserOuterClass.User> getGetAllUsersMethod() {
    io.grpc.MethodDescriptor<users.UserOuterClass.UserRequest, users.UserOuterClass.User> getGetAllUsersMethod;
    if ((getGetAllUsersMethod = UserServiceGrpc.getGetAllUsersMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getGetAllUsersMethod = UserServiceGrpc.getGetAllUsersMethod) == null) {
          UserServiceGrpc.getGetAllUsersMethod = getGetAllUsersMethod =
              io.grpc.MethodDescriptor.<users.UserOuterClass.UserRequest, users.UserOuterClass.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllUsers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  users.UserOuterClass.UserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  users.UserOuterClass.User.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("GetAllUsers"))
              .build();
        }
      }
    }
    return getGetAllUsersMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserServiceStub newStub(io.grpc.Channel channel) {
    return new UserServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new UserServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new UserServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class UserServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<users.UserOuterClass.User> saveUsers(
        io.grpc.stub.StreamObserver<users.UserOuterClass.Result> responseObserver) {
      return asyncUnimplementedStreamingCall(getSaveUsersMethod(), responseObserver);
    }

    /**
     */
    public void getAllUsers(users.UserOuterClass.UserRequest request,
        io.grpc.stub.StreamObserver<users.UserOuterClass.User> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAllUsersMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSaveUsersMethod(),
            asyncClientStreamingCall(
              new MethodHandlers<
                users.UserOuterClass.User,
                users.UserOuterClass.Result>(
                  this, METHODID_SAVE_USERS)))
          .addMethod(
            getGetAllUsersMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                users.UserOuterClass.UserRequest,
                users.UserOuterClass.User>(
                  this, METHODID_GET_ALL_USERS)))
          .build();
    }
  }

  /**
   */
  public static final class UserServiceStub extends io.grpc.stub.AbstractStub<UserServiceStub> {
    private UserServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UserServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UserServiceStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<users.UserOuterClass.User> saveUsers(
        io.grpc.stub.StreamObserver<users.UserOuterClass.Result> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(getSaveUsersMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void getAllUsers(users.UserOuterClass.UserRequest request,
        io.grpc.stub.StreamObserver<users.UserOuterClass.User> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetAllUsersMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class UserServiceBlockingStub extends io.grpc.stub.AbstractStub<UserServiceBlockingStub> {
    private UserServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UserServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UserServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<users.UserOuterClass.User> getAllUsers(
        users.UserOuterClass.UserRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getGetAllUsersMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class UserServiceFutureStub extends io.grpc.stub.AbstractStub<UserServiceFutureStub> {
    private UserServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UserServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UserServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_GET_ALL_USERS = 0;
  private static final int METHODID_SAVE_USERS = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final UserServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(UserServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ALL_USERS:
          serviceImpl.getAllUsers((users.UserOuterClass.UserRequest) request,
              (io.grpc.stub.StreamObserver<users.UserOuterClass.User>) responseObserver);
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
        case METHODID_SAVE_USERS:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.saveUsers(
              (io.grpc.stub.StreamObserver<users.UserOuterClass.Result>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UserServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return users.UserOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UserService");
    }
  }

  private static final class UserServiceFileDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier {
    UserServiceFileDescriptorSupplier() {}
  }

  private static final class UserServiceMethodDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    UserServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (UserServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UserServiceFileDescriptorSupplier())
              .addMethod(getSaveUsersMethod())
              .addMethod(getGetAllUsersMethod())
              .build();
        }
      }
    }
    return result;
  }
}
