package com.benjamin.api.Controllers;

import com.benjamin.api.Models.Note;
import com.benjamin.api.Models.ResponseData;
import com.benjamin.api.Services.NoteService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes(){

        return new ResponseEntity<List<Note>>(noteService.getAllNotes(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Map<String, String> payload){
        return new ResponseEntity<Note>(noteService.cretaeNote(payload.get("title"), payload.get("body"), payload.get("image")), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Note>> getNote(@PathVariable ObjectId id){
           return new ResponseEntity<Optional<Note>>(noteService.getNotebyId(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable ObjectId id){
        return new ResponseEntity<String>(noteService.deleteNote(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData> searchNote(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String body,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ){
        Pageable pageable = PageRequest.of(page != null ? page - 1 : 0, size != null ? size : 3);
        Page<Note> data = noteService.searchNote(title, body, pageable);
        List<Note> notes = data.getContent();
        int totalPages = data.getTotalPages();
        int currentPage = data.getNumber() + 1;
        ResponseData responseData = new ResponseData(notes, currentPage, totalPages);
        return new ResponseEntity<ResponseData>(responseData, HttpStatus.OK);
    }
}
