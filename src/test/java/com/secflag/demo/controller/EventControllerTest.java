package com.secflag.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secflag.demo.domain.Event;
import com.secflag.demo.dto.EventDto;
import com.secflag.demo.error.InvalidEventTransaction;
import com.secflag.demo.service.EventService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private EventService service;

    @Test
    void shouldFetchAllExistingEvents() throws Exception {
        given(service.fetchAllEvents()).willReturn(List.of(Event.newEvent("A", "B")));

        MockHttpServletResponse res = mvc.perform(get("/event/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(service, times(1)).fetchAllEvents();
    }

    @Test
    public void shouldSaveAnewEventAndReturn201() throws Exception {
        String reqBody = mapper.writeValueAsString(new EventDto(null, "a", "b"));

        doNothing().when(service).saveNewEvent(any(EventDto.class));

        MockHttpServletResponse res = mvc.perform(post("/event/new")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        verify(service, times(1)).saveNewEvent(any(EventDto.class));
    }

    @Test
    public void shouldHandleWhenSavingAnewEvenFailed() throws Exception {
        String reqBody = mapper.writeValueAsString(new EventDto(null, "a", "b"));

        doThrow(InvalidEventTransaction.class).when(service).saveNewEvent(any(EventDto.class));

        MockHttpServletResponse res = mvc.perform(post("/event/new")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verify(service, times(1)).saveNewEvent(any(EventDto.class));
    }

}
