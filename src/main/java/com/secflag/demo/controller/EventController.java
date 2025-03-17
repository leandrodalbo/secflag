package com.secflag.demo.controller;

import com.secflag.demo.domain.Event;
import com.secflag.demo.dto.EventDto;
import com.secflag.demo.error.InvalidEventTransaction;
import com.secflag.demo.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Event> allEvents() {
        return this.service.fetchAllEvents();
    }


    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public void newEvent(@RequestBody EventDto eventDto) throws InvalidEventTransaction {
        service.saveNewEvent(eventDto);
    }

}
