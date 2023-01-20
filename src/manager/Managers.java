package manager;

import tasks.Task;
import java.util.List;

public class Managers {

    public static TaskManager getDefault(){

        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}