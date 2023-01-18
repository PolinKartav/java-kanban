package manager;

import tasks.Task;
import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    List<Task> listHistoryOfTasks = new ArrayList<>();

    @Override
    public void add(Task task){

    }
    
    @Override
    public List<Task> getHistory(){
        List<Task> listHistoryOf10Tasks = new ArrayList<>();
        for(int i = 0; i < listHistoryOfTasks.size(); i++) {
            if(i >= listHistoryOfTasks.indexOf(0) && i < listHistoryOfTasks.indexOf(10)){
                listHistoryOf10Tasks.add(listHistoryOfTasks.get(i));
            }
            else listHistoryOfTasks.remove(i);
        }
        return listHistoryOf10Tasks;
    }
}
