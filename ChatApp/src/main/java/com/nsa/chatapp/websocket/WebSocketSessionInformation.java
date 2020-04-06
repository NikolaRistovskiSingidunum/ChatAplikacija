package com.nsa.chatapp.websocket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.java_websocket.WebSocket;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import com.nsa.chatapp.model.AuthUserDetails;
import com.nsa.chatapp.utils.BeanUtil;



public class WebSocketSessionInformation implements Comparable<WebSocketSessionInformation> {
	
	/*jedna korisnik mozda moze da bude ulogovan na vise mesta, na sva ta mesta traba da prosledimo prouku*/
	
	
	public enum State{
		ALLREADY_EXISTS, UNSUCCESSFUL, SUCCESS;
	}
	public enum RemovalInfo
	{
		NO_SESSION_REMOVED, SESSION_REMOVED;  
	}
	
	private static final List<WebSocketSessionInformation> allConnections = new ArrayList(); 
	
	private WebSocket websocket;
	private String sessionID;
	private AuthUserDetails userDetails;
	private SessionInformation sessionInformation;
	


	private WebSocketSessionInformation(WebSocket websocket, String sessionID, AuthUserDetails userDetails,
			SessionInformation sessionInformation) {
		
		this.websocket = websocket;
		this.sessionID = sessionID;
		this.userDetails = userDetails;
		this.sessionInformation = sessionInformation;
	}

	public static State registerNewSession(String sessionID, WebSocket websocket)
	{
		removeBadSessions();
		
		
		SessionRegistry sessionRegistry = BeanUtil.getBean(SessionRegistry.class);
		if(sessionRegistry==null)
			return State.UNSUCCESSFUL;
		
		SessionInformation si =  sessionRegistry.getSessionInformation(sessionID);
		
		if(si==null || si.isExpired()==true)
			return State.UNSUCCESSFUL;
		
		AuthUserDetails user = (AuthUserDetails) si.getPrincipal();
		
		if(user==null)
			return State.UNSUCCESSFUL;
		
		
		WebSocketSessionInformation newSession = new WebSocketSessionInformation(websocket, sessionID, user, si);
		
		int p =search(sessionID);
	
		if(p>=0)
			return State.ALLREADY_EXISTS;
			
		
		
		allConnections.add(newSession);
		
		return State.SUCCESS;
	}

	private static int search(String sesssinID)
	{
		
		for(int i =0 ; i< allConnections.size(); i++)
			if(allConnections.get(i).sessionID.equals(sesssinID))
				return i;
			
			return -1;
	}
	public static boolean isClientLogedIn(WebSocket websocket)
	{
		for(WebSocketSessionInformation w: allConnections)
		{
			if(w.websocket!=null && w.websocket == websocket && w.websocket.isClosing() == false && w.websocket.isClosed()==false)
			{
				return true;
			}
		}
		
		return false;
	}
	public static ArrayList<WebSocketSessionInformation> getWebSocketSessionInformation(int userID)
	{
		ArrayList<WebSocketSessionInformation> allSession = new ArrayList<>();
		
		for(WebSocketSessionInformation w: allConnections)
		{
			if(w.userDetails.getAdminID() == userID)
			{
				allSession.add(w);
			}
		}
		
		return allSession;
	}
	
	public static ArrayList<WebSocketSessionInformation> getWebSocketSessionInformation(ArrayList<Integer>  usersID)
	{
		ArrayList<WebSocketSessionInformation> allSession = new ArrayList<>();
		for(Integer id:usersID)
			allSession.addAll(getWebSocketSessionInformation(usersID));
		
		return allSession;
	}	
	private static RemovalInfo removeBadSessions()
	{
		Set<WebSocketSessionInformation> removeList = new HashSet<>(); 
		RemovalInfo returnValue = RemovalInfo.NO_SESSION_REMOVED;
		
		
		for(WebSocketSessionInformation w: allConnections)
		{
			if(w.websocket == null || w.sessionID == null || w.sessionInformation == null || w.userDetails == null  )
			{
				removeList.add(w);
				//returnValue = RemovalInfo.SESSION_REMOVED;
			}
			if(w.sessionInformation!=null && w.sessionInformation.isExpired() == true)
			{
				removeList.add(w);
				//returnValue = RemovalInfo.SESSION_REMOVED;
			}
			
		}
		if(allConnections.removeAll(removeList))
		{
			returnValue = RemovalInfo.SESSION_REMOVED;
			//System.out.println("SESSIONS UPDATE STATUS")
			
		}
		
		System.out.println("SESSIONS UPDATE STATUS" + returnValue.toString() );
		
		return returnValue;
	}
	
	public WebSocket getWebsocket() {
		return websocket;
	}



	public void setWebsocket(WebSocket websocket) {
		this.websocket = websocket;
	}



	public String getSessionID() {
		return sessionID;
	}



	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}



	public AuthUserDetails getUserDetails() {
		return userDetails;
	}



	public void setUserDetails(AuthUserDetails userDetails) {
		this.userDetails = userDetails;
	}

	


	public SessionInformation getSessionInformation() {
		return sessionInformation;
	}

	public void setSessionInformation(SessionInformation sessionInformation) {
		this.sessionInformation = sessionInformation;
	}

	@Override
	public int compareTo(WebSocketSessionInformation o) {
		// TODO Auto-generated method stub
		return this.sessionID.compareTo(o.sessionID);
		
	}




	
	
	

}
