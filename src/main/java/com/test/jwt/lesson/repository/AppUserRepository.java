package com.test.jwt.lesson.repository;

import com.test.jwt.lesson.models.AppUser;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AppUserRepository {

    public List<AppUser> users = new ArrayList<>();

    public AppUser findByUsername(String username) {

        for(AppUser user: users){
            if(user.getUsername().equals(username)){
                return user;
            }
        }

//        if (username.equals("user")) {
//            return createUser();
//        }

        return null;
    }

    public AppUser findByEmail(String email) {
        if (email.equals("test@email.com")) {
            return createUser();
        }

        return null;
    }

    public AppUser save(AppUser user) {
        users.add(user);

        return null;
    }
    private AppUser createUser(){
        AppUser user = new AppUser();
        user.setUsername("user");
        user.setPassword("password");
        user.setEmail("test@email.com");
        user.setRole("ROLE_USER");
        return user;
    }
}
