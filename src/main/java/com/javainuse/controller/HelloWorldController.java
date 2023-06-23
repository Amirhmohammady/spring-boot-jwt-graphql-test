package com.javainuse.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@CrossOrigin()
public class HelloWorldController {

    @Secured("ROLE_ADMIN")
    @RequestMapping(path={"/hello"}, method = RequestMethod.GET)
    @ResponseBody
    public String hello() {
        return "Hello World";
    }
}
