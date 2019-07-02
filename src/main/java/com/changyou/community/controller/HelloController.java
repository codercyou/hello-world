package com.changyou.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HelloController {


    /*@RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        System.out.println("hello spring...");
        return "Hello Springboot" ;
    }*/

    //@ResponseBody
    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String show(String username,Model model){
        model.addAttribute("username",username);
        model.addAttribute("uid","123456789");
        model.addAttribute("name","Jerry");
        return "show";
    }

}
