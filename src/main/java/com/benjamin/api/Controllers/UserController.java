package com.benjamin.api.Controllers;

import com.benjamin.api.Models.ResponseData;
import com.benjamin.api.Models.User;
import com.benjamin.api.Models.UserResponse;
import com.benjamin.api.Services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Optional<User>> sigUp(@RequestBody Map<String, String> payload){
        String email = payload.get("email");
        Optional<User> userExist = userService.getUserByEmail(email);

        if(userExist.isPresent()){
            return new ResponseEntity("Email already in use", HttpStatus.BAD_REQUEST);
        }

        String pw_hash = BCrypt.hashpw(payload.get("password"), BCrypt.gensalt(10));
        User user = new User();
        user.setName(payload.get("name"));
        user.setEmail(payload.get("email"));
        user.setPassword(pw_hash);
        user.setBio(payload.get("bio"));
        user.setLocation(payload.get("location"));
        user.setCreatedAt(Instant.now().toString());
        user.setUpdatedAt(Instant.now().toString());
        User createdUser = userService.createUser(user);
        createdUser.setPassword(null);
        return new ResponseEntity(createdUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Optional<User>> logIn(@RequestBody Map<String, String> payload){
        String email = payload.get("email");
        String password = payload.get("password");
        Optional<User> user = userService.getUserByEmail(email);

        if(!user.isPresent()){
            return new ResponseEntity("User not found", HttpStatus.BAD_REQUEST);
        }

        boolean pw_hash = BCrypt.checkpw(password, user.get().getPassword());
        if(!pw_hash){
            return new ResponseEntity("Password incorrect", HttpStatus.BAD_REQUEST);
        }
        user.get().setPassword(null);
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer page
    ){
        Pageable pageable = PageRequest.of(page != null ? page - 1 : 0, 15);
        Page<User> data = userService.getAllUsers(keyword, pageable);
        List<User> users = data.getContent();
        int totalPages = data.getTotalPages();
        int currentPage = data.getNumber() + 1;
        UserResponse responseData = new UserResponse(users, currentPage, totalPages);
        return new ResponseEntity<UserResponse>(responseData, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable ObjectId id){

        return new ResponseEntity<Optional<User>>(userService.getUserById(id), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable ObjectId id, @RequestBody Map<String, String> payload){

        return new ResponseEntity<User>(userService.updateUser(id, payload), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable ObjectId id){

        return new ResponseEntity<String>(userService.deleteUserById(id), HttpStatus.OK);

    }
}
