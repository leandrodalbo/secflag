package com.secflag.demo.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {
    @Test
    void itShouldImplementFactoryMethod() {
        assertThat(Event.newEvent("A", "B")).isNotNull();
    }
}
