package com.nsa.chatapp.websocket;

public class SocketAuthentification {

	private String jsessionid;

	
	
	protected SocketAuthentification() {
		super();
	}


	

	protected SocketAuthentification(String jsessionid) {
		super();
		this.jsessionid = jsessionid;
	}




	public String getJsessionid() {
		return jsessionid;
	}



	public void setJsessionid(String jsessionid) {
		this.jsessionid = jsessionid;
	}

	

	
	
	

	
	
	
}
