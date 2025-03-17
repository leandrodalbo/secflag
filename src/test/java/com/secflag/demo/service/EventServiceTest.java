package com.secflag.demo.service;

import com.secflag.demo.domain.Event;
import com.secflag.demo.dto.EventDto;
import com.secflag.demo.error.InvalidEventTransaction;
import com.secflag.demo.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository repository;

    @InjectMocks
    private EventService service;

    @Test
    void shouldFetchAllExistingEvents() {
        when(repository.allEvents()).thenReturn(List.of(Event.newEvent("A", "B")));

        List<Event> events = service.fetchAllEvents();

        assertThat(events.get(0).eventName()).isEqualTo("A");
        assertThat(events.get(0).eventDescription()).isEqualTo("B");
    }

    @Test
    void shouldSaveNewEvent() throws InvalidEventTransaction {
        when(repository.saveNewEvent(anyString(), anyString())).thenReturn(1);

        service.saveNewEvent(new EventDto(null, "a", "b"));

        verify(repository, times(1)).saveNewEvent(anyString(), anyString());
    }

    @Test
    void shouldNotSaveANewEventIfItHasAnId() {

        assertThatExceptionOfType(InvalidEventTransaction.class)
                .isThrownBy(() -> service.saveNewEvent(new EventDto(1L, "a", "b")));

    }

    @Test
    void shouldFailIfTheTransactionFailed() {
        when(repository.saveNewEvent(anyString(), anyString())).thenReturn(0);

        assertThatExceptionOfType(InvalidEventTransaction.class)
                .isThrownBy(() -> service.saveNewEvent(new EventDto(null, "a", "b")));

        verify(repository, times(1)).saveNewEvent(anyString(), anyString());
    }

}
