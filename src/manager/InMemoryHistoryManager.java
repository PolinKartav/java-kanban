package manager;

import tasks.Task;
import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    static private List<Task> listHistoryOfTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        listHistoryOfTasks.add(task);
    }

    @Override
    public List<Task> getHistoryTasks() {
        int size = listHistoryOfTasks.size();
        if (size <= 10) {
            return listHistoryOfTasks;
        } else {
            for (int i = 0; i < size - 10; i++) {
                listHistoryOfTasks.remove(0);
            }
            return listHistoryOfTasks;
        }
    }
}
