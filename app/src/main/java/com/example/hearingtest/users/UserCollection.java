package com.example.hearingtest.users;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserCollection {
    private List<User> users;

    public UserCollection(){
        users = new ArrayList<>();
    }
}
