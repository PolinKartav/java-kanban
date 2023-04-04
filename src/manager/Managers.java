package manager;


import server.HttpTaskManager;
import server.HttpTaskServer;

import java.io.IOException;
import java.nio.file.Path;

public class Managers {

    public static TaskManager getDefault(){

        return new InMemoryTaskManager();
    }

    public static TaskManager getFileManager(Path path) {
        return new FileBackedTasksManager(path);
        //return FileBackedTasksManager.loadFromFile(path);
    }

    public static HttpTaskServer getDefaultHttpTaskServer(HttpTaskManager manager) throws IOException {
        return new HttpTaskServer(manager);
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
