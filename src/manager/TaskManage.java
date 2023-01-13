package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManage {
    private static int ID = 0;
    HashMap<Integer, Task> taskSave = new HashMap<>();
    HashMap<Integer, Epic> epicSave = new HashMap<>();
    HashMap<Integer, Subtask> subtaskSave = new HashMap<>();

    public static  int getId(){
        return ID;
    }
     public static void setId(int id){
        ID = id;
    }

    public HashMap<Integer, Task> getTaskSave(){
        return taskSave;
    }
    public HashMap<Integer, Epic> getEpicSave(){
        return epicSave;
    }
    public HashMap<Integer, Subtask> getSubtaskSave(){
        return subtaskSave;
    }

    //1. Метод для хранения всех задач
    public void saveAllTasks(Object o){
        if(o instanceof Epic){
            epicSave.put(((Epic) o).getId(), (Epic) o);
        }
        else if(o instanceof Subtask){
            subtaskSave.put(((Subtask) o).getId(), (Subtask) o);
        }
        else if(o instanceof Task){
            taskSave.put(((Task) o).getId(), (Task) o);
        }
    }

    //2.1 Получение списка всех задач.
    public ArrayList<Object> getListOfAllTasks(){
        ArrayList<Object> allTasks = new ArrayList();
        allTasks.add(taskSave);
        allTasks.add(epicSave);
        allTasks.add(subtaskSave);
        return allTasks;
    }

    //2.2 Удаление всех задач;
    public void deleteTasks(ArrayList<Object> list){
        list.clear();
        taskSave.clear();
        epicSave.clear();
        subtaskSave.clear();
    }

    //2.3 Получение по идентификатору;
    public Object getTaskById(int id){
        Object taskById = null;

        if (taskSave.get(id) != null){
            taskById = taskSave.get(id);
        }
        else if (epicSave.get(id) != null){
            taskById = epicSave.get(id);
        }
        else if (subtaskSave.get(id) != null){
            taskById = subtaskSave.get(id);
        }
        return taskById;
    }

    //2.4 Создание. Сам объект должен передаваться в качестве параметра;
    public Object createTask(Object o){
        if(o instanceof Epic){
            Epic epic = (Epic) o;
            return epic;
        }
        else if(o instanceof Subtask){
            Subtask subtask = (Subtask) o;
            return subtask;
        }
        else if(o instanceof Task){
            Task task = (Task) o;
            return task;
        }
        else return null;
    }

    //2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра;
    public void updateTasks(Object o, int id){
        if(o instanceof Epic){
            Epic epic = (Epic) o;
            epic.setId(id);
            epicSave.replace(id, epic);
        }
        else if(o instanceof Subtask) {
            Subtask subtask = (Subtask) o;
            subtask.setId(id);
            subtaskSave.replace(id, subtask);
            updateSubtaskInEpic(subtask, id);
        }
        else if(o instanceof Task){
            Task task = (Task) o;
            task.setId(id);
            taskSave.replace(id, task);
        }
    }
    //обновление Subtasks в Epic
    private void updateSubtaskInEpic(Subtask subtask, int id){
        for(Map.Entry<Integer, Epic> entry : epicSave.entrySet()){
            Epic ep = entry.getValue();
            for (int i = 0; i < ep.subTasks.size();i++) {
                if (ep.subTasks.get(i).getId() == id) {
                    ep.subTasks.remove(i);
                    ep.subTasks.add(subtask);
                    epicSave.put(entry.getKey(),ep);
                    return;
                }
            }
        }
    }

    //2.6 Удаление по идентификатору.
    public void removeTaskById(Integer id){
        for (Object taskId: taskSave.keySet()){
            if(id == taskId){
                taskSave.remove(id);
                break;
            }
        }
        for (Object taskId : epicSave.keySet()){
            if(id == taskId){
                epicSave.remove(id);
                break;
            }
        }
        for (Object taskId: subtaskSave.keySet()){
            if(id == taskId){
                subtaskSave.remove(id);
                removeSubtasksInEpic(id);
                break;
            }
        }
    }
    //удаление Subtasks в Epic
    private void removeSubtasksInEpic(int id){
        for(Map.Entry<Integer, Epic> entry : epicSave.entrySet()){
            Epic ep = entry.getValue();
            for (int i = 0; i < ep.subTasks.size();i++) {
                if (ep.subTasks.get(i).getId() == id) {
                    ep.subTasks.remove(i);
                    epicSave.put(entry.getKey(),ep);
                    return;
                }
            }
        }
    }

    //3. Дополнительные методы:
    //3.1 Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> getAllSubtsksOfEpic(int id){
        if(epicSave.isEmpty()) {
            return null;
        }
        Epic epic = epicSave.get(id);
        if(epic.subTasks.isEmpty()){
            return null;
        }
        return epic.subTasks;
    }

    //4. Метод для управления статусом для эпик задач.
    public void getStatusEpic(){
        int newC = 0;
        int doneC = 0;

        for(Map.Entry<Integer, Epic> entry : epicSave.entrySet()){
            Epic ep = entry.getValue();

            if(ep.subTasks.isEmpty()){
                ep.setStatus("NEW");
                epicSave.put(entry.getKey(), ep);
                return;
            }

            for (int i = 0; i < ep.subTasks.size(); i++) {
                if(ep.subTasks.get(i).getStatus() == "NEW") {
                    newC++;
                }
                else if(ep.subTasks.get(i).getStatus() == "DONE") {
                    doneC++;
                }
                }
            if(newC == ep.subTasks.size()) {
                ep.setStatus("NEW");
            }
            else if(doneC == ep.subTasks.size()) {
                ep.setStatus("DONE");
            }
            else ep.setStatus("IN_PROGRESS");
            epicSave.put(entry.getKey(), ep);
            }
    }
    /*Если у эпика нет подзадач или все они имеют статус NEW | DONE, то статус должен быть NEW | DONE.
    Во всех остальных случаях статус должен быть IN_PROGRESS.*/
}
