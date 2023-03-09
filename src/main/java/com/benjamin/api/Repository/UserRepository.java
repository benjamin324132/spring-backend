package com.benjamin.api.Repository;

import com.benjamin.api.Models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    //Optional<User> findByUsername(String username);
    Optional<User> getUserByEmail(String email);
}
