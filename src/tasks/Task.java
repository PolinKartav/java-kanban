package tasks;
import manager.TaskManage;

 public class Task {
     private String name;
     private String description;
     private int id;
     private String status;

     //Конструктор для создания Task
     public Task(String name, String description, String status) {
         this.name = name;
         this.description = description;
         this.id = TaskManage.getId() + 1;
         TaskManage.setId(this.id);
         this.status = status;
     }

     //Конструктор для создания Epic и Subtasks
     public Task(String name, String description) {
         this.name = name;
         this.description = description;
         this.id = TaskManage.getId() +1;
         TaskManage.setId(this.id);
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
     public String getStatus() {
        setStatus(status);
         return status;
     }
    public void setStatus(String status){
        this.status = status;
    }
     @Override
     public String toString() {
         String result = "";
         result = " Название simple Task= '" + name + "', Описание= '" + description +
                 "', Id simple Task= '" + id + "', Статус= '" + status + "'.";
         return result;
     }
 }



