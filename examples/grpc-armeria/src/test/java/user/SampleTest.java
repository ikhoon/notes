package user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.linecorp.armeria.client.Clients;

import io.grpc.stub.StreamObserver;
import reactor.core.publisher.EmitterProcessor;
import users.UserOuterClass.Result;
import users.UserOuterClass.User;
import users.UserOuterClass.UserRequest;
import users.UserServiceGrpc.UserServiceStub;

@Disabled
class SampleTest {
    @Test
    void testHello() {
        System.err.println("hello world");
    }

    @Test
    void getUsers() throws InterruptedException {
        final var client = Clients.newClient("gproto+http://127.0.0.1:8080/grpc/", UserServiceStub.class);
        final EmitterProcessor<User> processor = EmitterProcessor.create();
        client.getAllUsers(UserRequest.newBuilder().setAge(10).build(), new StreamObserver<User>() {
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
                processor.onComplete();
            }
        });
        processor.subscribe(System.out::println);
        Thread.sleep(1000);
    }

    @Test
    void saveUser() throws InterruptedException {
        final var client = Clients.newClient("gproto+http://127.0.0.1:8080/grpc/", UserServiceStub.class);
        final StreamObserver<User> userPublisher = client.saveUsers(new StreamObserver<Result>() {
            @Override
            public void onNext(Result result) {
                System.out.println("result " + result);
            }

            @Override
            public void onError(final Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("finished");
            }
        });
        for (int i = 0; i <10; i++) {
            userPublisher.onNext(User.newBuilder().setId(i).setName("Name" + i).build());
        }
        userPublisher.onCompleted();
        Thread.sleep(10000);
    }
}
