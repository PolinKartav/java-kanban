package tasks;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer parentEpicId;
    public Subtask(int id, Integer parentEpicId, String nameSubtask, String descriptionSubtask,
                   StatusChoice statusSubtask){
        super(id, nameSubtask, descriptionSubtask, statusSubtask);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(int id, Integer parentEpicId, String nameSubtask, String descriptionSubtask,
                   StatusChoice statusSubtask, LocalDateTime startTime, long duration){
        super(id, nameSubtask, descriptionSubtask, statusSubtask,startTime,duration);
        this.parentEpicId = parentEpicId;
    }

    public Integer getParentEpicId() {
        return parentEpicId;
    }

    @Override
    public String toString(){
        String result = "";
        result = "Id of the Epic: " + parentEpicId + "', " +  super.toString() + "\n";
        return result;
    }
    @Override
    public String toSaveString(){
        String result = super.toSaveString();
        return result;
    }
}
