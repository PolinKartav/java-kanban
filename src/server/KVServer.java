package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KVServer {

    private final int port;
    protected String apiToken;
    private final HttpServer server;
    public final Map<String, String> data = new HashMap<>();

    public KVServer(String hostname, int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        this.port = port;
        apiToken = generateApiToken();
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
    }

    private void load(HttpExchange h) throws IOException {
        try {
            if (hasNotAuth(h)) {
                h.sendResponseHeaders(403, 0);
                return;
            }

            if ("GET".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/load/".length());

                if (key.isBlank()) {
                    h.sendResponseHeaders(401, 0);
                    return;
                }

                if (!data.containsKey(key)) {
                    h.sendResponseHeaders(400, 0);
                    return;
                }

                sendText(h, data.get(key));
            } else {
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void save(HttpExchange h) throws IOException {
        try {
            if (hasNotAuth(h)) {
                h.sendResponseHeaders(403, 0);
                return;
            }

            if ("POST".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());

                if (key.isEmpty()) {
                    h.sendResponseHeaders(400, 0);
                    return;
                }

                String value = readText(h);

                if (value.isEmpty()) {
                    h.sendResponseHeaders(400, 0);
                    return;
                }

                data.put(key, value);
                h.sendResponseHeaders(200, 0);
            } else {
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void register(HttpExchange h) throws IOException {
        try {
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, apiToken);
            } else {
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + port);
        System.out.println("Доступ по http://localhost:" + port + "/");
        System.out.println("API_TOKEN=" + apiToken);
        server.start();
    }

    public void stop(int delay) {
        server.stop(delay);
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasNotAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery == null || (!rawQuery.contains("API_TOKEN=" + apiToken) && !rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
