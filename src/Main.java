import manager.TaskManage;
import tasks.Epic;
import tasks.StatusVarion;
import tasks.Subtask;
import tasks.Task;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        TaskManage manager = new TaskManage();
        StatusVarion status = new StatusVarion();

        Task task1 = new Task("Relax", "Go to bed and sleep", status.getStatus1());
        Task task2 = new Task("Having fun", "Meet with friends", status.getStatus2());
        ArrayList<Subtask> subtasksEpic1 = new ArrayList<>();

        Subtask subtask1Epic1 = new Subtask("Похудеть", "Заняться спортом", "Делать зарядку", status.getStatus1());
        Subtask subtask2Epic1 = new Subtask("Похудеть", "Следить за диетой", "Не жрать после 6", status.getStatus1());
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
        System.out.println(manager.getTaskById(4));
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getTaskById(3));
        System.out.println(manager.getTaskById(5));

        System.out.println("2.4");//2.4
        System.out.println(manager.createTask(task2));

        System.out.println("2.5");//2.5
        Subtask subtask3Epic1 = new Subtask("Похудеть", "Следить за диетой", "Не жрать после 4", status.getStatus3());
        manager.updateTasks(subtask3Epic1, 4);
        System.out.println(manager.getEpicSave());
        System.out.println(manager.getSubtaskSave());


        System.out.println("2.6");//2.6
        manager.removeTaskById(1);
        System.out.println(manager.getEpicSave());
        System.out.println(manager.getListOfAllTasks());

        System.out.println(3.1);//3.1
        System.out.println(manager.getAllSubtsksOfEpic(5));

        System.out.println("4");//4
        manager.getStatusEpic();
        System.out.println(manager.getEpicSave());

        //2.2
        manager.deleteTasks(manager.getListOfAllTasks());
        System.out.println(manager.getListOfAllTasks());
    }
}
