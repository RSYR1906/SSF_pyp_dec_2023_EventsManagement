package vttp.ssf.assessment.eventmanagement.repositories;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vttp.ssf.assessment.eventmanagement.constant.Constants;
import vttp.ssf.assessment.eventmanagement.models.Event;
import vttp.ssf.assessment.eventmanagement.models.User;

@Repository
public class RedisRepository {

	@Autowired
	@Qualifier(Constants.template01)
	RedisTemplate<String, Object> template;

	// TODO: Task 2
	public void saveEvent(Event event) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		// Retrieve all events
		List<Object> eventJsonList = template.opsForList().range("eventsList", 0, -1);

		if (eventJsonList != null) {
			for (Object eventJson : eventJsonList) {
				Event existingEvent = objectMapper.readValue(eventJson.toString(), Event.class);

				// Check if the event already exists by comparing eventId
				if (existingEvent.getEventId().equals(event.getEventId())) {
					System.out.println("Event already exists: " + event.getEventName());
					return; // Exit if event exists
				}
			}
		}
		// Add event if it doesn't already exist
		String eventJson = objectMapper.writeValueAsString(event);
		template.opsForList().rightPush("eventsList", eventJson);
		System.out.println("Event added: " + event.getEventId());
	}

	// TODO: Task 3
	public Integer getNumberOfEvents() {
		return (int) (long) template.opsForList().size(Constants.REDIS_KEY);
	}

	// TODO: Task 4
	public Event getEvent(Integer index) throws JsonMappingException, JsonProcessingException {
		Object eventObject = template.opsForList().index(Constants.REDIS_KEY, index);
		ObjectMapper objectMapper = new ObjectMapper();
		Event event = new Event();
		event = objectMapper.readValue(eventObject.toString(), Event.class);
		return event;
	}

	public Event getEventById(Integer eventId) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		// Get the entire list from Redis
		List<Object> eventJsonList = template.opsForList().range("eventsList", 0, -1);

		if (eventJsonList == null || eventJsonList.isEmpty()) {
			return null; // Handle case where no events exist
		}
		for (Object eventJson : eventJsonList) {
			Event event = objectMapper.readValue(eventJson.toString(), Event.class);
			if (event.getEventId().equals(eventId)) {
				return event; // Return the matching event
			}
		}

		return null; // Event not found
	}

	public List<Event> getAllEvents() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Object> eventJsonList = template.opsForList().range("eventsList", 0, -1);

		if (eventJsonList == null || eventJsonList.isEmpty()) {
			return new ArrayList<>(); // Return an empty list if no events exist
		}

		List<Event> events = new ArrayList<>();
		for (Object eventJson : eventJsonList) {
			Event event = objectMapper.readValue(eventJson.toString(), Event.class);
			events.add(event);
		}

		return events;
	}

	public Integer getAge(User user) {
		// Convert Date to LocalDate
		LocalDate birthLocalDate = user.getBirthDate().toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate();

		// Get the current date
		LocalDate currentDate = LocalDate.now();

		// Calculate the age
		return Period.between(birthLocalDate, currentDate).getYears();
	}

	public void updateParticipants(Integer eventId, int ticketsRequested) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		// Retrieve all events
		List<Object> eventJsonList = template.opsForList().range("eventsList", 0, -1);

		if (eventJsonList != null) {
			for (int i = 0; i < eventJsonList.size(); i++) {
				Object eventJson = eventJsonList.get(i);
				Event event = objectMapper.readValue(eventJson.toString(), Event.class);

				// Find the event by ID
				if (event.getEventId().equals(eventId) && event.getParticipants() < event.getEventSize()) {

					// Increment the participants count
					event.setParticipants(event.getParticipants() + ticketsRequested);

					// Replace the updated event back in Redis
					String updatedEventJson = objectMapper.writeValueAsString(event);
					template.opsForList().set("eventsList", i, updatedEventJson);
					System.out.println("Updated participants for event: " + event.getEventName());
					return;
				}
			}
		}

		System.err.println("Event not found with ID: " + eventId);
	}
}
