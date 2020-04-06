package com.nsa.chatapp.websocket;

import java.util.ArrayList;

public class SocketMessage {

	
	
	private ArrayList<Integer> id;
	
	private String message;

	protected SocketMessage()
	{};
	
	public SocketMessage(ArrayList<Integer> id, String message) {
		super();
		this.id = id;
		this.message = message;
	}

	public ArrayList<Integer> getId() {
		return id;
	}

	public void setId(ArrayList<Integer> id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
