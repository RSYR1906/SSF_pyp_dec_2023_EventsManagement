package vttp.ssf.assessment.eventmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;
import vttp.ssf.assessment.eventmanagement.models.Event;
import vttp.ssf.assessment.eventmanagement.models.User;
import vttp.ssf.assessment.eventmanagement.repositories.RedisRepository;

@Controller
@RequestMapping("/events")
public class RegistrationController {

    @Autowired
    RedisRepository redisRepository;

    // Display the registration form
    @GetMapping("/register")
    public String registerForm(@RequestParam("eventId") Integer eventId, Model model)
            throws JsonProcessingException {
        Event event = redisRepository.getEventById(eventId);

        if (event == null) {
            return "error"; // Handle case where event is not found
        }

        model.addAttribute("event", event);
        model.addAttribute("user", new User());
        return "eventregister";
    }

    // Process the registration form submission
    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            @RequestParam("eventId") Integer eventId,
            Model model) throws JsonProcessingException {

        if (result.hasErrors()) {
            Event event = redisRepository.getEventById(eventId);
            model.addAttribute("event", event); // Retain event details if validation fails
            return "eventregister"; // Return to the registration form
        }

        // Check if the user is eligible
        if (redisRepository.getAge(user) < 21) {
            Event event = redisRepository.getEventById(eventId);
            model.addAttribute("event", event);
            model.addAttribute("errorMessage", "You must be at least 21 years old to register.");
            return "ErrorRegistration"; // Return to form with error
        }

        try {
            // Update the participants count in the event
            redisRepository.updateParticipants(eventId, user.getTicketAmount());
            Event eventUpdated = redisRepository.getEventById(eventId);

            // Redirect to success page
            model.addAttribute("event", eventUpdated);
            return "success"; // Render success page
        } catch (IllegalArgumentException e) {
            // Handle the error when participants exceed the event size
            Event event = redisRepository.getEventById(eventId);
            model.addAttribute("event", event);
            model.addAttribute("errorMessage", e.getMessage());
            return "ErrorRegistration"; // Redirect to error page
        }
    }
}