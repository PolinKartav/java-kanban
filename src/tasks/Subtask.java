package tasks;

public class Subtask extends Task {
    private String nameEpic;
    String type = "Subtask";
    public Subtask(String nameEpic, String nameSubtask, String descriptionSubtask,
                   StatusChoice statusSubtask){
        super(nameSubtask, descriptionSubtask, statusSubtask);
        this.nameEpic = nameEpic;
    }

    @Override
    String getType(){
        return type;
    }

    @Override
    public String toString(){
        String result = "";
        result = "Epic: Название = '" + nameEpic + "', " +  super.toString() + "\n";
        return result;
    }
}
