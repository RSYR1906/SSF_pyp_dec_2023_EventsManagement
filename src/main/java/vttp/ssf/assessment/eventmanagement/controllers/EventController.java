package vttp.ssf.assessment.eventmanagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

import vttp.ssf.assessment.eventmanagement.models.Event;
import vttp.ssf.assessment.eventmanagement.repositories.RedisRepository;
import vttp.ssf.assessment.eventmanagement.services.DatabaseService;

@Controller
@RequestMapping("/events")
public class EventController {

	@Autowired
	DatabaseService databaseService;

	@Autowired
	RedisRepository redisRepository;

	// TODO: Task 5
	@GetMapping("")
	public String displayEvents(Model model) throws JsonProcessingException {
		List<Event> eventsList = redisRepository.getAllEvents();
		model.addAttribute("events", eventsList);
		return "events-list";
	}

}
