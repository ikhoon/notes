package push;

import java.time.Duration;

import com.linecorp.armeria.client.ClientOptionsBuilder;
import com.linecorp.armeria.client.Clients;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.docs.DocService;

import users.UserServiceGrpc.UserServiceStub;

public class PushApp {
    public static void main(String[] args) {
        final var clientOptions = new ClientOptionsBuilder()
                .responseTimeout(Duration.ZERO).build();
        final var userServiceStub =
                Clients.newClient("gproto+http://127.0.0.1:18080/grpc/",
                                  UserServiceStub.class, clientOptions);

        final var pushService = new PushService(userServiceStub);
        final var server = Server.builder()
                                 .http(18081)
                                 .annotatedService("/push", pushService)
                                 .serviceUnder("/docs", new DocService())
                                 .build();
        server.start().join();
    }

}
