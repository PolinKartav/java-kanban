package tasks;


import manager.InMemoryTaskManager;

public class Task {
     private String name;
     private String description;
     private int id;
     private StatusChoice status;

     //Конструктор для создания Task
     public Task(String name, String description, StatusChoice status) {
         this.name = name;
         this.description = description;
         this.id = InMemoryTaskManager.getId() + 1;
         InMemoryTaskManager.setId(this.id);
         this.status = status;
     }

     //Конструктор для создания Epic и Subtasks
     public Task(String name, String description) {
         this.name = name;
         this.description = description;
         this.id = InMemoryTaskManager.getId() +1;
         InMemoryTaskManager.setId(this.id);
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
         result = " Название = '" + name + "', Описание= '" + description +
                 "', Id = '" + id + "', Статус= '" + status + "'.";
         return result;
     }
 }



