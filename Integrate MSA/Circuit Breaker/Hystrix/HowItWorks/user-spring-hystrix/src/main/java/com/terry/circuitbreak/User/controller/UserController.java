package com.terry.circuitbreak.User.controller;

import java.util.ArrayList;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.terry.circuitbreak.Item.model.Item;
import com.terry.circuitbreak.User.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    GetItemCommand getItemCommand;
    
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@RequestMapping(value="/{name}",method=RequestMethod.GET)
	public List<User> getUsers(@PathVariable String name){
		logger.info("User service "+name);
		
		List<User> usersList = getItemCommand.getItem(name);
				
		return usersList;
		
	}
	
	public List<User> getUsersFallBack(String name){
		List<User> usersList = new ArrayList<User>();
		usersList.add(new User(name,"myemail@mygoogle.com"));
		
		return usersList;	
	}

}
