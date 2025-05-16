package com.astrapay.controller;

import com.astrapay.dto.ResponseDto;
import com.astrapay.dto.UpsertDto;
import com.astrapay.entity.Note;
import com.astrapay.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUpsertNote_success() throws Exception {
        UpsertDto dto = new UpsertDto(1,"Test", "Desc");
        ResponseDto response = new ResponseDto(true, "success", null);

        when(noteService.saveNotes(any(UpsertDto.class))).thenReturn(response);

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testUpsertNote_invalidRequest() throws Exception {
        UpsertDto dto = new UpsertDto(1,"", ""); // Assume @NotBlank in DTO

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testListNotes_success() throws Exception {
        // Prepare dummy note list
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test Title");
        note.setDescription("Test Description");

        List<Note> noteList = Collections.singletonList(note);

        Page<Note> page = new PageImpl<>(noteList, PageRequest.of(0, 10), 1);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("data", page.getContent());
        data.put("totalElements", page.getTotalElements());
        data.put("totalPages", page.getTotalPages());
        data.put("size", page.getSize());

        ResponseDto response = new ResponseDto(true, "Successfuly", data);

        when(noteService.findAllNotes(1, 10)).thenReturn(response);

        mockMvc.perform(get("/notes?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfuly"))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.size").value(10))
                .andExpect(jsonPath("$.data.data[0].title").value("Test Title"));
    }


    @Test
    void testDetailNote_success() throws Exception {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test Title");
        note.setDescription("Test Description");

        ResponseDto response = new ResponseDto(true, "Data Found", note);

        when(noteService.findById(1L)).thenReturn(response);

        String json = mockMvc.perform(get("/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Data Found"))
                .andExpect(jsonPath("$.data.title").value("Test Title"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ResponseDto dto = objectMapper.readValue(json, ResponseDto.class);
        assertTrue(dto.isSuccess());
        assertEquals("Data Found", dto.getMessage());
    }

    @Test
    void testDetailNote_notFound() throws Exception {
        ResponseDto response = new ResponseDto(false, "Data Not Found", null);

        when(noteService.findById(999L)).thenReturn(response);

        mockMvc.perform(get("/notes/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testDeleteNote_success() throws Exception {
        ResponseDto response = new ResponseDto(true, "Successfully deleted the note", null);

        when(noteService.deleteNotesById(1L)).thenReturn(response);

        mockMvc.perform(delete("/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testDeleteNote_notFound() throws Exception {
        ResponseDto response = new ResponseDto(false, "failed to delete note", null);

        when(noteService.deleteNotesById(123L)).thenReturn(response);

        mockMvc.perform(delete("/notes/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }
}
