package org.example.stripeintegrationapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/success")
    public String success() {
        return "success";
    }

}
