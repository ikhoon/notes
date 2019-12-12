package armerianote;

import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.server.Server;

public class ArmeriaProxy {
    public static void main(String[] args) {
        // Use Armeria's async & reactive HTTP/2 client.
        var client = HttpClient.of("h2c://backend");
        var server = Server.builder()
                           .http(8080)
                           .service("prefix:/",
                                    // Forward all requests reactively
                                    (ctx, req) -> client.execute(req))
                           .build();
    }
}
