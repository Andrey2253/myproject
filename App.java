import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Instant;

public class App {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);

        server.createContext("/", (HttpExchange exchange) -> {
            String ip = exchange.getRemoteAddress().getAddress().getHostAddress();
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String ts = Instant.now().toString();
            System.out.printf("[%s] %s %s %s%n", ts, ip, method, path);

            if ("/ping".equals(path) && "GET".equalsIgnoreCase(method)) {
                byte[] body = "pong".getBytes();
                exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
                exchange.sendResponseHeaders(200, body.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body);
                }
            } else {
                byte[] body = "Not Found".getBytes();
                exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
                exchange.sendResponseHeaders(404, body.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body);
                }
            }
        });

        System.out.println("Server JAVA started on port " + port);
        server.start();
    }
}