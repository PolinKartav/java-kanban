import client.HttpClient;
import manager.Managers;
import server.HttpTaskManager;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.StatusChoice;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Month;

import static java.util.Calendar.MAY;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Path path = Path.of("resources/http-backup.csv");

        KVServer kvServer = new KVServer("0.0.0.0", 7586);
        kvServer.start();

        HttpTaskManager taskManager = new HttpTaskManager("http://0.0.0.0:7586", path);
        HttpTaskServer server = Managers.getDefaultHttpTaskServer(taskManager);
        server.start();

        HttpClient client = new HttpClient();
        String apiToken = client.register();
        client.createTask(newTask(), apiToken);
        client.createEpic(newEpic(), apiToken);
        client.createSubTask(newSubTask(), apiToken);
        server.stop();
    }
    private static Task newTask(){
        return new Task(1, "Посмотреть фильм", "Выбрать фильм", StatusChoice.NEW, LocalDateTime.of(1980, MAY, 16, 3, 6), 3);
    }

    private static Epic newEpic(){
        return new Epic(4, "Бросить курить", "Да просто не курить", StatusChoice.NEW, LocalDateTime.of(2023, MAY, 2, 20, 22),3);
    }

    private static Subtask newSubTask(){
        return new Subtask(6, 4, "Следить за диетой", "Не жрать после 4", StatusChoice.IN_PROGRESS, LocalDateTime.of(2000, Month.NOVEMBER, 1, 20, 22),400);
    }

}
