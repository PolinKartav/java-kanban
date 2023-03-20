package managerTest;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.StatusChoice;
import tasks.Subtask;
import tasks.Task;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import static java.util.Calendar.MAY;
import static org.junit.jupiter.api.Assertions.*;
import static tasks.StatusChoice.DONE;
import static tasks.StatusChoice.NEW;

class InMemoryTaskManagerTest<T extends TaskManager>{
    protected TaskManager manager = new InMemoryTaskManager();

    protected Task createTask(){
        Task task = new Task(1,"Вкусно покушать", "Приготовить салат", NEW, LocalDateTime.of(1980, MAY, 16, 3, 6), 3 );
        return task;
    }
    protected Epic createEpic(){
        Epic epic = new Epic(2,"Повеселетиться", "Устроить тусу", NEW );
        return epic;
    }
    protected Subtask createSubtask(){
        Subtask subtask = new Subtask(3, 2, "Позвать компанию", "Позвонить друзьям", DONE, LocalDateTime.of(2000, MAY, 16, 3, 6), 19);
        return subtask;
    }

    // getListOfAllTasks
    @Test
    void shouldGetList(){
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.add(createTask());
        manager.saveAnyTask(createTask());
        assertEquals(allTasks.get(0).toString(), manager.getListOfAllTasks().get(0).toString(), "Задача создается и добавляется");
    }

    @Test
    void shouldGetEmptyList(){
        assertEquals(null, manager.getListOfAllTasks(), "Список пустой");
    }

    @Test
    void shouldDeleteAllTasks(){
        manager.saveAnyTask(createTask());
        manager.saveAnyTask(createEpic());
        manager.saveAnyTask(createSubtask());
        manager.deleteAllTasks();
        assertEquals(null, manager.getListOfAllTasks(), "Все задачи удаляются");
    }

    @Test
    void shouldGetTaskById(){
        manager.saveAnyTask(createTask());
        manager.saveAnyTask(createEpic());
        assertEquals(createTask().toString(), manager.getTaskById(1).toString(), "Задача возвращается по id");
        assertEquals(null, manager.getTaskById(3), "Такой задачи нет");
    }

    @Test
    void shouldRemoveTaskById(){
        manager.saveAnyTask(createTask());
        manager.saveAnyTask(createEpic());
        manager.saveAnyTask(createSubtask());

        manager.removeTaskById(1);
        assertEquals(2, manager.getListOfAllTasks().size(), "Размер списка после удаления верен");
        manager.removeTaskById(2);
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.add(createSubtask());
        assertEquals(allTasks.toString(), manager.getListOfAllTasks().toString(), "Удаление правильных задач");
        manager.removeTaskById(5);
        assertEquals(allTasks.toString(), manager.getListOfAllTasks().toString(), "Задачи с таким id не существует");
    }
    
    @Test
    void shouldRemoveAllSubtasksInEpic(){
        manager.saveAnyTask(createEpic());
        manager.saveAnyTask(createSubtask());
        manager.saveAnyTask(new Subtask(4, 2, "Одеться", "Выбрать наряд", StatusChoice.IN_PROGRESS, LocalDateTime.of(2000, Month.MAY, 16, 4, 30), 40));
        manager.removeAllSubtasksInEpic(2);
        assertEquals(null, manager.getAllSubtasksOfEpic(2), "Удаление subtasks у Epic");
        manager.saveAnyTask(new Epic(6,"Повеселетиться", "Устроить тусу", NEW ));
        manager.removeAllSubtasksInEpic(6);
        assertEquals(null, manager.getAllSubtasksOfEpic(6), "у Epic нет Subtasks");
        manager.removeAllSubtasksInEpic(7);
        assertEquals(null, manager.getAllSubtasksOfEpic(7), "Нет такого Epica");
    }

    @Test
    void shouldUpdateTask(){
        manager.saveAnyTask(createTask());
        Task task = new Task(1,"Выпить", "Приготовить напиток", NEW, LocalDateTime.of(2023, MAY, 10, 20, 15), 20);
        manager.updateTasks(task, 1);
        assertEquals(task.toString(), manager.getTaskById(1).toString(), "Обновление задачи");
        manager.saveAnyTask(createEpic());
        assertNotEquals(task.toString(), manager.getTaskById(2).toString(), "Задачи не соотвествуют");
        manager.updateTasks(task, 3);
        assertEquals(null, manager.getTaskById(3), "Задачи не существует");
    }

    @Test
    void shouldUpdateSubtaskInEpic(){
        Epic epic = createEpic();
        Subtask subtask = createSubtask();
        manager.saveAnyTask(epic);
        manager.saveAnyTask(subtask);
        manager.saveSubtasksInEpic(epic,subtask);

        Subtask subtask1 = new Subtask(3, 2, "Позвать компанию", "Позвонить друзьям", NEW, LocalDateTime.of(2030, Month.APRIL, 16, 3, 6), 19);
        manager.updateSubtaskInEpic(subtask1, 3);
        assertEquals(subtask1.toString(), manager.getTaskById(3).toString(), "Обновление задачи");
        assertNotEquals(subtask1.toString(), manager.getTaskById(2).toString(), "Задачи не соотвествуют");
        manager.updateSubtaskInEpic(subtask1, 4);
        assertEquals(null, manager.getTaskById(4), "Задачи не существует");
    }

