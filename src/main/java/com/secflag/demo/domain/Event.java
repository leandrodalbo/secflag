package com.secflag.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("event")
public record Event(@Id
                    @Column("event_id")
                    Long eventId,

                    @Column("event_name")
                    String eventName,
                    @Column("event_description")
                    String eventDescription,

                    @Version
                    @Column("version")
                    Integer version) {

    public static Event newEvent(String eventName, String eventDescription) {
        return new Event(null, eventName, eventDescription, 0);
    }
}
