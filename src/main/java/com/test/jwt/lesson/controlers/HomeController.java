package com.test.jwt.lesson.controlers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the Home Page!";
    }

    @GetMapping("/store")
    public String store() {
        return "Welcome to the store Page!";
    }

    @GetMapping("/admion/home")
    public String getAdminHome() {
        return "Welcome to Admin home!";
    }

    @GetMapping("/client/home")
    public String getClientHome() {
        return "Welcome client Home Page!";
    }


}
