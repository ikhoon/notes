package armerianote;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class HelloWorld {
    public static void main(String[] args) throws CertificateException, SSLException {
        Server server = new ServerBuilder()
                .http(8080)
                .https(8443)
                .tlsSelfSigned()
                .service("/", (ctx, req) -> HttpResponse.of("Hello, World!"))
                .build();
        server.start();
    }
}
