package managerTest;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.util.Calendar.MAY;
import static org.junit.jupiter.api.Assertions.*;
import static tasks.StatusChoice.DONE;
import static tasks.StatusChoice.NEW;

public class InMemoryHistoryManagerTest <T extends HistoryManager>{

    protected HistoryManager manager;
    protected TaskManager managerTask;
    @BeforeEach
    public void beforeEach(){
        manager = new InMemoryHistoryManager();
        managerTask = new InMemoryTaskManager();
    }
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

    @Test
    void shouldGetHistory(){
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.add(createTask());

        manager.add(createTask());
        assertEquals(allTasks.toString(), manager.getHistoryTasks().toString(), "Task добовляется в историю");
        manager.remove(createTask());
        assertNotEquals(allTasks, manager.getHistoryTasks(), "Неправильная история");
        allTasks.clear();
        assertEquals(allTasks, manager.getHistoryTasks(), "Возвращает пустой список");
    }

    @Test
    void shouldClearHistory(){
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.add(createTask());
        allTasks.clear();
        manager.add(createTask());
        manager.clearHistory();
        assertEquals(allTasks, manager.getHistoryTasks(), "Список очищается");
    }

    @Test
    void shouldRemoveTaskInHistory(){
        Task task = createTask();
        manager.add(task);
        Subtask subtask = createSubtask();
        manager.add(subtask);
        manager.remove(task);
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.add(createSubtask());
        assertEquals(allTasks.toString(), manager.getHistoryTasks().toString(), "Удаляние Task происходит верно");
        manager.remove(subtask);
        assertNotEquals(allTasks, manager.getHistoryTasks().toString(), "Удаляется неправильно");
        allTasks.clear();
        assertEquals(allTasks, manager.getHistoryTasks(), "Список пуст");
    }

    @Test
    void shouldAddTaskInHistory(){
        ArrayList<Task> allTasks = new ArrayList<>();
        Task task = createTask();
        assertEquals(allTasks.toString(), manager.getHistoryTasks().toString(), "Добавления не было совершенно, список пустой");
        manager.add(task);
        allTasks.add(createTask());
        assertEquals(allTasks.toString(), manager.getHistoryTasks().toString(), "Добавление Task происходит верно");
        manager.add(createSubtask());
        assertNotEquals(allTasks.toString(), manager.getHistoryTasks(), "Списки не равны");
    }
}
