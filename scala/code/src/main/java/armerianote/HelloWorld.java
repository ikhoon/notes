package armerianote;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;

public class HelloWorld {
    public static void main(String[] args) throws CertificateException, SSLException {
        Server server = Server.builder()
                .http(8080)
                .https(8443)
                .tlsSelfSigned()
                .service("/", (ctx, req) -> HttpResponse.of("Hello, World!"))
                .build();
        server.start();
    }
}
