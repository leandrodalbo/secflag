package com.secflag.demo.repository;

import com.secflag.demo.domain.Event;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    @Query(""" 
            SELECT event_id, event_name, event_description, version FROM event;
            """)
    List<Event> allEvents();


    @Modifying
    @Transactional
    @Query("""
                INSERT INTO event (event_name, event_description, version) VALUES (:eventName, :eventDescription, 0)
            """)
    int saveNewEvent(String eventName, String eventDescription);

    @Modifying
    @Transactional
    @Query("""
                UPDATE event SET event_name = :eventName, event_description = :eventDescription, version = :version WHERE event_id = :eventId
            """)
    int updateEvent(Long eventId, String eventName, String eventDescription, Integer version);
}
