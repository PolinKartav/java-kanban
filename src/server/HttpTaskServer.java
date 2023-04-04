package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import tasks.Epic;

import tasks.Subtask;
import tasks.Task;

import static java.nio.charset.StandardCharsets.UTF_8;


public class HttpTaskServer {
    public static final int PORT = 8274;
    private final String apiToken;
    private final HttpServer server;
    private HttpTaskManager httpTaskManager;

    private Gson gson = new Gson();

    public HttpTaskServer(HttpTaskManager manager) throws IOException {
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/load/task", this::saveNewTask);
        server.createContext("/load/epic", this::saveNewEpic);
        server.createContext("/load/subtask", this::saveNewSubTask);
        server.createContext("/history", this::getHistory);
        server.createContext("/epic/remove/subtask", this::removeAllSubtasksInEpic);
        httpTaskManager = manager;
    }

    private void saveNewTask(HttpExchange h) throws IOException {
        System.out.println("---------- Trying to save new Task -------------\n");
        try{
            System.out.println("\n/save/task/\n");
            if (!hasAuth(h, apiToken)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(h);
                if (value.isEmpty()) {
                    System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                Task task = this.gson.fromJson(value, Task.class);
                //httpTaskManager.save("task");
                //Task task = taskAdapter.parseTask(value);
                httpTaskManager.saveAnyTask(task);
                System.out.println("Task успешно добавлена!");
                System.out.println("Добавленная таска: " + httpTaskManager.getTaskById(task.getId()).toString() + "\n\n\n");
                //httpTaskManager.save();
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    protected boolean hasAuth(HttpExchange h, String apiToken) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private void saveNewEpic(HttpExchange h) throws IOException {
        System.out.println("---------- Trying to save new Epic -------------\n");
        try{
            System.out.println("\n/save/epic/\n");
            if (!hasAuth(h, apiToken)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(h);
                if (value.isEmpty()) {
                    System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                Epic epic = this.gson.fromJson(value, Epic.class);
                httpTaskManager.saveAnyTask(epic);
                //httpTaskManager.save();
                System.out.println("Epic успешно добавлен!");
                System.out.println("Добавленный epic: " + httpTaskManager.getTaskById(epic.getId()).toString() + "\n\n\n");
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void saveNewSubTask(HttpExchange h) throws IOException {
        System.out.println("---------- Trying to save new SubTask -------------\n");
        try{
            System.out.println("\n/save/subtask/\n");
            if (!hasAuth(h, apiToken)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(h);
                if (value.isEmpty()) {
                    System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                Subtask subtask = this.gson.fromJson(value, Subtask.class);
                httpTaskManager.saveAnyTask(subtask);
                httpTaskManager.saveSubtasksInEpic((Epic) httpTaskManager.getTaskById(subtask.getParentEpicId()), subtask);
                httpTaskManager.setTimeOfEpic((Epic) httpTaskManager.getTaskById(subtask.getParentEpicId()));
                //httpTaskManager.save();
                System.out.println("Subtask успешно добавлен!");
                System.out.println("Добавленный subtask: " + httpTaskManager.getTaskById(subtask.getId()).toString() + "\n");
                System.out.println("Обновленный epic" + ((Epic) httpTaskManager.getTaskById(subtask.getParentEpicId())) + "\n\n\n");
                h.sendResponseHeaders(200, 0);
                httpTaskManager.load();
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            h.close();
        }
    }

    private void getHistory(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/history");
            if ("GET".equals(h.getRequestMethod())) {
                System.out.println("\n\n\n--------- All actions -----------\n");
                httpTaskManager.load();
            } else {
                System.out.println("/history ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            h.close();
        }
    }

    private void removeAllSubtasksInEpic(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/epic/remove/subtask");
            if ("DELETE".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/epic/remove/subtask".length());
                httpTaskManager.removeAllSubtasksInEpic(Integer.valueOf(key));
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void register(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/register");
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, apiToken);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    public void stop() {
        System.out.println("Останавливаем сервер на порту " + PORT);
        server.stop(100000000);
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}