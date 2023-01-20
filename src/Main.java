import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.StatusChoice;
import tasks.Subtask;
import tasks.Task;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Relax", "Go to bed and sleep", StatusChoice.DONE);
        Task task2 = new Task("Having fun", "Meet with friends", StatusChoice.NEW);
        ArrayList<Subtask> subtasksEpic1 = new ArrayList<>();

        Subtask subtask1Epic1 = new Subtask("Похудеть", "Заняться спортом", "Делать зарядку", StatusChoice.DONE);
        Subtask subtask2Epic1 = new Subtask("Похудеть", "Следить за диетой", "Не жрать после 6", StatusChoice.IN_PROGRESS);
        subtasksEpic1.add(subtask1Epic1);
        subtasksEpic1.add(subtask2Epic1);
        Epic epic1 = new Epic("Похудеть", "Сбросить вес", subtasksEpic1);
        Epic epic2 = new Epic ("Бросить курить", "Да просто не курить");

        //1.
        manager.saveAllTasks(task1);
        manager.saveAllTasks(task2);
        manager.saveAllTasks(subtask1Epic1);
        manager.saveAllTasks(subtask2Epic1);
        manager.saveAllTasks(epic1);
        manager.saveAllTasks(epic2);

        //2.1
        System.out.println(2.1);
        System.out.println(manager.getListOfAllTasks());

        //2.3
        System.out.println(2.3);
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getTaskById(3));
        System.out.println(manager.getTaskById(4));
        System.out.println(manager.getTaskById(5));

        ////5(new).
        System.out.println(5.);
        System.out.println(manager.getHistory());

        //2.4
        System.out.println(2.4);
        System.out.println(manager.createTask(task2));

        //2.5
        System.out.println(2.5);
        Subtask subtask3Epic1 = new Subtask("Похудеть", "Следить за диетой", "Кушать больше фруктов", StatusChoice.DONE);
        manager.updateTasks(subtask3Epic1, 4);



        //2.6
        System.out.println(2.6);
        manager.removeTaskById(1);
        System.out.println(manager.getListOfAllTasks());

        //3.1
        System.out.println(3.1);
        System.out.println(manager.getAllSubtsksOfEpic(5));

        //4
        System.out.println(4);
        manager.getStatusEpic();


        //2.2
        manager.deleteTasks(manager.getListOfAllTasks());
        System.out.println(manager.getListOfAllTasks());
    }
}
