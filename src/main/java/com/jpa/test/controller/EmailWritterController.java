package com.jpa.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.jpa.test.EmailRequest;
import com.jpa.test.service.EmailWritterService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmailWritterController {

	private final EmailWritterService emailWritterService;
	 
	@PostMapping("/generate")
	public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest){
		String response =emailWritterService.generateEmailReply(emailRequest);
		return ResponseEntity.ok(response);
	}
	@GetMapping({"", "/"})
	    public String testBrowser() {
	        return "✅ Smart Email Assistant API is Running!";
	    }
	
}
