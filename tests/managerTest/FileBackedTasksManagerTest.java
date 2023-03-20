package managerTest;

import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.nio.file.Path;
import java.time.LocalDateTime;
import static java.util.Calendar.MAY;
import static org.junit.jupiter.api.Assertions.*;
import static tasks.StatusChoice.DONE;
import static tasks.StatusChoice.NEW;

class FileBackedTasksManagerTest {
    private Path path = Path.of("resources/backup.csv");
    TaskManager manager = Managers.getFileManager(path);
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
    void shouldMakeTaskFromString(){
        String str = "2,EPIC,Повеселетиться,Устроить тусу,NEW";
        Epic epic = (Epic) FileBackedTasksManager.fromString(str);
        assertEquals(createEpic().toString(), epic.toString(), "Делает из строчки Task");
    }

    @Test
    void shouldloadFromFile(){
        manager.saveAnyTask(createTask());
        manager.saveAnyTask(createEpic());
        manager.saveAnyTask(createSubtask());
        manager.removeTaskById(1);
        FileBackedTasksManager.loadFromFile(path);
        assertEquals(manager.getHistory(), FileBackedTasksManager.loadFromFile(path).getHistory(), "Истории равны");
    }
}