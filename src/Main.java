import manager.InMemoryTaskManager;
import manager.Managers;
import tasks.Epic;
import tasks.StatusChoice;
import tasks.Subtask;
import tasks.Task;
public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = (InMemoryTaskManager) Managers.getDefault();

        Task task1 = new Task("Relax", "Go to bed and sleep", StatusChoice.DONE);
        Task task2 = new Task("Having fun", "Meet with friends", StatusChoice.NEW);

        //creation 2 epics
        Epic epic1 = new Epic("Похудеть", "Сбросить вес");
        Epic epic2 = new Epic ("Бросить курить", "Да просто не курить");
        manager.saveAnyTask(epic1);
        manager.saveAnyTask(epic2);

        //creation 3 subtasks of the  first epic
        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "Заняться спортом", "Делать зарядку", StatusChoice.DONE);
        Subtask subtask2Epic1 = new Subtask(epic1.getId(), "Следить за диетой", "Не жрать после 6", StatusChoice.IN_PROGRESS);
        Subtask subtask4Epic1 = new Subtask(epic1.getId(), "Уменьшить колличество сахара", "Пить кофе без сиропа", StatusChoice.IN_PROGRESS);
        manager.saveAnyTask(subtask1Epic1);
        manager.saveAnyTask(subtask2Epic1);
        manager.saveAnyTask(subtask4Epic1);

        //save Subtasks in List of the Epic
        manager.saveSubtasksInEpic(epic1,subtask1Epic1);
        manager.saveSubtasksInEpic(epic1,subtask2Epic1);
        manager.saveSubtasksInEpic(epic1,subtask4Epic1);

        //2.1
        System.out.println(2.1);
        System.out.println(manager.getListOfAllTasks());

        //2.3 get task by id
        System.out.println(2.3);
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getTaskById(3));
        System.out.println(manager.getTaskById(4));
        System.out.println(manager.getTaskById(5));

        //get history
        System.out.println("History");
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getTaskById(3);
        manager.getTaskById(1);
        manager.getTaskById(4);
        System.out.println(manager.getHistory());
        manager.removeTaskById(1);
        System.out.println(manager.getHistory());

        //2.5 update the object
        System.out.println(2.5);
        Subtask subtask3Epic1 = new Subtask(epic1.getId(), "Следить за диетой", "Кушать больше фруктов", StatusChoice.DONE);
        manager.updateTasks(subtask3Epic1, 1);

        //2.6 delete task by id
        System.out.println(2.6);
        manager.removeTaskById(5);
        System.out.println(manager.getListOfAllTasks());

        //3.1
        System.out.println(3.1);
        System.out.println(manager.getAllSubtsksOfEpic(1));

        //4
        System.out.println(4);
        manager.getStatusEpic();

        //2.2
        System.out.println(2.2);
        manager.deleteAllTasks(manager.getListOfAllTasks());
        System.out.println(manager.getListOfAllTasks());
    }
}
