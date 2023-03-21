package taskTest;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.discovery.SelectorResolver;
import tasks.Epic;
import tasks.StatusChoice;
import tasks.Subtask;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.StatusChoice.*;

class EpicTest {
    TaskManager manager;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
        epic = new Epic(1, "Написать диплом", "Провести исследование", NEW);
        subtask1 = new Subtask(2, 1, "Выбрать тему", "Выбрать хорошую тему для исследования", DONE, LocalDateTime.of(2023, Month.JANUARY, 5, 10, 05), 30);
        subtask2 = new Subtask(3, 1, "Выбрать научника", "Выбрать хорошего начного руководителя", DONE, LocalDateTime.of(2023, Month.JANUARY, 5, 10, 05), 30) ;
        subtask3 = new Subtask(4, 1, "Начать писать", "Создать документ Диплом", NEW, LocalDateTime.of(2023, Month.FEBRUARY, 5, 10, 5), 30);
    }

    @Test
    void shouldPutSubtaskInList(){
        manager.saveSubtasksInEpic(epic, subtask1);
        manager.saveSubtasksInEpic(epic, subtask2);
        manager.saveSubtasksInEpic(epic, subtask3);
        ArrayList<Subtask> list = new ArrayList<>();
        list.add(subtask1);
        list.add(subtask2);
        list.add(subtask3);
        assertEquals(list, epic.getSubtask(), "Возвращает лист с Subtasks правильно");
        list.remove(0);
        assertNotEquals(list, epic.getSubtask(), "Лист с subtasks неверен");
    }

    @Test
    void shouldGetNullTimeOfEpic(){
        assertEquals(0, epic.getDuration(), "Продолжительность неизвестна");
        assertNull(epic.getStartTime(), "Начало выполнения неизвестно");
        assertNull(epic.getEndTime(), "Окончание выполнения неизвестно");
    }

    @Test
    void shouldGetRightStatus(){
        Subtask subtask_1 = subtask1;
        Subtask subtask_2 = subtask2;
        Subtask subtask_3 = subtask3;
        Epic epic1 = epic;
        manager.saveAnyTask(epic1);
        manager.saveAnyTask(subtask_1);
        manager.saveAnyTask(subtask_2);
        manager.saveAnyTask(subtask_3);
        manager.saveSubtasksInEpic(epic1, subtask_1);
        manager.saveSubtasksInEpic(epic1, subtask_2);
        manager.saveSubtasksInEpic(epic1, subtask_3);
        manager.getStatusEpic();
        assertEquals(IN_PROGRESS, manager.getTaskById(1).getStatus(), "Статус обновлен");
        assertNotEquals(NEW, manager.getTaskById(1).getStatus(), "Статус не верен");
    }
}