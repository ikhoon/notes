package armerianote;

import brave.Tracing;
import brave.http.HttpTracing;
import com.linecorp.armeria.client.*;
import com.linecorp.armeria.client.brave.BraveClient;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreaker;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerHttpClient;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerStrategy;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.client.endpoint.EndpointGroupRegistry;
import com.linecorp.armeria.client.endpoint.EndpointSelectionStrategy;
import com.linecorp.armeria.client.endpoint.healthcheck.HealthCheckedEndpointGroup;
import com.linecorp.armeria.client.retry.RetryStrategy;
import com.linecorp.armeria.client.retry.RetryingHttpClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.common.brave.RequestContextCurrentTraceContext;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.healthcheck.HealthCheckService;
import com.linecorp.armeria.server.healthcheck.SettableHealthChecker;
import com.twitter.finagle.client.EndpointRegistry;
import org.junit.BeforeClass;
import org.junit.Test;
import zipkin2.Span;
import zipkin2.reporter.Reporter;

public class HealthCheckExampleTest {

    static final String HEALTH_CHECK_PATH = "/health";
    static Server server;
    private static final SettableHealthChecker health = new SettableHealthChecker();


//    @BeforeClass
    public static void runServer() {
        Server server = Server.builder()
                .http(8080)
                .service(HEALTH_CHECK_PATH, HealthCheckService.builder().build())
                .service("/hello", (ctx, req) -> HttpResponse.of("Hello world!"))
                .build();
        HealthCheckExampleTest.server = server;
        server.start().join();
    }

    @Test
    public void healthCheckClient() {
        Endpoint endpoint = Endpoint.of("127.0.0.1", server.activeLocalPort());
        EndpointGroup endpointGroup = HealthCheckedEndpointGroup
                .builder(EndpointGroup.of(endpoint), HEALTH_CHECK_PATH)
                .build();
        EndpointGroupRegistry.register("myService", endpointGroup,
                EndpointSelectionStrategy.WEIGHTED_ROUND_ROBIN);
        HttpClient client = HttpClient.of("http://group:myService");
        HttpResponse response = client.get("/hello");
        ;
    }

    @Test
    public void healthCheckFail() {
        health.setHealthy(false);
        final Endpoint endpoint = Endpoint.of("127.0.0.1", server.activeLocalPort());
        final HealthCheckedEndpointGroup healthCheckedEndpointGroup = HealthCheckedEndpointGroup
                .builder(EndpointGroup.of(endpoint), HEALTH_CHECK_PATH)
                .build();
        EndpointGroupRegistry.register("myService",
                healthCheckedEndpointGroup,
                EndpointSelectionStrategy.WEIGHTED_ROUND_ROBIN);
        final HttpClient client = HttpClient.of("http://group:myService");
        final AggregatedHttpResponse response = client.get("/hello").aggregate().join();
        final String content = response.contentUtf8();
        System.out.println(content);
    }

    @Test
    public void circuitBreaker() {
        final CircuitBreakerStrategy strategy = CircuitBreakerStrategy.onServerErrorStatus();
        final HttpClient client = new HttpClientBuilder("http://127.0.0.1:8080")
                .decorator(CircuitBreakerHttpClient
                        .builder(strategy)
                        .newDecorator())
                .build();
        final AggregatedHttpResponse response = client.get("/hello").aggregate().join();
        System.out.println(response.contentUtf8());
    }

    @Test
    public void automaticRetry() {
        RetryStrategy strategy = RetryStrategy.onUnprocessed();
        HttpClient client = new HttpClientBuilder("http://127.0.0.1:8080")
          .decorator(RetryingHttpClient
                  .builder(strategy)
                  .newDecorator())
          .build();

        AggregatedHttpResponse response = client.get("/hello").aggregate().join();
        System.out.println(response.contentUtf8());
    }

    @Test
    public void zipkinTracing() {

        Reporter<Span> myReporter = null;
        Tracing tracing = Tracing.newBuilder()
                .localServiceName("myService")
                .currentTraceContext(RequestContextCurrentTraceContext.ofDefault())
                .spanReporter(myReporter)
                .build();

        HttpTracing httpTracing = HttpTracing.create(tracing);
        final HttpClient client = new HttpClientBuilder("http://myBackend.com")
                .decorator(BraveClient.newDecorator(httpTracing.clientOf("myBackend")))
                .build();
    }

    @Test
    public void armeriaServer() throws InterruptedException {
        final Server server = Server.builder()
                .http(8080)
                .service("/", (ctx, req) -> HttpResponse.of(HttpStatus.OK))
                .build();
        server.start().join();
        Thread.sleep(100000);
    }
}