package com.benjamin.api.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    @Id
    private ObjectId id;

    private String title;

    private String image;

    private String body;

    public Note(String title,String body,String image){
        this.body = body;
        this.title = title;
        this.image = image;
    }
}
