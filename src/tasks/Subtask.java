package tasks;

public class Subtask extends Task {
    private Integer parentEpicId;
    String type = "Subtask";
    public Subtask(Integer parentEpicId, String nameSubtask, String descriptionSubtask,
                   StatusChoice statusSubtask){
        super(nameSubtask, descriptionSubtask, statusSubtask);
        this.parentEpicId = parentEpicId;
    }

    public Integer getParentEpicId() {
        return parentEpicId;
    }

    @Override
    String getType(){
        return type;
    }

    @Override
    public String toString(){
        String result = "";
        result = "Id of the Epic: " + parentEpicId + "', " +  super.toString() + "\n";
        return result;
    }
}
