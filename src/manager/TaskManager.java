package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    //1. Метод для хранения всех задач
    // 2.4 Создание. Сам объект должен передаваться в качестве параметра;
    void saveAnyTask(Task o);

    //2.1 Получение списка всех задач.
    List<Task> getListOfAllTasks();

    //2.2 Удаление всех задач;
    void deleteAllTasks();
    //2.3 Получение по идентификатору;
    Task getTaskById(int id);

    //2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра;
    void updateTasks(Task o, int id);

    //обновление Subtasks в Epic
     void updateSubtaskInEpic(Subtask subtask, int id);

    //2.6 Удаление по идентификатору.
    void removeTaskById(Integer id);

    //3. Дополнительные методы:
    //3.1 Получение списка всех подзадач определённого эпика.
    List<Subtask> getAllSubtasksOfEpic(int id);

    //4. Метод для управления статусом для эпик задач.
    void getStatusEpic();

    //5(new).возвращать последние 10 просмотренных задач
    List<Task> getHistory();
    void saveSubtasksInEpic(Epic epic, Subtask task);

    //Расчитать время для выполнения Эпика
    void setTimeOfEpic(Epic epic);

    void removeAllSubtasksInEpic(int id);


    ArrayList<Task> getPrioritizedTasks();
}
