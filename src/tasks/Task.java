package tasks;

import java.time.LocalDateTime;

public class Task implements Comparable<Task>{
     private String name;
     private String description;
     private Integer id;
     private StatusChoice status;
     private long duration;
     private LocalDateTime startTime;



     //Конструктор для создания Task
    public Task(int id, String name, String description, StatusChoice status){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }
     public Task(int id, String name, String description, StatusChoice status, LocalDateTime startTime, long duration) {
         this.id = id;
         this.name = name;
         this.description = description;
         this.status = status;
         this.startTime = startTime;
         this.duration = duration;
     }

     //Конструктор для создания Epic и Subtasks
     public Task(String name, String description) {
         this.name = name;
         this.description = description;

     }
     public int getId() {
         return id;
     }
     public void setId(int id) {
         this.id = id;
     }
     public String getDescription() {
         return description;
     }
     public String getName() {
         return name;
     }

     public LocalDateTime getStartTime(){
         return startTime;
     }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public long getDuration() {
        return duration;
    }
    public LocalDateTime getEndTime(){
         return startTime.plusMinutes(duration);
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
     public StatusChoice getStatus() {
        setStatus(status);
         return status;
     }
    public void setStatus(StatusChoice status){
        this.status = status;
    }

     @Override
     public String toString() {
         String result = "";
         result =  "{ " + this.getClass().getName() + ":" +" Название= '" + name + "',"
                 + "\n'Описание= '" + description + "', " + "Id= '" + id + "',"
                 + "\nСтатус= '" + status + "', " + "startTime= '" + startTime + "'," +
                 "\nduration ='" + duration + "', " + "endTime ='" + getEndTime() + "'}" + "\n";
         return   result;
     }
     public String toSaveString(){
         return String.valueOf(getId()) + "," +
                 TaskType.TASK + "," +
                 getName() + "," +
                 getDescription() + "," +
                 getStatus() + ", " +
                 getStartTime() + ", " +
                 getDuration() + ", " +
                 getEndTime();
     }

    @Override
    public int compareTo(Task o) {
        if(this.getStartTime().isBefore(o.getStartTime())){
            return  -1;
        } else if (this.getStartTime().isAfter(o.getStartTime())){
            return 1;
        } else if(this.getStartTime() == null){
            return -3;
        }
        else return 0;
    }
}



