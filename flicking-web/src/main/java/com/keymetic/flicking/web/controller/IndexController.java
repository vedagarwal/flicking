package com.keymetic.flicking.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.keymetic.flicking.rest.service.EmailService;

@Controller
public class IndexController {
	
	@Autowired
	private EmailService emailService;
	
	@RequestMapping(value={"/","/index","/home"})
    public String homePage() {  
        return "index";
    }	
	
	@RequestMapping(value="/errors/404")
    public String handle404() {
    	return "error404";
    }
	
	
	@RequestMapping(value="/errors/500")
    public String handle500() {
    	return "error500";
    }

}
