package com.nsa.chatapp.websocket;

import java.util.ArrayList;
import java.util.List;

import com.nsa.chatapp.model.AuthUserDetails;

/*Ovo moze da bude grupna chet soba ili moze da bude za minumm dva korisnika*/

public class ChatRoom {

	public enum RoomStatus {
		ALLRADY_EXISTS, ERROR, OK;
	}
	
	public enum RoomType
	{
		PRIVATE,PUBLIC;
	}
	
	private static final List<ChatRoom> chatRooms = new ArrayList<>();
	
	private static int id;
	private String name;
	
	private RoomType roomType   = RoomType.PRIVATE;
	
	private boolean dirty = false;
	
	private String roomOwner;
	
	private final List<AuthUserDetails> memebers = new ArrayList();
	
	private ChatRoom() {};
	
	public static RoomStatus createChatRoom(String user1, String user2, RoomType rootType   )
	{
		
		
		return RoomStatus.OK;
	}
	
	private static RoomStatus checkForChatRoom(AuthUserDetails user1, AuthUserDetails user2, RoomType rootType   )
	{
		
		return RoomStatus.OK;
	}
	
//	private static 
	
	
}
