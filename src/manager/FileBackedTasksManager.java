package manager;

import tasks.*;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private Path path;

    public FileBackedTasksManager(Path path) {
        this.path = path;
    }

    public void save() {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);//создание физического файла
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка создания файла");
            }
        }

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(path.toAbsolutePath().toString(), StandardCharsets.UTF_8))) {
            bw.write("id,type,name,description,status,epic");
            if (!super.getListOfAllTasks().isEmpty()) {
                bw.newLine();

                for (Task task : getListOfAllTasks()) {
                    bw.write(task.toSaveString());
                    bw.newLine();
                }
                bw.newLine();

                List<Task> list = super.getHistory();
                List<Integer> lisId = new ArrayList<>();
                for (Task task : list) {
                    lisId.add(task.getId());
                }
                bw.write(String.join(",", lisId.toArray(new String[0])));

            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла");
        }
    }

    //создание задачи из строки
    public static Task fromString(String value) {

        String[] str = (value.split(",", 6));
        TaskType type = TaskType.valueOf(str[1]);

        int id = Integer.valueOf(str[0]);
        switch (type) {
            case TASK:
                return new Task(id, str[2], str[3], StatusChoice.valueOf(str[4]));
            case EPIC:
                return new Epic(id, str[2], str[3], StatusChoice.NEW);
            case SUBTASK:
                return new Subtask(id, Integer.valueOf(str[5]), str[2], str[3], StatusChoice.valueOf(str[4]));
            default:
                return null;
        }
    }

    //восстанавливать данные менеджера из файла при запуске программы.
    public static FileBackedTasksManager loadFromFile(Path path) {
        FileBackedTasksManager manager = new FileBackedTasksManager(path);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);//создание физического файла
            } catch (IOException e) {
                throw new ManagerSaveException("Файла не существует");
            }
        }

        try (BufferedReader bw = new BufferedReader(
                new FileReader(path.toAbsolutePath().toString(), StandardCharsets.UTF_8))) {

            bw.readLine();
            int maxId = 0;
            while (bw.ready()) {
                String line = bw.readLine();
                if(line.equals("")){
                    break;
                }
                Task task = fromString(line);

                assert task != null;
                if (task.getId() > maxId) {
                    maxId = task.getId();
                }
                if (task instanceof Epic) {
                    manager.epicSave.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    manager.subtaskSave.put(task.getId(), (Subtask) task);
                } else {
                    manager.taskSave.put(task.getId(), task);
                }

            }
            manager.iD = ++maxId;
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
        // Проверка на существование файла
        // открываем буфер с читателем
        // Проверка пустоту файла
        // читаем файл до первой пустой строки
        // формируем список тасок или строк
        // создаем таски в памяти менеджера
        // считываем историю
        // загружаем историю в память менеджера

        return manager;
    }

    @Override
    public void saveAnyTask(Task o) {
        super.saveAnyTask(o);
        save();

    }

    @Override
    public ArrayList<Task> getListOfAllTasks() {
        ArrayList<Task> list = super.getListOfAllTasks();
        return list;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();

    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void updateTasks(Task o, int id) {
        super.updateTasks(o, id);
        save();
    }

    @Override
    public void updateSubtaskInEpic(Subtask subtask, int id) {
        super.updateSubtaskInEpic(subtask, id);
        save();
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public List<Subtask> getAllSubtasksOfEpic(int id) {
        List<Subtask> list = super.getAllSubtasksOfEpic(id);
        save();
        return list;
    }

    @Override
    public void getStatusEpic() {
        super.getStatusEpic();
        save();
    }

    @Override
    public List<Task> getHistory() {
        List<Task> list = super.getHistory();
        save();
        return list;
    }

    @Override
    public void saveSubtasksInEpic(Epic epic, Subtask task) {
        super.saveSubtasksInEpic(epic, task);
        save();

    }
}
