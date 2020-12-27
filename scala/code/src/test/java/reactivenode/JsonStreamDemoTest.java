package reactivenode;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.armeria.client.ClientOptions;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpHeaders;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.sse.ServerSentEvent;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.ProducesJsonSequences;
import com.linecorp.armeria.server.streaming.JsonTextSequences;
import com.linecorp.armeria.server.streaming.ServerSentEvents;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import reactor.core.publisher.Flux;

public class JsonStreamDemoTest {
    static Server userServer;
    static Server pushServer;
    static WebClient userClient;
    static WebClient pushClient;
    static Random random = new Random();
    static ObjectMapper mapper = new ObjectMapper();

    public static class User {
        public final long id;
        public final String name;
        public final int age;

        @JsonCreator
        public User(@JsonProperty("id") long id,
                    @JsonProperty("name") String name,
                    @JsonProperty("age") int age) {
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
                .http(8080)
                .annotatedService("/api", new UserService())
                .service("/api-observable-error-zip", (ctx, req) -> {
                    ctx.setRequestTimeout(Duration.ZERO);
                    final Observable<User> userObservable = Observable.zip(
                            Observable.interval(1000, TimeUnit.MILLISECONDS).take(Integer.MAX_VALUE - 1),
                            Observable.range(1, Integer.MAX_VALUE)
                                    .subscribeOn(Schedulers.newThread())
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
                .service("/api-observable-interval", (ctx, req) -> {
                    ctx.setRequestTimeout(Duration.ZERO);
                    final Observable<User> observable = Observable.interval(100, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.newThread())
                            .map(id -> new User(id, "User" + id, random.nextInt(100)))
                            .take(1005);
                    return JsonTextSequences.fromPublisher(observable.toFlowable(BackpressureStrategy.BUFFER));
                })
                .service("/api-observable-error-range", (ctx, req) -> {
                    ctx.setRequestTimeout(Duration.ZERO);
                    final Observable<User> observable = Observable.interval(100, TimeUnit.MILLISECONDS)
                            .map(id -> new User(id, "User" + id, random.nextInt(100)))
                            .take(Long.MAX_VALUE);
                    return JsonTextSequences.fromPublisher(observable.toFlowable(BackpressureStrategy.BUFFER));
                })
                .service("/api-sse", (ctx, req) -> {
                    ctx.setRequestTimeout(Duration.ZERO);
                    final Observable<String> observable = Observable.interval(100, TimeUnit.MILLISECONDS)
                                                                    .subscribeOn(Schedulers.newThread())
                                                                    .map(id -> new User(id, "User" + id,
                                                                                        random.nextInt(100)))
                                                                    .map(mapper::writeValueAsString)
                                                                    .take(1005);
                    return ServerSentEvents.fromPublisher(observable.toFlowable(BackpressureStrategy.BUFFER),
                                                          ServerSentEvent::ofData);
                })
                           .build();
        userServer.start().join();

        userClient = WebClient.builder("http://127.0.0.1:" + userServer.activeLocalPort())
                              .options(ClientOptions.of())
                              .build();

        // subscribe & produce json data stream
        pushServer = Server.builder()
                           .service("/push", (ctx, req) -> HttpResponse.of(200))
                           .build();
        pushServer.start().join();
        pushClient = WebClient.of("http://127.0.0.1:" + pushServer.activeLocalPort());
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
                    } else {
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
    public void jsonSequenceTestObservableInterval() throws InterruptedException {
        System.out.println("http://127.0.0.1:" + userServer.activeLocalPort() + "/api-observable-interval");
        Thread.sleep(1000000000L);
        Observable.fromPublisher(userClient.get("/api-observable-interval"))
                .blockingForEach(httpObject -> {
                    if (httpObject instanceof HttpHeaders) {
                        System.out.println("##### " + ((HttpHeaders) httpObject).get("status"));
                    } else if (httpObject instanceof HttpData) {
                        System.out.println("### Data" + ((HttpData) httpObject).toStringUtf8());
                    }
                });
    }

    @Test
    public void serverSentObservableInterval() throws InterruptedException {
        System.out.println("http://127.0.0.1:" + userServer.activeLocalPort() + "/api-sse");
        Observable.fromPublisher(userClient.get("/api-sse"))
                .blockingForEach(httpObject -> {
                    if (httpObject instanceof HttpHeaders) {
                        System.out.println("##### " + ((HttpHeaders) httpObject).get("content-type"));
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
