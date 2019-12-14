package user;

import java.time.Duration;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.grpc.GrpcService;

public final class UserApp {
    public static void main(String[] args) {
        final var grpcService =
                GrpcService.builder()
                           .addService(new UserService())
                           .build();

        final var server =
                Server.builder()
                      .http(18080)
                      .serviceUnder("/grpc", grpcService)
                      .serviceUnder("/docs", new DocService())
                      .requestTimeout(Duration.ZERO)
                      .build();
        server.start().join();
    }
}
