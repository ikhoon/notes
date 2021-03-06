package armerianote;

import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.*;
import com.linecorp.armeria.server.*;
import com.linecorp.armeria.server.annotation.decorator.LoggingDecorators;
import com.linecorp.armeria.server.logging.LoggingService;

public class DecoratorExample {
    public static void main(String[] args) {
        HttpService service = (ctx, req) -> HttpResponse.of(HttpStatus.OK);
        final HttpService decorate = service
                .decorate(AuthService::new)
                .decorate(LoggingService.newDecorator());
        final Server server = Server.builder()
                .serviceUnder("/web", decorate)
                .build();

        server.start().join();

        WebClient client = WebClient.of("http://127.0.0.1:" + server.activeLocalPort());
        final AggregatedHttpResponse response = client.get("/web/hello").aggregate().join();
        System.out.println(response.status());
    }

    static class AuthService extends SimpleDecoratingHttpService {

        @Override
        public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
            if (!authenticate(req)) {
                return HttpResponse.of(HttpStatus.UNAUTHORIZED);
            }
            return unwrap().serve(ctx, req);
        }

        AuthService(HttpService delegate) {
            super(delegate);
        }
    }

    static boolean authenticate(Request req) {
        return false;
    }
}
