package armerianote;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.server.Server;

public class ArmeriaProxy {
    public static void main(String[] args) {
        // Use Armeria's async & reactive HTTP/2 client.
        var client = WebClient.of("h2c://backend");
        var server = Server.builder()
                           .http(8080)
                           .service("prefix:/",
                                    // Forward all requests reactively
                                    (ctx, req) -> client.execute(req))
                           .build();
    }
}
