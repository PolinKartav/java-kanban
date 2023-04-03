package server;

import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    String url;

    Path path;
    private static KVTaskClient kvTaskClient;

    static String apiToken = new String();


    public HttpTaskManager(String url, Path path) {
        super(path);
        this.path = path;
        kvTaskClient = new KVTaskClient(url);
    }

    public void save(String key) throws IOException, InterruptedException {
        if (apiToken.isEmpty()){
            this.apiToken = kvTaskClient.registerToKVServer();
        }
        kvTaskClient.saveToKVServer(apiToken, key);
    }

    public String load() throws IOException, InterruptedException {
        if (apiToken.isEmpty()){
            this.apiToken = kvTaskClient.registerToKVServer();
        }
        return kvTaskClient.loadFromKVServer(apiToken);
    }
}
