package com.secflag.demo.repository;

import com.secflag.demo.domain.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EventRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.4"));
    @Autowired
    private EventRepository eventRepository;

    @Test
    public void shouldFetchAListOf5Events() {
        assertThat(eventRepository.allEvents().size()).isEqualTo(5);
    }

    @Test
    public void shouldInsertAnewEvent() {
        Event event = Event.newEvent("x", "y");
        int affectedRows = eventRepository.saveNewEvent(event.eventName(), event.eventDescription());

        assertThat(affectedRows).isEqualTo(1);
    }

    @Test
    public void shouldUpdateAnEvent() {
        Event event = eventRepository.allEvents().getFirst();

        int affectedRows = eventRepository.updateEvent(event.eventId(), "new name", event.eventDescription(), event.version() + 1);

        assertThat(affectedRows).isEqualTo(1);
    }
}
