package user;

import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.stub.StreamObserver;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import users.UserOuterClass.Result;
import users.UserOuterClass.User;
import users.UserOuterClass.UserRequest;
import users.UserServiceGrpc.UserServiceImplBase;

public final class UserService extends UserServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public static final String PAYLOAD;
    static {
        final StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            stringBuffer.append("Welcome to Armeria");
        }
        PAYLOAD = stringBuffer.toString();
    }
    @Override
    public StreamObserver<User> saveUsers(StreamObserver<Result> responseObserver) {
        Processor<User, User> processor = EmitterProcessor.create();
        final Publisher<User> publisher = processor;
        final Subscriber<User> subscriber = processor;

        return new StreamObserver<User>() {
            @Override
            public void onNext(User user) {
                processor.onNext(user);
            }

            @Override
            public void onError(Throwable throwable) {
                processor.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(Result.newBuilder().setStatus(200).build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getAllUsers(UserRequest request, StreamObserver<User> responseObserver) {
        final Flux<User> publisher =
                Flux.range(1, 10000000)
                    .map(this::newUser)
                    .doOnNext(user -> logger.info("Get '" + user.getName() + "' information"))
                    .take(10000000);
        publisher.subscribe(responseObserver::onNext,
                            responseObserver::onError,
                            responseObserver::onCompleted);

    }

    private User newUser(final Integer id) {
        return User.newBuilder().setId(Math.toIntExact(id)).setName("User_" + id).setPayload(PAYLOAD).build();
   }
}
