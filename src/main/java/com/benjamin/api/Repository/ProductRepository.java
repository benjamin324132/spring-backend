package com.benjamin.api.Repository;

import com.benjamin.api.Models.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, ObjectId> {
    List<Product> findByCategory(String category);

}
