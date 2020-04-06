package com.nsa.chatapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Friend {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer friendID;
	
//	@ManyToOne
//	@JoinColumn(name = "id", table = "AdminDetails")
	@Column(nullable = false, updatable = true, insertable = true)
	private Integer friend1;
	
//	@ManyToOne
//	@JoinColumn(name = "id", table = "AdminDetails")
	@Column(nullable = false, updatable = true, insertable = true)
	private Integer friend2;

}
