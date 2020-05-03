package com.nsa.chatapp.websocket;


//ovu porku saljemo klijentu da ga obavestimo koji poruku treba da procita


public class MessageSocketClient {
	public enum MessageType 
	{
		GET_MESSAGE, GET_MAIL, GET_FILE;
	}
	
	private MessageType messagetype;

	private Integer messageid;

	public MessageType getMessagetype() {
		return messagetype;
	}

	public void setMessagetype(MessageType messagetype) {
		this.messagetype = messagetype;
	}

	public Integer getMessageid() {
		return messageid;
	}

	public void setMessageid(Integer messageid) {
		this.messageid = messageid;
	}

	protected MessageSocketClient(MessageType messagetype, Integer messageid) {
		super();
		this.messagetype = messagetype;
		this.messageid = messageid;
	}
	
	
	
	
	
	
	
}
