package reactivenode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.client.ClientOptions;
import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.common.*;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.ProducesJsonSequences;
import com.linecorp.armeria.server.streaming.JsonTextSequences;
import io.micrometer.core.instrument.util.JsonUtils;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JsonStreamDemoTest {
    static Server userServer;
    static Server pushServer;
    static HttpClient userClient;
    static HttpClient pushClient;
    static Random random = new Random();

    public static class User {
        final long id;
        final String name;
        final int age;

        @JsonCreator
        public User(@JsonProperty("id") long id,
             @JsonProperty("name") String name,
             @JsonProperty("age")int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    static class PushResult {
        final int userId;
        final String result;

        @JsonCreator
        PushResult(int userId, String result) {
            this.userId = userId;
            this.result = result;
        }
    }

    static class UserService {
        @Get("/users")
        @ProducesJsonSequences
        public Observable<User> getAllUsers() {
            return Observable.zip(
                    Observable.interval(1000, TimeUnit.MILLISECONDS).take(Integer.MAX_VALUE - 1),
                    Observable.range(1, Integer.MAX_VALUE)
                            .map(idx -> new User(idx, "User" + idx, random.nextInt(100))),
                    (aLong, integer) -> integer)
                    .doOnNext(user -> System.out.println("### Server: " + Instant.now() + ' ' + user));
        }
    }

    static class PushService {
        @Post("push")
        public PushResult pushToOlderThan(@Param int age) {
//            Observable.fromPublisher(userClient.get("/users"));
            return null;
        }
    }

    @Before
    public void init() {
        // produce json data stream
        userServer = Server.builder()
                .annotatedService("/api", new UserService())
                .service("/api-observable-error-zip", (ctx, req) -> {
                    ctx.setRequestTimeout(Duration.ZERO);
                    final Observable<User> userObservable = Observable.zip(
                            Observable.interval(1000, TimeUnit.MILLISECONDS).take(Integer.MAX_VALUE - 1),
                            Observable.range(1, Integer.MAX_VALUE)
                                    .map(idx -> new User(idx, "User" + idx, random.nextInt(100))),
                            (aLong, integer) -> integer);
                    return JsonTextSequences.fromPublisher(userObservable.toFlowable(BackpressureStrategy.BUFFER));
                })
                .service("/api-flux-error", (ctx, req) -> {
                    ctx.setRequestTimeout(Duration.ZERO);
                    final Flux<User> flux = Flux.interval(Duration.ofMillis(100))
                            .map(id -> new User(id, "User" + id, random.nextInt(100)))
                            .take(Long.MAX_VALUE);
                    return JsonTextSequences.fromPublisher(flux);
                })
                .service("/api-observable-error-interval",(ctx, req) -> {
                    ctx.setRequestTimeout(Duration.ZERO);
                    final Observable<User> observable = Observable.interval(100, TimeUnit.MILLISECONDS)
                            .map(id -> new User(id, "User" + id, random.nextInt(100)))
                            .take(Long.MAX_VALUE);
                    return JsonTextSequences.fromPublisher(observable.toFlowable(BackpressureStrategy.BUFFER));

                })

                .service("/api-observable-error-range",(ctx, req) -> {
                    ctx.setRequestTimeout(Duration.ZERO);
                    final Observable<User> observable = Observable.interval(100, TimeUnit.MILLISECONDS)
                            .map(id -> new User(id, "User" + id, random.nextInt(100)))
                            .take(Long.MAX_VALUE);
                    return JsonTextSequences.fromPublisher(observable.toFlowable(BackpressureStrategy.BUFFER));

                })
                .build();
        userServer.start().join();

        userClient = HttpClient.of("http://127.0.0.1:" + userServer.activeLocalPort(), ClientOptions.of());

        // subscribe & produce json data stream
        pushServer = Server.builder()
                .service("/push", (ctx, req) -> HttpResponse.of(200))
                .build();
        pushServer.start().join();
        pushClient = HttpClient.of("http://127.0.0.1:" + pushServer.activeLocalPort());
    }

    @Test
    public void userServiceSubscribe() throws InterruptedException {
        Observable.fromPublisher(userClient.get("/api/users"))
                .forEach(httpObject -> {
                    if (httpObject instanceof HttpHeaders) {
                        System.out.println("##### " + ((HttpHeaders) httpObject).get("status"));
                    } else if (httpObject instanceof HttpData) {
                        System.out.println("#######");
                        System.out.println(((HttpData) httpObject).toStringUtf8());
                    }
                });
        Thread.sleep(10000);
    }

    @Test
    public void userServiceSubscribeFlux() throws InterruptedException {
        Flux.from(userClient.get("/api2"))
                .subscribe(httpObject -> {
                    if (httpObject instanceof HttpHeaders) {
                        System.out.println("##### " + ((HttpHeaders) httpObject).get("status"));
                    } else if (httpObject instanceof HttpData) {
                        System.out.println("#######");
                        System.out.println(((HttpData) httpObject).toStringUtf8());
                    }
                    else {
                        System.out.println(httpObject);
                    }
                });
        Thread.sleep(20000);
    }

    @Test
    public void jsonSequenceTest() {
        Observable.fromPublisher(userClient.get("/api2"))
                .blockingForEach(httpObject -> {
                    if (httpObject instanceof HttpHeaders) {
                        System.out.println("##### " + ((HttpHeaders) httpObject).get("status"));
                    } else if (httpObject instanceof HttpData) {
                        System.out.println("#######");
                        System.out.println(((HttpData) httpObject).toStringUtf8());
                    }
                });
    }

    @Test
    public void jsonSequenceTestFlux() {
        Observable.fromPublisher(userClient.get("/api-flux-error"))
                .blockingForEach(httpObject -> {
                    if (httpObject instanceof HttpHeaders) {
                        System.out.println("##### " + ((HttpHeaders) httpObject).get("status"));
                    } else if (httpObject instanceof HttpData) {
                        System.out.println("### Data" + ((HttpData) httpObject).toStringUtf8());
                    }
                });
    }

    @Test
    public void test() {

    }
    @Test
    public void jsonSequenceTestObservableInterval() {
        Observable.fromPublisher(userClient.get("/api-observable-error-interval"))
                .blockingForEach(httpObject -> {
                    if (httpObject instanceof HttpHeaders) {
                        System.out.println("##### " + ((HttpHeaders) httpObject).get("status"));
                    } else if (httpObject instanceof HttpData) {
                        System.out.println("### Data" + ((HttpData) httpObject).toStringUtf8());
                    }
                });
    }

    @Test
    public void jsonSequenceTestObservableZip() {
        Observable.fromPublisher(userClient.get("/api-observable-error-zip"))
                .blockingForEach(httpObject -> {
                    if (httpObject instanceof HttpHeaders) {
                        System.out.println("##### " + ((HttpHeaders) httpObject).get("status"));
                    } else if (httpObject instanceof HttpData) {
                        System.out.println("### Data" + ((HttpData) httpObject).toStringUtf8());
                    }
                });
    }

    @Test
    public void jsonSequenceTestObservableRange() {
        Observable.fromPublisher(userClient.get("/api-observable-error-range"))
                .blockingForEach(httpObject -> {
                    if (httpObject instanceof HttpHeaders) {
                        System.out.println("##### " + ((HttpHeaders) httpObject).get("status"));
                    } else if (httpObject instanceof HttpData) {
                        System.out.println("### Data" + ((HttpData) httpObject).toStringUtf8());
                    }
                });
    }


    @Test
    public void json() throws JsonProcessingException {
        final User user = new User(1, "a", 10);
        final ObjectMapper objectMapper = new ObjectMapper();
        final String string = objectMapper.writeValueAsString(user);
        System.out.println(string);
    }
}
