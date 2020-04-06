package com.nsa.chatapp.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsa.chatapp.model.AuthUserDetails;
import com.nsa.chatapp.repo.CommentRepository;
import com.nsa.chatapp.utils.BeanUtil;
import com.nsa.chatapp.websocket.WebSocketSessionInformation;

/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */

public class ChatServer extends WebSocketServer {

	protected static final List<WebSocket> konekcije = new ArrayList<>();

	public ChatServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public ChatServer(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		konekcije.add(conn);
		conn.send("Welcome to the server!"); // This method sends a message to the new client
		broadcast("new connection: " + handshake.getResourceDescriptor()); // This method sends a message to all clients
																			// connected
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");

	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		broadcast(conn + " has left the room!");
		System.out.println(conn + " has left the room!");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		// broadcast( message );
		System.out.println("public void onMessage( WebSocket conn, String message )");
		System.out.println(message);
//		if(sessionRegistry != null)
//			System.out.println(sessionRegistry.getSessionInformation("AAA"))
//		
//		if (konekcije.size() >= 2) {
//			if (konekcije.get(0) == conn) {
//				konekcije.get(1).send("Poruku od 0 -----> Poruka od 1");
//			} else {
//				konekcije.get(0).send("Poruku od 1 -----> Poruka od 0");
//			}
//		}

		//new ObjectMapper().readVa
		System.out.println(message);
		
//		String sessionID = "";
//		
//		SocketAuthentification socketAuthentification = new ObjectMapper().readValue(message, SocketAuthentification.class);
		
		//SocketMessage socketMessage = new ObjectMapper().readValue(message, SocketMessage.class);
/*		
		try
		{
			SocketAuthentification socketAuthentification = new ObjectMapper().readValue(message, SocketAuthentification.class);
			System.out.println("Uspesno logovanje desirajilazija" + socketAuthentification.getJsessionid() );
			
			WebSocketSessionInformation.State state = WebSocketSessionInformation.registerNewSession(socketAuthentification.getJsessionid(), conn);
			
			if(state == WebSocketSessionInformation.State.UNSUCCESSFUL)
			{
				System.out.println("PLEASE LOGIN");
				conn.close();
			}
			else
			{
				System.out.println("Uspesno logovanje");
			}
		}
		catch(Exception e)
		{
			System.out.println("Nesuspesno logovanje deserijalizacije");
			System.out.println(e.getMessage());
			
		}
		try
		{
			boolean clientLogedIn = WebSocketSessionInformation.isClientLogedIn(conn);
			SocketMessage socketMessage = new ObjectMapper().readValue(message, SocketMessage.class);
			if(clientLogedIn)
			{
				//Ovde traba da bude i klijent koji salje poruku
				ArrayList< WebSocketSessionInformation>  allToRespond = WebSocketSessionInformation.getWebSocketSessionInformation(socketMessage.getId()); 
				
				for( WebSocketSessionInformation w : allToRespond )
				{
					w.getWebsocket().send(socketMessage.getMessage());
				}
			}
			
			
			
			//if(state != State.UNSUCCESSFUL)
			System.out.println("stigla je poruka od klijenta" + socketMessage.getMessage());
		}
		catch(Exception e)
		{
			System.out.println("Nesuspesno poruka deserijalizacije");
		}

		
		System.out.flush();
		*/
//		try {
//
//			System.out.println(conn + ": " + message);
//			SessionRegistry sessionRegistry = BeanUtil.getBean(SessionRegistry.class);
//			System.out.println("all prinicples" + sessionRegistry.getAllPrincipals().size());
////		System.out.println("session infomration" + sessionRegistry.getSessionInformation(message));	
//			// System.out.println();
//			if (sessionRegistry.getSessionInformation(message) != null)
//			{
//				System.out.println("session registry nije null");
//				
//			AuthUserDetails user = (AuthUserDetails) sessionRegistry.getSessionInformation(message).getPrincipal();
//			
//			if (user == null) {
//				//conn.close();
//				//conn.close(code, message);
//				System.out.println("conn.closeConnection(3, \"ulogujte se\"); user je null");
//				conn.close();
//				
//				
//			} else {
//				System.out.println(user.getUsername());
//			}
//			}
//			else {
//			System.out.println("conn.closeConnection(3, \"ulogujte se\");");
//			konekcije.remove(conn);
//			conn.close();
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		broadcast(message.array());
		System.out.println("public void onMessage( WebSocket conn, ByteBuffer message )");
		System.out.println(conn + ": " + message);
	}

	public static void start(String[] args) throws InterruptedException, IOException {
		int port = 8887; // 843 flash policy port
//		try {
//			port = Integer.parseInt( args[ 0 ] );
//		} catch ( Exception ex ) {
//		}
		ChatServer s = new ChatServer(port);
		s.start();
		System.out.println("ChatServer started on port: " + s.getPort());

		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String in = sysin.readLine();
			s.broadcast(in);
			if (in.equals("exit")) {
				s.stop(1000);
				break;
			}
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		if (conn != null) {
			// some errors like port binding failed may not be assignable to a specific
			// websocket
		}
	}

	@Override
	public void onStart() {
		System.out.println("Server started!");
		setConnectionLostTimeout(0);
		setConnectionLostTimeout(100);
	}

}