package com.nsa.chatapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nsa.chatapp.model.LoggedUser;
import com.nsa.chatapp.model.AuthUserDetails;
import com.nsa.chatapp.model.Comment;
import com.nsa.chatapp.repo.LoggedUserRepository;

@RestController
@CrossOrigin
public class FriendController {

	@Autowired
	private LoggedUserRepository authUserDetailsRepository;
	
	@Secured({ "ROLE_LOGGEDUSER" })
	@RequestMapping(value = { "/whoAmI", "/koSamJa" }, method = RequestMethod.GET)
	public ResponseEntity<LoggedUser> get(Authentication authentication) {
		
		AuthUserDetails userDetails = (AuthUserDetails) (authentication.getPrincipal());
		
		
		
		LoggedUser adminDetails = authUserDetailsRepository.findByUsername(userDetails.getUsername());
		return new ResponseEntity(adminDetails,HttpStatus.OK);
	}
	
}
