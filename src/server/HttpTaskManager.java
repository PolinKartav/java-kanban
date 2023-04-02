package server;

import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager implements TaskManager{

    private TaskManager manager = Managers.getDefault();

    @Override
    public void saveAnyTask(Task o) {
        manager.saveAnyTask(o);
    }

    @Override
    public List<Task> getListOfAllTasks() {
        return manager.getListOfAllTasks();
    }

    @Override
    public void deleteAllTasks() {
        manager.deleteAllTasks();
    }

    @Override
    public Task getTaskById(int id) {
        return manager.getTaskById(id);
    }

    @Override
    public void updateTasks(Task o, int id) {
        manager.updateTasks(o, id);
    }

    @Override
    public void updateSubtaskInEpic(Subtask subtask, int id) {
        manager.updateSubtaskInEpic(subtask, id);
    }

    @Override
    public void removeTaskById(Integer id) {
        manager.removeTaskById(id);
    }

    @Override
    public List<Subtask> getAllSubtasksOfEpic(int id) {
        return manager.getAllSubtasksOfEpic(id);
    }

    @Override
    public void getStatusEpic() {
        manager.getStatusEpic();
    }

    @Override
    public List<Task> getHistory() {
        return manager.getHistory();
    }

    @Override
    public void saveSubtasksInEpic(Epic epic, Subtask task) {
        manager.saveSubtasksInEpic(epic, task);
    }

    @Override
    public void setTimeOfEpic(Epic epic) {
        manager.setTimeOfEpic(epic);
    }

    @Override
    public void removeAllSubtasksInEpic(int id) {
        manager.removeAllSubtasksInEpic(id);
    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        return manager.getPrioritizedTasks();
    }
}
