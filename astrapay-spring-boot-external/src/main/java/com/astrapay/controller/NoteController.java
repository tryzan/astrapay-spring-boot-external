package com.astrapay.controller;

import com.astrapay.dto.ResponseDto;
import com.astrapay.dto.UpsertDto;
import com.astrapay.exception.ExampleException;
import com.astrapay.service.NoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "NotesController")
@Slf4j
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    @ApiOperation(value = "Say Hello")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK", response = UpsertDto.class)
            }
    )
    public ResponseEntity<ResponseDto> listNotes(@RequestParam(value = "page",defaultValue = "1") int page,
                                                 @RequestParam(value = "size",defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.findAllNotes(page,size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> detailNotes(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(noteService.findById(id));
    }
    @PostMapping
    public ResponseEntity<ResponseDto> upsertNote(@Valid @RequestBody UpsertDto upsertDto){
        return ResponseEntity.status(HttpStatus.OK).body(noteService.saveNotes(upsertDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteNoteById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(noteService.deleteNotesById(id));
    }


}