package com.benjamin.api.Services;

import com.benjamin.api.Models.User;
import com.benjamin.api.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public Page<User> getAllUsers(String keyword, Pageable pageable){
        Query query = new Query().with(pageable);
        query.fields().exclude("password");
        List<Criteria> criteria = new ArrayList<>();

        if(keyword != null && !keyword.isEmpty()){
            criteria.add(Criteria.where("name").regex(keyword, "i"));
            //criteria.add(Criteria.where("email").regex(keyword, "i"));
            //criteria.add(Criteria.where("location").regex(keyword, "i"));
        }

        if(!criteria.isEmpty()) {
            query.addCriteria(new Criteria()
                    .andOperator(criteria.toArray(new Criteria[0])));
        }

        Update update = new Update();
        update.unset("password");


        return PageableExecutionUtils.getPage(
                mongoTemplate.find(query, User.class),
                pageable,
                () -> mongoTemplate.count(query.skip(0).limit(0), User.class)
        );
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public User createUser(User user){
        return userRepository.insert(user);
    }

    public Optional<User> getUserById(ObjectId id){
        return userRepository.findById(id);
    }


    public User updateUser(ObjectId id, Map<String, String> payload){
        Optional<User> userExist = userRepository.findById(id);
        if(!userExist.isPresent()){
            return null;
        }

        User user = userExist.get();
        if(payload.get(("name")) != null){
            user.setName(payload.get(("name")));
        }
        if(payload.get(("bio")) != null){
            user.setBio(payload.get(("bio")));
        }
        if(payload.get(("location")) != null){
            user.setLocation(payload.get(("location")));
        }

       return userRepository.save(user);
    }

    public String deleteUserById(ObjectId id){
        Optional<User> userExist = userRepository.findById(id);
        if(!userExist.isPresent()){
            return "User not found";
        }

        userRepository.delete(userExist.get());

        return "User deleted";
    }

}
