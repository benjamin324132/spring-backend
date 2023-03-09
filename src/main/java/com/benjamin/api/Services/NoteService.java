package com.benjamin.api.Services;

import com.benjamin.api.Models.Note;
import com.benjamin.api.Repository.NoteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Note> getAllNotes(){
       return noteRepository.findAll();
    }

    public Optional<Note> getNotebyId(ObjectId id){
        return noteRepository.findById(id);
    }

    public Note cretaeNote(String title, String body, String image){
        Note note = noteRepository.insert(new Note(title, body, image));
        return note;
    }

    public String deleteNote(ObjectId id){
        noteRepository.deleteById(id);
        return "Deleted succesfully";
    }

    public Page<Note> searchNote(String title, String body, Pageable pageable) {

        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();

        if(title != null && !title.isEmpty())
            criteria.add(Criteria.where("title").regex(title, "i"));

        if(body != null && !body.isEmpty())
            criteria.add(Criteria.where("body").regex(body, "i"));


        if(!criteria.isEmpty()) {
            query.addCriteria(new Criteria()
                    .andOperator(criteria.toArray(new Criteria[0])));
        }
        return PageableExecutionUtils.getPage(
                mongoTemplate.find(query, Note.class),
                pageable,
                () -> mongoTemplate.count(query.skip(0).limit(0), Note.class)
        );
    }
}
