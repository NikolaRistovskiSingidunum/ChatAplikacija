package com.nsa.chatapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nsa.chatapp.model.AdminDetails;
import com.nsa.chatapp.repo.AuthUserDetailsRepository;


@RestController
public class AdminController {

	
	@Autowired
	private AuthUserDetailsRepository authUserDetailsRepository;
	
	@RequestMapping(value = "/admin", method = RequestMethod.POST)
	public ResponseEntity<AdminDetails> putAdmin(AdminDetails admin) {
		//System.out.print(admin.getPassword());
		//
		return new ResponseEntity(authUserDetailsRepository.save(admin.encode()), HttpStatus.I_AM_A_TEAPOT);
	}
	
	@RequestMapping(value = "/admin/{username}", method = RequestMethod.GET)
	public ResponseEntity<AdminDetails> findAdmin(@PathVariable(value="username") String username ) {
		//System.out.print(admin.getPassword());
		//
		return new ResponseEntity(authUserDetailsRepository.findByUsername(username), HttpStatus.I_AM_A_TEAPOT);
	}
	
	@RequestMapping(value = "/allusers", method = RequestMethod.GET)
	public ResponseEntity<Iterable<AdminDetails>> getAllUsers() {
		//System.out.print(admin.getPassword());
		//
		 
		return new ResponseEntity(authUserDetailsRepository.findAll(), HttpStatus.OK);
	}
}
