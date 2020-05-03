package com.nsa.chatapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nsa.chatapp.model.LoggedUser;
import com.nsa.chatapp.repo.LoggedUserRepository;


@RestController
public class AdminController {

	
	@Autowired
	private LoggedUserRepository authUserDetailsRepository;
	
	@RequestMapping(value = "/admin", method = RequestMethod.POST)
	public ResponseEntity<LoggedUser> putAdmin(LoggedUser admin) {
		//System.out.print(admin.getPassword());
		//
		//addA();
		return new ResponseEntity(authUserDetailsRepository.save(admin.encode()), HttpStatus.OK);
		
//		LoggedUser u = admin.encode();
//		
//		u = authUserDetailsRepository.add(u.getFullname(), u.getPassword(), u.getRole(), u.getUsername());
//		return new ResponseEntity(u, HttpStatus.OK);
	}
	@RequestMapping(value = "/a", method = RequestMethod.GET)
	void addA()
	{
		for(int i=1;i<1000;i++)
		{
			LoggedUser l = new LoggedUser(""+i, ""+i, "ROLE_LOGGEDUSER",""+i).encode();
			authUserDetailsRepository.save(l);
		}
	}
	@RequestMapping(value = "/admin/{username}", method = RequestMethod.GET)
	public ResponseEntity<LoggedUser> findAdmin(@PathVariable(value="username") String username ) {
		//System.out.print(admin.getPassword());
		//
		return new ResponseEntity(authUserDetailsRepository.findByUsername(username), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/allusers", method = RequestMethod.GET)
	public ResponseEntity<Iterable<LoggedUser>> getAllUsers() {
		//System.out.print(admin.getPassword());
		//
		 
		return new ResponseEntity(authUserDetailsRepository.findAll(), HttpStatus.OK);
	}
}
