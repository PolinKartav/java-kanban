package manager;

import server.HttpTaskManager;
import tasks.Task;

import java.nio.file.Path;
import java.util.List;

public class Managers {

    public static TaskManager getDefault(){

        return new InMemoryTaskManager();
    }

    public static HttpTaskManager getDefaultTaskManager(){
        return new HttpTaskManager();
    }

    public static TaskManager getFileManager(Path path) {
        //return new FileBackedTasksManager(path);
        return FileBackedTasksManager.loadFromFile(path);
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
