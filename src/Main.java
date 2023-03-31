import client.KVClient;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import server.KVServer;
import tasks.Epic;
import tasks.StatusChoice;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Month;

import static java.util.Calendar.FEBRUARY;
import static java.util.Calendar.MAY;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        /*TaskManager manager1 = Managers.getDefault();
        //Path path = Path.of("resources/backup.csv");
        //TaskManager manager = Managers.getFileManager(path);
        Task task1 = new Task(1, "Посмотреть фильм", "Выбрать фильм", StatusChoice.NEW, LocalDateTime.of(1980, MAY, 16, 3, 6), 3);
        manager1.saveAnyTask(task1);
        Epic epic1 = new Epic(3, "Похудеть", "Сбросить вес", StatusChoice.NEW);
        Epic epic2 = new Epic(4, "Бросить курить", "Да просто не курить", StatusChoice.NEW, LocalDateTime.of(2023, MAY, 2, 20, 22),3);
        manager1.saveAnyTask(epic1);
        manager1.saveAnyTask(epic2);

        Subtask subtask1Epic1 = new Subtask(5, epic1.getId(), "Заняться спортом", "Делать зарядку", StatusChoice.DONE, LocalDateTime.of(2000, Month.SEPTEMBER, 10, 13, 10),50);
        Subtask subtask2Epic1 = new Subtask(6, epic1.getId(), "Следить за диетой", "Не жрать после 6", StatusChoice.IN_PROGRESS, LocalDateTime.of(2023, Month.NOVEMBER, 1, 20, 22),300);
        manager1.saveAnyTask(subtask1Epic1);
        manager1.saveAnyTask(subtask2Epic1);

        manager1.saveSubtasksInEpic(epic1, subtask1Epic1);
        manager1.saveSubtasksInEpic(epic1, subtask2Epic1);
        manager1.setTimeOfEpic(epic1);
        manager1.setTimeOfEpic(epic2);

        System.out.println("All tasks");
        System.out.println(manager1.getListOfAllTasks() + "the end!!!!");

        System.out.println("отсортированный список : ");
        System.out.println(manager1.getPrioritizedTasks());

        Subtask subtask3Epic1 = new Subtask(6, epic1.getId(), "Следить за диетой", "Не жрать после 4", StatusChoice.IN_PROGRESS, LocalDateTime.of(2000, Month.NOVEMBER, 1, 20, 22),400);
        manager1.updateSubtaskInEpic(subtask3Epic1, 6);
        System.out.println("Update" + manager1.getTaskById(6));
        System.out.println("Update ytygbh" + manager1.getAllSubtasksOfEpic(3));
        //fillManager(manager);*/
        startServer();

        KVClient client = new KVClient();
        String apiToken = client.register();
        client.createTask(newTask(), apiToken);
        client.createEpic(newEpic(), apiToken);
        client.createSubTask(newSubTask(), apiToken);
    }

    private static void startServer() throws IOException, InterruptedException {
        KVServer server = new KVServer();
        server.start();
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
