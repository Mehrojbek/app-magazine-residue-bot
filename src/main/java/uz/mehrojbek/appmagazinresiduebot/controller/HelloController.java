package uz.mehrojbek.appmagazinresiduebot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hi(){
        return "Salom";
    }

}
