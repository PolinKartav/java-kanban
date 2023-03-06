package tasks;

public class Task {
     private String name;
     private String description;
     private Integer id;
     private StatusChoice status;


     //Конструктор для создания Task
     public Task(int id, String name, String description, StatusChoice status) {
         this.id = id;
         this.name = name;
         this.description = description;
         this.status = status;
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
         result =  this.getClass().getName() + ":" +" Название = '" + name + "', Описание= '" + description +
                 "', Id = '" + id + "', Статус= '" + status + "'.";
         return   result;
     }
     public String toSaveString(){
         return String.valueOf(getId()) + "," +
                 TaskType.TASK + "," +
                 getName() + "," +
                 getDescription() + "," +
                 getStatus();
     }

 }



