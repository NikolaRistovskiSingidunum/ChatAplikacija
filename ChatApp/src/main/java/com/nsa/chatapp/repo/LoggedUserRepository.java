package com.nsa.chatapp.repo;

import java.util.ArrayList;
import java.util.Collection;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.nsa.chatapp.model.LoggedUser;
import com.nsa.chatapp.model.AuthUserDetails;


public interface LoggedUserRepository extends CrudRepository<LoggedUser, Integer> {
	
    LoggedUser findByUsername(String username);

}
