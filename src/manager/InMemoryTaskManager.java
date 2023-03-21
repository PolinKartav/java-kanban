package manager;

import tasks.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager{
    Map<Integer, Task> taskSave;
    Map<Integer, Epic> epicSave;
    Map<Integer, Subtask> subtaskSave;
    TreeSet<Task> tree;



    public InMemoryHistoryManager historyManager;

    public InMemoryTaskManager() {
        taskSave = new HashMap<>();
        epicSave = new HashMap<>();
        subtaskSave = new HashMap<>();
        historyManager = new InMemoryHistoryManager();
        tree = new TreeSet<>();
    }


    //1. Сохрание любой наски в нужную HashMap
    //2.4 Создание. Сам объект должен передаваться в качестве параметра;
    @Override
    public void saveAnyTask(Task o) {

        if (o instanceof Epic) {
            epicSave.put(o.getId(), (Epic) o);
        } else if (o instanceof Subtask) {
            subtaskSave.put(((Subtask) o).getId(), (Subtask) o);
        } else if (o instanceof Task) {
            taskSave.put(((Task) o).getId(), (Task) o);
        }
    }

    //2.1 Получение списка всех задач.
    @Override
    public ArrayList<Task> getListOfAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();

        if (!taskSave.isEmpty()) {
            allTasks.addAll(taskSave.values());
        }
        if (!epicSave.isEmpty()) {
            allTasks.addAll(epicSave.values());
        }
        if (!subtaskSave.isEmpty()) {
            allTasks.addAll(subtaskSave.values());
        }
        if (!allTasks.isEmpty()) {
            return allTasks;
        } else return null;
    }

    //2.2 Удаление всех задач;
    @Override
    public void deleteAllTasks() {
        taskSave.clear();
        epicSave.clear();
        subtaskSave.clear();
        historyManager.clearHistory();
    }

    //2.3 Получение по идентификатору;
    @Override
    public Task getTaskById(int id) {
        Task taskById = null;

        if (taskSave.get(id) != null) {
            taskById = taskSave.get(id);
            historyManager.add(taskById);
        } else if (epicSave.get(id) != null) {
            taskById = epicSave.get(id);
            historyManager.add(taskById);
        } else if (subtaskSave.get(id) != null) {
            taskById = subtaskSave.get(id);
            historyManager.add(taskById);
        }return taskById;
    }

    //2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра;
    @Override
    public void updateTasks(Task o, int id) {

        if (o instanceof Epic) {
            Epic epic = (Epic) o;
            epic.setId(id);
            epicSave.replace(id, epic);
        } else if (o instanceof Subtask) {
            Subtask subtask = (Subtask) o;
            subtask.setId(id);
            subtaskSave.replace(id, subtask);
            updateSubtaskInEpic(subtask, id);
        } else if (o instanceof Task) {
            Task task = (Task) o;
            task.setId(id);
            taskSave.replace(id, task);
        }
    }

    //обновление Subtasks в Epic
    @Override
    public void updateSubtaskInEpic(Subtask subtask, int id) {

        for (Map.Entry<Integer, Epic> entry : epicSave.entrySet()) {
            Epic ep = entry.getValue();
            for (int i = 0; i < ep.subTasks.size(); i++) {
                if (ep.subTasks.get(i).getId() == id) {
                    ep.subTasks.remove(i);
                    ep.subTasks.add(subtask);
                    subtaskSave.remove(i);
                    subtaskSave.put(id, subtask);
                    epicSave.put(entry.getKey(), ep);
                    return;
                }
            }
        }
    }

    //2.6 Удаление по идентификатору.
    @Override
    public void removeTaskById(Integer id) {

        if (taskSave.containsKey(id)) {
            historyManager.remove(taskSave.get(id));
            taskSave.remove(id);
        }else if (epicSave.containsKey(id)) {
            historyManager.remove(epicSave.get(id));
            removeAllSubtasksInEpic(id);
            epicSave.remove(id);
        }else if (subtaskSave.containsKey(id)) {
            epicSave.get(subtaskSave.get(id).getParentEpicId()).deleteSubtask(subtaskSave.get(id));
            historyManager.remove(subtaskSave.get(id));
            subtaskSave.remove(id);
        }
    }

    @Override
    public void removeAllSubtasksInEpic(int id) {
        if(epicSave.containsKey(id)) {
            List<Subtask> tasks = epicSave.get(id).subTasks;
            if (tasks.isEmpty()) {
                return;
            } else {
                while (!tasks.isEmpty()) {
                    removeTaskById(tasks.get(0).getId());
                }
            }
        } else return;
    }

    //3. Дополнительные методы:
    //3.1 Получение списка всех подзадач определённого эпика.
    @Override
    public List<Subtask> getAllSubtasksOfEpic(int id) {

        if (epicSave.containsKey(id)) {
            Epic epic = epicSave.get(id);
            if (!epic.subTasks.isEmpty()) {
                return epic.subTasks;
            }
        }

        return null;
    }

    //4. Метод для управления статусом для эпик задач.
    @Override
    public void getStatusEpic() {
        int newC = 0;
        int doneC = 0;

        for (Map.Entry<Integer, Epic> entry : epicSave.entrySet()) {
            Epic ep = entry.getValue();

            if (ep.subTasks.isEmpty()) {
                ep.setStatus(StatusChoice.NEW);
                epicSave.put(entry.getKey(), ep);
                return;
            }

            for (int i = 0; i < ep.subTasks.size(); i++) {
                if (ep.subTasks.get(i).getStatus() == StatusChoice.NEW) {
                    newC++;
                } else if (ep.subTasks.get(i).getStatus() == StatusChoice.DONE) {
                    doneC++;
                }
            }
            if (newC == ep.subTasks.size()) {
                ep.setStatus(StatusChoice.NEW);
            } else if (doneC == ep.subTasks.size()) {
                ep.setStatus(StatusChoice.DONE);
            } else ep.setStatus(StatusChoice.IN_PROGRESS);
            epicSave.put(entry.getKey(), ep);
        }
    }
    /*Если у эпика нет подзадач или все они имеют статус NEW | DONE, то статус должен быть NEW | DONE.
    Во всех остальных случаях статус должен быть IN_PROGRESS.*/

    //5.возвращать историю просмотра задач
    @Override
    public List<Task> getHistory() {

        return historyManager.getHistoryTasks();
    }

    @Override
    public void saveSubtasksInEpic(Epic epic, Subtask task) {

        epic.saveSubtasksInList(task);
    }

   //Расчитать время для выполнения Эпика
    @Override
    public void setTimeOfEpic(Epic epic) {
        List<Subtask> subtasks = getAllSubtasksOfEpic(epic.getId());
        if(subtasks != null) {
            LocalDateTime startOfSubtask = subtasks.get(0).getStartTime();
            LocalDateTime endOfSubtask = subtasks.get(0).getEndTime();
            long duration = 0;
            for (Subtask subtask : subtasks) {
                if (subtask.getStartTime().isBefore(startOfSubtask)) {
                    startOfSubtask = subtask.getStartTime();

                }
                if (subtask.getEndTime().isAfter(endOfSubtask)) {
                    endOfSubtask = subtask.getEndTime();
                }
                duration = duration + subtask.getDuration();
            }
            epic.setStartTime(startOfSubtask);
            epic.setEndTime(endOfSubtask);
            epic.setDuration(duration);
        } else {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(0);
        }
    }


    //метод, который возвращает список задач по времени старта
    public ArrayList<Task> getPrioritizedTasks(){

        if(!taskSave.isEmpty()){
            for(Task task :taskSave.values()){
                tree.add(new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus(), task.getStartTime(), task.getDuration()));
            }
        }
        if(!subtaskSave.isEmpty()){

            for(Subtask subtask : subtaskSave.values()){
                tree.add(new Subtask(subtask.getId(), subtask.getParentEpicId(), subtask.getName(), subtask.getDescription(), subtask.getStatus(),
                        subtask.getStartTime(), subtask.getDuration()));
            }
        } else return null;

        return new ArrayList<Task>(tree);
    }
    
}
