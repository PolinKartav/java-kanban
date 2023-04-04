package server;

import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.FileBackedTasksManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;


import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {

    String url;

    Path path;
    private static KVTaskClient kvTaskClient;

    static String apiToken = new String();

    Gson gson = new Gson();


    public HttpTaskManager(String url, Path path) {
        super(path);
        this.path = path;
        kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        if (apiToken.isEmpty()){
            try {
                this.apiToken = kvTaskClient.registerToKVServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (!taskSave.isEmpty()){
            try {
                kvTaskClient.saveToKVServer(apiToken, "task", gson.toJson(taskSave));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (!epicSave.isEmpty()){
            try {
                kvTaskClient.saveToKVServer(apiToken, "epic", gson.toJson(epicSave));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (!subtaskSave.isEmpty()){
            try {
                kvTaskClient.saveToKVServer(apiToken, "subtask", gson.toJson(subtaskSave));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void load() throws IOException, InterruptedException {
        if (apiToken.isEmpty()){
            this.apiToken = kvTaskClient.registerToKVServer();
        }
        String stringTask = kvTaskClient.loadFromKVServer(apiToken, "task");
        Type empMapTypeTask = new TypeToken<Map<Integer, Task>>() {}.getType();
        Map<Integer, Task> tasks = gson.fromJson(stringTask, empMapTypeTask);
        if (!tasks.isEmpty()){
            taskSave = tasks;
        }
        String stringEpic = kvTaskClient.loadFromKVServer(apiToken, "epic");
        Type empMapTypeEpic = new TypeToken<Map<Integer, Epic>>() {}.getType();
        Map<Integer, Epic> epics = gson.fromJson(stringEpic, empMapTypeEpic);
        if (!epics.isEmpty()){
            epicSave = epics;
        }
        String stringSub = kvTaskClient.loadFromKVServer(apiToken, "subtask");
        Type empMapTypeSub = new TypeToken<Map<Integer, Subtask>>() {}.getType();
        Map<Integer, Subtask> subs = gson.fromJson(stringSub, empMapTypeSub);
        if (!subs.isEmpty()){
            subtaskSave = subs;
        }
    }
}
