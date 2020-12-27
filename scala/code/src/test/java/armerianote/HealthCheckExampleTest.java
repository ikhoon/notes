package armerianote;

import org.junit.Test;

import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.brave.BraveClient;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerClient;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerStrategy;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.client.endpoint.healthcheck.HealthCheckedEndpointGroup;
import com.linecorp.armeria.client.retry.RetryStrategy;
import com.linecorp.armeria.client.retry.RetryingClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.common.brave.RequestContextCurrentTraceContext;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.healthcheck.HealthCheckService;
import com.linecorp.armeria.server.healthcheck.SettableHealthChecker;

import brave.Tracing;
import brave.http.HttpTracing;
import zipkin2.Span;
import zipkin2.reporter.Reporter;

public class HealthCheckExampleTest {

    static final String HEALTH_CHECK_PATH = "/health";
    static Server server;
    private static final SettableHealthChecker health = new SettableHealthChecker();


//    @BeforeClass
    public static void runServer() {
        final Server server = Server.builder()
                                    .http(8080)
                                    .service(HEALTH_CHECK_PATH, HealthCheckService.builder().build())
                                    .service("/hello", (ctx, req) -> HttpResponse.of("Hello world!"))
                                    .build();
        HealthCheckExampleTest.server = server;
        server.start().join();
    }

    @Test
    public void healthCheckClient() {
        final Endpoint endpoint = Endpoint.of("127.0.0.1", server.activeLocalPort());
        final EndpointGroup endpointGroup = HealthCheckedEndpointGroup
                .builder(EndpointGroup.of(endpoint), HEALTH_CHECK_PATH)
                .build();
        final WebClient client = WebClient.of(SessionProtocol.HTTP, endpointGroup);
        final HttpResponse response = client.get("/hello");
    }

    @Test
    public void healthCheckFail() {
        health.setHealthy(false);
        final Endpoint endpoint = Endpoint.of("127.0.0.1", server.activeLocalPort());
        final HealthCheckedEndpointGroup healthCheckedEndpointGroup = HealthCheckedEndpointGroup
                .builder(EndpointGroup.of(endpoint), HEALTH_CHECK_PATH)
                .build();
        final WebClient client = WebClient.of(SessionProtocol.HTTP, healthCheckedEndpointGroup);
        final AggregatedHttpResponse response = client.get("/hello").aggregate().join();
        final String content = response.contentUtf8();
        System.out.println(content);
    }

    @Test
    public void circuitBreaker() {
        final CircuitBreakerStrategy strategy = CircuitBreakerStrategy.onServerErrorStatus();
        final WebClient client = WebClient.builder("http://127.0.0.1:8080")
                                          .decorator(CircuitBreakerClient.builder(strategy).newDecorator())
                                          .build();
        final AggregatedHttpResponse response = client.get("/hello").aggregate().join();
        System.out.println(response.contentUtf8());
    }

    @Test
    public void automaticRetry() {
        final RetryStrategy strategy = RetryStrategy.onUnprocessed();
        final WebClient client = WebClient.builder("http://127.0.0.1:8080")
                                          .decorator(RetryingClient.builder(strategy).newDecorator())
                                          .build();

        final AggregatedHttpResponse response = client.get("/hello").aggregate().join();
        System.out.println(response.contentUtf8());
    }

    @Test
    public void zipkinTracing() {

        final Reporter<Span> myReporter = null;
        final Tracing tracing = Tracing.newBuilder()
                                       .localServiceName("myService")
                                       .currentTraceContext(RequestContextCurrentTraceContext.ofDefault())
                                       .spanReporter(myReporter)
                                       .build();

        final HttpTracing httpTracing = HttpTracing.create(tracing);
        final WebClient client =
                WebClient.builder("http://myBackend.com")
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