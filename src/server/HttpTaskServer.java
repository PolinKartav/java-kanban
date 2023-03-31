package server;

import com.google.gson.Gson;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.sun.net.httpserver.HttpExchange;
import manager.Managers;
import manager.TaskManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tasks.Epic;
import tasks.StatusChoice;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static java.nio.charset.StandardCharsets.UTF_8;
import static tasks.StatusChoice.leadToStatusChoice;

public class HttpTaskServer {

    private TaskManager manager1 = Managers.getDefault();
    private JSONParser parser = new JSONParser();

    public void createTask(HttpExchange h, String apiToken) throws IOException, ParseException {
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
                Task task = parseTask(value);
                manager1.saveAnyTask(task);
                System.out.println("Task успешно добавлена!");
                System.out.println("Добавленная таска: " + manager1.getTaskById(task.getId()).toString() + "\n\n\n");
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    public void createEpic(HttpExchange h, String apiToken) throws IOException, ParseException {
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
                Epic epic = parseEpic(value);
                manager1.saveAnyTask(epic);
                System.out.println("Epic успешно добавлен!");
                System.out.println("Добавленный epic: " + manager1.getTaskById(epic.getId()).toString() + "\n\n\n");
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    public void createSubtask(HttpExchange h, String apiToken) throws IOException, ParseException {
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
                Subtask subtask = parseSubtask(value);
                manager1.saveAnyTask(subtask);
                manager1.saveSubtasksInEpic((Epic) manager1.getTaskById(subtask.getParentEpicId()), subtask);
                manager1.setTimeOfEpic((Epic) manager1.getTaskById(subtask.getParentEpicId()));
                System.out.println("Subtask успешно добавлен!");
                System.out.println("Добавленный subtask: " + manager1.getTaskById(subtask.getId()).toString() + "\n");
                System.out.println("Обновленный epic" + ((Epic) manager1.getTaskById(subtask.getParentEpicId())) + "\n\n\n");
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

    private Task parseTask(String jsonString) {
        //this.gson.toJson(jsonString);
        this.parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);

            String name =  json.get("name").toString();
            int id = ((Long) json.get("id")).intValue();
            String description =  json.get("description").toString();
            StatusChoice status =leadToStatusChoice(json.get("status").toString());
            JSONObject jsonStart = (JSONObject) json.get("startTime");
            JSONObject jsonDate = (JSONObject) jsonStart.get("date");
            JSONObject jsonTime = (JSONObject) jsonStart.get("time");
            LocalDateTime startTime = getLocalTimeFromJson(jsonDate, jsonTime);
            long duration = (Long) json.get("duration");
            return new Task(id, name, description, status, startTime, duration);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Epic parseEpic(String jsonString){
        this.parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);

            String name =  json.get("name").toString();
            int id = ((Long) json.get("id")).intValue();
            String description =  json.get("description").toString();
            StatusChoice status =leadToStatusChoice(json.get("status").toString());
            JSONObject jsonStart = (JSONObject) json.get("startTime");
            JSONObject jsonDate = (JSONObject) jsonStart.get("date");
            JSONObject jsonTime = (JSONObject) jsonStart.get("time");
            LocalDateTime startTime = getLocalTimeFromJson(jsonDate, jsonTime);
            long duration = (Long) json.get("duration");
            return new Epic(id, name, description, status, startTime, duration);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Subtask parseSubtask(String jsonString){
        this.parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);

            String name =  json.get("name").toString();
            int id = ((Long) json.get("id")).intValue();
            Integer parentEpicId = ((Long) json.get("parentEpicId")).intValue();
            String description =  json.get("description").toString();
            StatusChoice status =leadToStatusChoice(json.get("status").toString());
            JSONObject jsonStart = (JSONObject) json.get("startTime");
            JSONObject jsonDate = (JSONObject) jsonStart.get("date");
            JSONObject jsonTime = (JSONObject) jsonStart.get("time");
            LocalDateTime startTime = getLocalTimeFromJson(jsonDate, jsonTime);
            long duration = (Long) json.get("duration");
            return new Subtask(id, parentEpicId, name, description, status, startTime, duration);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private LocalDateTime getLocalTimeFromJson(JSONObject jsonDate, JSONObject jsonTime){
        return LocalDateTime.of(((Long) jsonDate.get("year")).intValue(), ((Long) jsonDate.get("month")).intValue(), ((Long) jsonDate.get("day")).intValue(), ((Long) jsonTime.get("hour")).intValue(), ((Long) jsonTime.get("minute")).intValue());
    }
}
