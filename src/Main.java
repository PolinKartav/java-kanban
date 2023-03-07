import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.StatusChoice;
import tasks.Subtask;
import tasks.Task;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("resources/backup.csv");
        TaskManager manager = Managers.getFileManager(path);

        fillManager(manager);
    }

    //метод для тестов
    static void fillManager(TaskManager manager) {
        Task task1 = new Task(1, "Relax", "Go to bed and sleep", StatusChoice.DONE);
        Task task2 = new Task(2, "Having fun", "Meet with friends", StatusChoice.NEW);
        manager.saveAnyTask(task1);
        manager.saveAnyTask(task2);

        //creation 2 epics
        Epic epic1 = new Epic(3, "Похудеть", "Сбросить вес", StatusChoice.NEW);
        Epic epic2 = new Epic(4, "Бросить курить", "Да просто не курить", StatusChoice.NEW);
        Epic epic3 = new Epic(7, "Отдохнуть", "посмотреть сериал", StatusChoice.NEW);
        manager.saveAnyTask(epic1);
        manager.saveAnyTask(epic2);
        manager.saveAnyTask(epic3);

        //creation 3 subtasks of the  first epic
        Subtask subtask1Epic1 = new Subtask(5, epic1.getId(), "Заняться спортом", "Делать зарядку", StatusChoice.DONE);
        Subtask subtask2Epic1 = new Subtask(6, epic1.getId(), "Следить за диетой", "Не жрать после 6", StatusChoice.IN_PROGRESS);
        Subtask subtask4Epic1 = new Subtask(7, epic1.getId(), "Уменьшить колличество сахара", "Пить кофе без сиропа", StatusChoice.IN_PROGRESS);
        manager.saveAnyTask(subtask1Epic1);
        manager.saveAnyTask(subtask2Epic1);
        manager.saveAnyTask(subtask4Epic1);

        //save Subtasks in List of the Epic
        manager.saveSubtasksInEpic(epic1, subtask1Epic1);
        manager.saveSubtasksInEpic(epic1, subtask2Epic1);
        manager.saveSubtasksInEpic(epic1, subtask4Epic1);

        System.out.println("All tasks");
        System.out.println(manager.getListOfAllTasks() + "the end!!!!");
        Task task0 = manager.getTaskById(1);
        System.out.println("Task 0 is : " + task0);
        //История хранится в менеджере, чтобы досать историю, как я поняла, нужно использовать текущего менеджера по ТЗ
        //HistoryManager historyManager = Managers.getDefaultHistory();
        System.out.println("!!!! History");
        System.out.println(manager.getHistory());
    }
}
