package vttp.ssf.assessment.eventmanagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import vttp.ssf.assessment.eventmanagement.constant.Constants;
import vttp.ssf.assessment.eventmanagement.models.Event;
import vttp.ssf.assessment.eventmanagement.repositories.RedisRepository;
import vttp.ssf.assessment.eventmanagement.services.DatabaseService;

@SpringBootApplication
public class EventmanagementApplication implements CommandLineRunner {

	@Autowired
	private DatabaseService databaseService;

	@Autowired
	private RedisRepository redisRepository;

	public static void main(String[] args) {
		SpringApplication.run(EventmanagementApplication.class, args);
	}

	// TODO: Task 1

	@Override
	public void run(String... args) throws Exception {

		try {
			List<Event> eventList = databaseService.readFile(Constants.fileName);

			for (Event event : eventList) {
				redisRepository.saveEvent(event);
			}
		} catch (Exception e) {
			System.err.println("Error while reading events: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
