package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import server.adapter.TaskAdapter;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static java.nio.charset.StandardCharsets.UTF_8;
import static manager.Managers.getDefaultTaskManager;

public class HttpTaskServer {
    public static final int PORT = 8275;
    private final String apiToken;
    private final HttpServer server;
    private HttpTaskManager httpTaskManager = getDefaultTaskManager();

    TaskAdapter taskAdapter = new TaskAdapter();

    public HttpTaskServer() throws IOException {
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/load/task", this::saveNewTask);
        server.createContext("/load/epic", this::saveNewEpic);
        server.createContext("/load/subtask", this::saveNewSubTask);
        server.createContext("/history", this::getHistory);
        server.createContext("/epic/remove/subtask", this::removeAllSubtasksInEpic);
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
                Task task = taskAdapter.parseTask(value);
                httpTaskManager.saveAnyTask(task);
                System.out.println("Task успешно добавлена!");
                System.out.println("Добавленная таска: " + httpTaskManager.getTaskById(task.getId()).toString() + "\n\n\n");
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
                Epic epic = taskAdapter.parseEpic(value);
                httpTaskManager.saveAnyTask(epic);
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
                Subtask subtask = taskAdapter.parseSubtask(value);
                httpTaskManager.saveAnyTask(subtask);
                httpTaskManager.saveSubtasksInEpic((Epic) httpTaskManager.getTaskById(subtask.getParentEpicId()), subtask);
                httpTaskManager.setTimeOfEpic((Epic) httpTaskManager.getTaskById(subtask.getParentEpicId()));
                System.out.println("Subtask успешно добавлен!");
                System.out.println("Добавленный subtask: " + httpTaskManager.getTaskById(subtask.getId()).toString() + "\n");
                System.out.println("Обновленный epic" + ((Epic) httpTaskManager.getTaskById(subtask.getParentEpicId())) + "\n\n\n");
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void getHistory(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/history");
            if ("GET".equals(h.getRequestMethod())) {
                System.out.println("\n\n\n--------- All actions -----------\n");
                httpTaskManager.getHistory().forEach(task -> System.out.println(task.toString()));
            } else {
                System.out.println("/history ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
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