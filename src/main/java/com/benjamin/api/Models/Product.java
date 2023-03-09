package com.benjamin.api.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private ObjectId id;

    private String title;
    private String image;
    private String description;
    private String category;
    private double price;
    private Integer quantity;
    @CreatedDate
    private  String createdAt;
    @LastModifiedDate
    private  String updatedAt;
}
