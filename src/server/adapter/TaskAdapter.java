package server.adapter;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tasks.Epic;
import tasks.StatusChoice;
import tasks.Subtask;
import tasks.Task;

import java.text.ParseException;
import java.time.LocalDateTime;

import static tasks.StatusChoice.leadToStatusChoice;

public class TaskAdapter {
    private JSONParser parser = new JSONParser();

    public Task parseTask(String jsonString) {
        this.parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);

            String name =  json.get("name").toString();
            int id = ((Long) json.get("id")).intValue();
            String description =  json.get("description").toString();
            StatusChoice status =leadToStatusChoice(json.get("status").toString());
            JSONObject jsonStart = (JSONObject) json.get("startTime");
            JSONObject jsonDate = (JSONObject) jsonStart.get("date");
            JSONObject jsonTime = (JSONObject) jsonStart.get("time");
            LocalDateTime startTime = getLocalTimeFromJson(jsonDate, jsonTime);
            long duration = (Long) json.get("duration");
            return new Task(id, name, description, status, startTime, duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Epic parseEpic(String jsonString){
        this.parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);

            String name =  json.get("name").toString();
            int id = ((Long) json.get("id")).intValue();
            String description =  json.get("description").toString();
            StatusChoice status =leadToStatusChoice(json.get("status").toString());
            JSONObject jsonStart = (JSONObject) json.get("startTime");
            JSONObject jsonDate = (JSONObject) jsonStart.get("date");
            JSONObject jsonTime = (JSONObject) jsonStart.get("time");
            LocalDateTime startTime = getLocalTimeFromJson(jsonDate, jsonTime);
            long duration = (Long) json.get("duration");
            return new Epic(id, name, description, status, startTime, duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Subtask parseSubtask(String jsonString){
        this.parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);

            String name =  json.get("name").toString();
            int id = ((Long) json.get("id")).intValue();
            Integer parentEpicId = ((Long) json.get("parentEpicId")).intValue();
            String description =  json.get("description").toString();
            StatusChoice status =leadToStatusChoice(json.get("status").toString());
            JSONObject jsonStart = (JSONObject) json.get("startTime");
            JSONObject jsonDate = (JSONObject) jsonStart.get("date");
            JSONObject jsonTime = (JSONObject) jsonStart.get("time");
            LocalDateTime startTime = getLocalTimeFromJson(jsonDate, jsonTime);
            long duration = (Long) json.get("duration");
            return new Subtask(id, parentEpicId, name, description, status, startTime, duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private LocalDateTime getLocalTimeFromJson(JSONObject jsonDate, JSONObject jsonTime){
        return LocalDateTime.of(((Long) jsonDate.get("year")).intValue(), ((Long) jsonDate.get("month")).intValue(), ((Long) jsonDate.get("day")).intValue(), ((Long) jsonTime.get("hour")).intValue(), ((Long) jsonTime.get("minute")).intValue());
    }
}
