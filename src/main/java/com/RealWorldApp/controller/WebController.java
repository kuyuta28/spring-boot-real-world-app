package com.RealWorldApp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class WebController {


    @GetMapping("/hello")
    public String hello(@RequestParam String name, Model model) {
        model.addAttribute("name", name);


        return "hello";
    }

    @GetMapping("") // Nếu người dùng request tới địa chỉ "/"
    public String index() {
        return "index"; // Trả về file index.html
    }

    @GetMapping("/reload") // Nếu người dùng request tới địa chỉ "/"
    public String reload() {
        return index();
    }
}
