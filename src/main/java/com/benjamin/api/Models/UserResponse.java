package com.benjamin.api.Models;

import com.benjamin.api.Models.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private List<User> users;
    private int page;
    private int pages;
}
