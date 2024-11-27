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
                System.out.println("User was in the list");
                return user;
            }
        }

        if (!username.isBlank()) {
            System.out.println("User was tweaked");
            return createUser();
        }

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
        user.setUsername("user-created-on-the-fly");
        user.setPassword("password-created-on-the-fly");
        user.setEmail("test@email.com-created-on-the-fly");
        user.setRole("ROLE_USER");
        return user;
    }
}
