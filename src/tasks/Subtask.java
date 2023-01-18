package tasks;

public class Subtask extends Task {
    private String nameEpic;
    public Subtask(String nameEpic, String nameSubtask, String descriptionSubtask,
                   StatusChoice statusSubtask){
        super(nameSubtask, descriptionSubtask, statusSubtask);
        this.nameEpic = nameEpic;
    }

    @Override
    public String toString(){
        String result = "";
        result = "Epic: Название = '" + nameEpic + "', Subtask: '" +  super.toString() + "\n";
        return result;
    }
}
