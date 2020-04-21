package com.nsa.chatapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nsa.chatapp.model.LoggedUser;
import com.nsa.chatapp.model.AuthUserDetails;
import com.nsa.chatapp.repo.LoggedUserRepository;

@Service
public class AuthService implements UserDetailsService {

	@Autowired
	LoggedUserRepository authUserDetailsRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		LoggedUser adminDetails = authUserDetailsRepository.findByUsername(username);
		return new AuthUserDetails(adminDetails);
	}

}
