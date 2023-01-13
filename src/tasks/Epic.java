package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Subtask> subTasks;

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
    public String toString(){
        String result = "";
        result = "Название Epic = '" + getName() + "', Описание = '" + getDescription() +
                "', id Epic = '" + getId() + "', Cтатус = '" + getStatus() + "':\n" +
                "Subtasks:'" + subTasks +  "'.\n";
        return result;
    }
}
