package com.terry.circuitbreak.User.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.terry.circuitbreak.Item.model.Item;
import com.terry.circuitbreak.User.model.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class GetItemCommand {
	
	@Autowired
	RestTemplate restTemplate;
	
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

	// GetItem command
	@HystrixCommand(fallbackMethod = "getFallback")
	public List<User> getItem(String name)  {
		List<User> usersList = new ArrayList<User>();
		
		List<Item> itemList = (List<Item>)restTemplate.exchange("http://localhost:8082/users/"+name+"/items"
										,HttpMethod.GET,null
										,new ParameterizedTypeReference<List<Item>>() {}).getBody();
		usersList.add(new User(name,"myemail@mygoogle.com",itemList));
				
		return usersList;
	}
	
	// fall back method
	// it returns default result
	@SuppressWarnings("unused")
	public List<User> getFallback(String name){
		List<User> usersList = new ArrayList<User>();
		usersList.add(new User(name,"myemail@mygoogle.com"));
		
		return usersList;
	}
}
