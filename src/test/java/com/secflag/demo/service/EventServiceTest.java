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
import java.util.Optional;

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

    @Test
    void updateShouldFailIfTheEventIsNotFound() {
        when(repository.findById(null)).thenReturn(Optional.empty());

        assertThatExceptionOfType(InvalidEventTransaction.class)
                .isThrownBy(() -> service.updateEvent(new EventDto(null, "a", "b")));

        verify(repository, times(1)).findById(null);
    }

    @Test
    void updateShouldFailIfTheTransactionFailed() {
        when(repository.findById(1L)).thenReturn(Optional.of(Event.newEvent("a", "b")));
        when(repository.updateEvent(anyLong(), anyString(), anyString(), anyInt())).thenReturn(0);

        assertThatExceptionOfType(InvalidEventTransaction.class)
                .isThrownBy(() -> service.updateEvent(new EventDto(1L, "a", "b")));

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).updateEvent(anyLong(), anyString(), anyString(), anyInt());
    }

    @Test
    void shouldUpdateAnEvent() throws InvalidEventTransaction {
        when(repository.findById(1L)).thenReturn(Optional.of(Event.newEvent("a", "b")));
        when(repository.updateEvent(anyLong(), anyString(), anyString(), anyInt())).thenReturn(1);

        service.updateEvent(new EventDto(1L, "a", "b"));

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).updateEvent(anyLong(), anyString(), anyString(), anyInt());
    }

}
