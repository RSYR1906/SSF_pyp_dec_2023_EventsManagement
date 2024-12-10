package vttp.ssf.assessment.eventmanagement.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.ssf.assessment.eventmanagement.models.Event;

@Service
public class DatabaseService {

    // TODO: Task 1

    public List<Event> readFile(String fileName) throws FileNotFoundException {

        FileInputStream fis = new FileInputStream(fileName);
        JsonReader jReader = Json.createReader(fis);
        JsonArray jArray = jReader.readArray();

        List<Event> eventList = new ArrayList();

        for (JsonValue jsonValue : jArray) {
            JsonObject jsonObject = jsonValue.asJsonObject();

            Event event = new Event();
            event.setEventId(jsonObject.getJsonNumber("eventId").intValue());
            event.setEventName(jsonObject.getString("eventName"));
            event.setEventDate(jsonObject.getJsonNumber("eventDate").longValue());
            event.setEventSize(jsonObject.getJsonNumber("eventSize").intValue());
            event.setParticipants(jsonObject.getJsonNumber("participants").intValue());

            eventList.add(event);
        }
        return eventList;
    }
}