    @Test
    void shouldGetAllAubtasksIfEpic(){
        Epic epic = createEpic();
        Subtask subtask = createSubtask();
        manager.saveAnyTask(epic);
        manager.saveAnyTask(subtask);
        manager.saveSubtasksInEpic(epic,subtask);
        manager.getAllSubtasksOfEpic(2);
        ArrayList<Task> listSubtasks = new ArrayList<>();
        listSubtasks.add(subtask);
        assertEquals(listSubtasks.toString(), manager.getAllSubtasksOfEpic(2).toString(), "Список верен");

        Epic epic1 = new Epic(5,"Повеселетиться", "Устроить тусу", NEW );
        assertEquals(null, manager.getAllSubtasksOfEpic(5), "Список пуст");
        assertEquals(null, manager.getAllSubtasksOfEpic(6), "такого Epic не сушествет");
    }

    @Test
    void shouldGetStatusOfEpic(){
        Epic epic = createEpic();
        Subtask subtask = createSubtask();
        manager.saveAnyTask(epic);
        manager.saveAnyTask(subtask);
        manager.saveSubtasksInEpic(epic,subtask);
        manager.getStatusEpic();
        assertEquals(DONE, manager.getTaskById(2).getStatus(), "Статус обновлен");
        assertNotEquals(NEW, manager.getTaskById(2).getStatus(), "Статус не верен");
    }
    @Test
    void shouldGetHistory(){
         InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
         manager.getHistory();
         assertEquals(historyManager.getHistoryTasks(),manager.getHistory(), "История вытаскивается правильно");
         assertNotEquals("[]", manager.getHistory(), "История не соотвествует");
         manager.deleteAllTasks();
         assertEquals("[]", historyManager.getHistoryTasks().toString(), "История пуста");
    }

    @Test
    void shouldSaveSubtasksInEpic(){
        Epic epic = createEpic();
        Subtask subtask = createSubtask();
        manager.saveAnyTask(epic);
        manager.saveAnyTask(subtask);
        manager.saveSubtasksInEpic(epic, subtask);
        ArrayList<Task> listSubtasks = new ArrayList<>();
        listSubtasks.add(subtask);
        assertEquals(listSubtasks.toString(), manager.getAllSubtasksOfEpic(2).toString(), "Список Subtasks у Epic");
        Epic epic1 = new Epic(6,"Повеселетиться", "Устроить тусу", DONE);
        manager.saveAnyTask(epic1);
        assertEquals(null, manager.getAllSubtasksOfEpic(6), "Пустой лист Subtasks");
        Subtask subtask1 = new Subtask(3, 2, "Позвать компанию", "Позвонить друзьям", DONE, LocalDateTime.of(2000, MAY, 16, 3, 6), 10);
        manager.saveAnyTask(subtask1);
        manager.saveSubtasksInEpic(epic1, subtask1);
        assertNotEquals(listSubtasks.toString(), manager.getAllSubtasksOfEpic(6), "Неправильный вывод списка Subtasks");
    }

    @Test
    void shouldSetTimeOfEpic(){
        Epic epic = createEpic();
        Subtask subtask = createSubtask();
        manager.saveAnyTask(epic);
        manager.saveAnyTask(subtask);
        manager.saveSubtasksInEpic(epic, subtask);
        Subtask subtask1 = new Subtask(3, 2, "Позвать компанию", "Позвонить друзьям", DONE, LocalDateTime.of(2034, MAY, 16, 3, 6), 10);
        manager.saveAnyTask(subtask1);
        manager.saveSubtasksInEpic(epic, subtask1);
        manager.setTimeOfEpic(epic);
        assertEquals(LocalDateTime.of(2000, MAY, 16, 3, 6), epic.getStartTime(), "Начало выполнение epic правильно");
        assertEquals(LocalDateTime.of(2034, MAY, 16, 3, 16), epic.getEndTime(), "Завершение выполнение epic правильно");
        assertEquals(29, epic.getDuration(), "длительность выполнения epic правильна");

        assertNotEquals(LocalDateTime.of(2009, MAY, 16, 3, 6), epic.getStartTime(), "Начало выполнение epic неправильно");
        assertNotEquals(LocalDateTime.of(2035, MAY, 16, 3, 16), epic.getEndTime(), "Завершение выполнение epic неправильно");
        assertNotEquals(10, epic.getDuration(), "Длительность выполнения epic неправильна");

        Epic epic1 = new Epic(6,"Повеселетиться", "Устроить тусу", DONE);
        manager.saveAnyTask(epic1);
        manager.setTimeOfEpic(epic1);
        assertEquals(null, epic1.getStartTime(), "Время начало выполнения epic не указана");
        assertEquals(null, epic1.getEndTime(), "Время завершения выполнения epic не указана");
        assertEquals(0, epic1.getDuration(), "Время длительности выполнения epic не указана");
    }

    @Test
    void shouldGetPrioritizedTasks(){
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.add(createTask());
        allTasks.add(createSubtask());
        allTasks.add(createEpic());

        assertNotEquals(allTasks.toString(), manager.getPrioritizedTasks(), "Список неверно отсортирован");
        assertEquals(null, manager.getPrioritizedTasks(), "Список пуст");

        Epic epic = createEpic();
        Subtask subtask = createSubtask();
        manager.saveAnyTask(epic);
        manager.saveAnyTask(subtask);
        manager.saveAnyTask(createTask());
        manager.saveSubtasksInEpic(epic, subtask);
        assertEquals(createTask().toString(), manager.getPrioritizedTasks().get(0).toString(), "Список правильно отсортирован");
        assertEquals(createSubtask().toString(), manager.getPrioritizedTasks().get(1).toString(), "Список правильно отсортирован");
    }








}
