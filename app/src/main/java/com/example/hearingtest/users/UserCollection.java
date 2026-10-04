package com.example.hearingtest.users;

import java.util.ArrayList;
import java.util.List;

public class UserCollection {
    public List<User> users;

    public UserCollection(){
        users = new ArrayList<>();
    }

    public List<User> getUsers(){return users;}
}
