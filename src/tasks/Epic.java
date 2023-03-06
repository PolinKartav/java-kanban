package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    public ArrayList<Subtask> subTasks;

    //конструктор Epic
    public Epic(String nameEpic, String descriptionEpic){
        super(nameEpic, descriptionEpic);
        subTasks = new ArrayList<>();
    }

    public Epic(int id, String nameEpic, String descriptionEpic, StatusChoice status){
        super(id,nameEpic, descriptionEpic, StatusChoice.NEW);
        subTasks = new ArrayList<>();
    }

    //сохранение подзадач в список
    public void saveSubtasksInList(Subtask task){
        subTasks.add(task);
    }

    public void deleteSubtask(Subtask task) {
        subTasks.remove(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks)
                && getName().equals(epic.getName())
                && getDescription().equals(epic.getDescription())
                && getId() == epic.getId()
                && getStatus().equals(epic.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getId(), getStatus(), subTasks);
    }

    @Override
    public String toString(){
        String result = "";
        result =  super.toString() + "\nSubtasks:'" + subTasks +  "'.\n";
        return result;
    }
    @Override
    public String toSaveString(){
        return String.valueOf(getId()) + "," +
                TaskType.EPIC + "," +
                getName() + "," +
                getDescription() + "," +
                getStatus();
    }
}
