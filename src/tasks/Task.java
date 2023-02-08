package tasks;

public class Task {
     private String name;
     private String description;
     private int id;
     private StatusChoice status;
     private String type = "Task";


     //Конструктор для создания Task
     public Task(String name, String description, StatusChoice status) {
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

    String getType(){
        return type;
    }

     @Override
     public String toString() {
         String result = "";
         result =  getType() +":" +" Название = '" + name + "', Описание= '" + description +
                 "', Id = '" + id + "', Статус= '" + status + "'.";
         return   result;
     }
 }



