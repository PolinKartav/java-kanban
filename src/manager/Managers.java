package manager;

import server.HttpTaskManager;
import server.HttpTaskServer;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Managers {

    public static TaskManager getDefault(){

        return new InMemoryTaskManager();
    }

    public static HttpTaskServer getDefaultHttpTaskServer(HttpTaskManager manager) throws IOException {
        return new HttpTaskServer(manager);
    }

    public static TaskManager getFileManager(Path path) {
        //return new FileBackedTasksManager(path);
        return FileBackedTasksManager.loadFromFile(path);
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
