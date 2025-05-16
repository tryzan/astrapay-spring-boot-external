package com.astrapay.service;

import com.astrapay.dto.ResponseDto;
import com.astrapay.dto.UpsertDto;
import com.astrapay.entity.Note;
import com.astrapay.repository.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class NoteService {

    private NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
    }
    public ResponseDto saveNotes(UpsertDto upsertDto) {
        Note entity = new Note();
        entity.setTitle(upsertDto.getTitle());
        entity.setDescription(upsertDto.getDescription());
        noteRepository.save(entity);
        return new ResponseDto(true,"success",null);

    }

    public ResponseDto findAllNotes(int page,int size){
        Pageable pagination = PageRequest.of(page-1,size);
        Map<String,Object> data = new LinkedHashMap<>();
        Page<Note> listNotes = noteRepository.findAll(pagination);
        data.put("data",listNotes.getContent());
        data.put("totalElements",listNotes.getTotalElements());
        data.put("totalPages",listNotes.getTotalPages());
        data.put("size",listNotes.getSize());
        return new ResponseDto(true,"Successfuly",data);
    }

    public ResponseDto findById(Long id){
         Note entity = noteRepository.findById(id).orElse(null);
         if(entity == null){
             return new ResponseDto(false, "Data Not Found",null);
         }
        return new ResponseDto(true, "Data Found",entity);
    }

    public ResponseDto deleteNotesById(Long id){
        return noteRepository.findById(id)
                .map(note -> {
                    noteRepository.delete(note);
                    return new ResponseDto(true, "Successfully deleted the note",null);
                })
                .orElseGet(() -> new ResponseDto(false, "failed to delete note",null));
    }

}