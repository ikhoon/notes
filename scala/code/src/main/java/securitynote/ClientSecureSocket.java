package securitynote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

class ClientSecureSocket {
    public static void main(String[] args) throws IOException {
        final SocketFactory factory = SSLSocketFactory.getDefault();
        try (final SSLSocket socket = (SSLSocket) factory.createSocket("www.google.com", 443)) {
            socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
            final Writer out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            out.write("GET https://www.google.com/ HTTP/1.1\r\n");
            out.write("Host: google.com\r\n");
            out.write("\r\n");
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // READ header
            String s;
            while (!(s = in.readLine()).isEmpty()) {
                System.out.println(s);
            }
            System.out.println();

            int c;
            while ((c = in.read()) != -1) {
                System.out.write(c);
            }
        }
    }
}
