package push;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.util.concurrent.Uninterruptibles;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.Post;

import io.grpc.stub.StreamObserver;
import reactor.core.publisher.EmitterProcessor;
import users.UserOuterClass.User;
import users.UserOuterClass.UserRequest;
import users.UserServiceGrpc.UserServiceStub;

public class PushService {
    private static final Logger logger = LoggerFactory.getLogger(PushService.class);

    private final UserServiceStub userService;
    private int delayMillis = 10;

    public PushService(UserServiceStub userService) {
        this.userService = userService;
    }

    @Post("/messages")
    public CompletableFuture<Void> pushMessage(PushRequest request) {
        final var processor = EmitterProcessor.create();
        final var completionFuture = new CompletableFuture<Void>();
        processor.doOnComplete(() -> completionFuture.complete(null));

        userService.getAllUsers(newUserRequest(request.minAge), new StreamObserver<User>() {
            @Override
            public void onNext(User user) {
                logger.info("Push '" + user.getName() + "' to " + request.message);
                Uninterruptibles.sleepUninterruptibly(delayMillis, TimeUnit.MILLISECONDS);
                delayMillis += 10;
            }

            @Override
            public void onError(Throwable throwable) {
                processor.onError(throwable);
            }

            @Override
            public void onCompleted() {
                processor.onComplete();
            }
        });
        return completionFuture;
    }

    private static UserRequest newUserRequest(@Param int minAge) {
        return UserRequest.newBuilder().setAge(minAge).build();
    }

    static class PushRequest {
        @Param
        private String message;
        @Param
        private int minAge;

        @JsonCreator
        public PushRequest(@JsonProperty("message") String message,
                           @JsonProperty("minAge") int minAge) {
            this.message = message;
            this.minAge = minAge;
        }

        public String message() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int minAge() {
            return minAge;
        }

        public void setMinAge(int minAge) {
            this.minAge = minAge;
        }
    }
}
