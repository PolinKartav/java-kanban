package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Subtask> subTasks;
    String type = "Epic";

    //конструктор Epic
    public Epic(String nameEpic, String descriptionEpic){
        super(nameEpic, descriptionEpic);
        subTasks = new ArrayList<Subtask>();
        //this.subTasks = subTask;
    }

    //созранение подзадач в список
    public void saveSubtasksInList(Subtask task){
        subTasks.add(task);
    }

    public void deleteSubtask(Subtask task) {
        subTasks.remove(task);
    }
    @Override
    String getType(){
        return type;
    }

    @Override
    public String toString(){
        String result = "";
        result =  super.toString() + "\nSubtasks:'" + subTasks +  "'.\n";
        return result;
    }
}
