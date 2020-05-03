package com.nsa.chatapp.repo;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.nsa.chatapp.model.LoggedUser;
import com.nsa.chatapp.model.MessageChat;
import com.nsa.chatapp.model.AuthUserDetails;


public interface LoggedUserRepository extends CrudRepository<LoggedUser, Integer> {
	
    LoggedUser findByUsername(String username);
    
//    INSERT INTO table_name (column1, column2, column3, ...)
//    VALUES (value1, value2, value3, ...); 
	@Query(
			  value = "	insert into logged_user (fullname,password, role, username) values(?1,?2,?3,?4)"  
			  ,nativeQuery = true)
	LoggedUser add(String fullname, String password, String role, String username);
}
