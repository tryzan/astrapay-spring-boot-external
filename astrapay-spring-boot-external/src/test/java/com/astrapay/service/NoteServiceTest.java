package com.astrapay.service;

import com.astrapay.dto.ResponseDto;
import com.astrapay.dto.UpsertDto;
import com.astrapay.entity.Note;
import com.astrapay.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoteServiceTest {
    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveNotes() {
        UpsertDto dto = new UpsertDto();
        dto.setTitle("Test");
        dto.setDescription("Desc");

        Note note = new Note();
        note.setTitle(dto.getTitle());
        note.setDescription(dto.getDescription());

        when(noteRepository.save(any(Note.class))).thenReturn(note);

        ResponseDto result = noteService.saveNotes(dto);

        assertTrue(result.isSuccess());
        assertEquals("success", result.getMessage());
    }

    @Test
    void testFindAllNotes() {
        List<Note> notes = Arrays.asList(new Note(), new Note());
        Page<Note> page = new PageImpl<>(notes);
        when(noteRepository.findAll(any(Pageable.class))).thenReturn(page);

        ResponseDto result = noteService.findAllNotes(1, 10);

        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
    }

    @Test
    void testFindByIdFound() {
        Note note = new Note();
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        ResponseDto result = noteService.findById(1L);

        assertTrue(result.isSuccess());
        assertEquals("Data Found", result.getMessage());
    }

    @Test
    void testFindByIdNotFound() {
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseDto result = noteService.findById(1L);

        assertFalse(result.isSuccess());
        assertEquals("Data Not Found", result.getMessage());
    }

    @Test
    void testDeleteNotesByIdSuccess() {
        Note note = new Note();
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        ResponseDto result = noteService.deleteNotesById(1L);

        assertTrue(result.isSuccess());
        assertEquals("Successfully deleted the note", result.getMessage());
    }

    @Test
    void testDeleteNotesByIdFail() {
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseDto result = noteService.deleteNotesById(1L);

        assertFalse(result.isSuccess());
        assertEquals("failed to delete note", result.getMessage());
    }
}
