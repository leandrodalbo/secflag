package com.secflag.demo.service;

import com.secflag.demo.domain.Event;
import com.secflag.demo.dto.EventDto;
import com.secflag.demo.error.InvalidEventTransaction;
import com.secflag.demo.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> fetchAllEvents() {
        return repository.allEvents();
    }

    public void saveNewEvent(EventDto eventDto) throws InvalidEventTransaction {
        if (eventDto.eventId() != null) {
            throw new InvalidEventTransaction();
        }

        int affectedRows = repository.saveNewEvent(eventDto.eventName(), eventDto.eventDescription());

        if (affectedRows != 1) {
            throw new InvalidEventTransaction();
        }
    }

    public void updateEvent(EventDto eventDto) throws InvalidEventTransaction {
        Optional<Event> event = repository.findById(eventDto.eventId());

        if (event.isEmpty()) {
            throw new InvalidEventTransaction();
        }

        int affectedRows = repository.updateEvent(eventDto.eventId(), eventDto.eventName(), eventDto.eventDescription(), event.get().version() + 1);

        if (affectedRows != 1) {
            throw new InvalidEventTransaction();
        }
    }
}
