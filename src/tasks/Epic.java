package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Subtask> subTasks;
    String type = "Epic";

    //конструктор Epic c Subtasks
    public Epic(String nameEpic, String descriptionEpic, ArrayList<Subtask> subTask){
        super(nameEpic, descriptionEpic);
        this.subTasks = subTask;
    }

    //конструктор Epic без Subtasks
    public Epic(String nameEpic, String descriptionEpic){
        super(nameEpic, descriptionEpic);
        subTasks = new ArrayList<>();
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
