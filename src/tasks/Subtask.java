package tasks;

public class Subtask extends Task {
    private String nameEpic;
    public Subtask(String nameEpic, String nameSubtask, String descriptionSubtask,
                   String statusSubtask){
        super(nameSubtask, descriptionSubtask, statusSubtask);
        this.nameEpic = nameEpic;
    }

    @Override
    public String toString(){
        String result = "";
        result = "Название Epic= '" + nameEpic + "'\t, Название Subtask= '" + getName() +"', Описание= '" +
                getDescription() + "', id subtask= '" + getId() + "', Статус= '" + getStatus() + "'.";
        return result;
    }
}
