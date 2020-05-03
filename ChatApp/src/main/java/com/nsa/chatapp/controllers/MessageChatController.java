package com.nsa.chatapp.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.tomcat.websocket.MessageHandlerResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nsa.chatapp.model.LoggedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsa.chatapp.model.AuthUserDetails;
import com.nsa.chatapp.model.Comment;
import com.nsa.chatapp.model.MessageChat;
import com.nsa.chatapp.model.MessageChat.ContentType;
import com.nsa.chatapp.model.MessageChat.MessageType;
import com.nsa.chatapp.repo.LoggedUserRepository;
import com.nsa.chatapp.repo.MessageChatRepository;
import com.nsa.chatapp.utils.CommentState;
import com.nsa.chatapp.websocket.WebSocketSessionInformation;

@RestController
@CrossOrigin
public class MessageChatController {

//	private final ReentrantLock lock = new ReentrantLock();
	
	@Autowired
	private LoggedUserRepository authUserDetailsRepository;
	
	@Autowired
	MessageChatRepository messageChatRepository;
	
	
	//transactional nije potrebno
	//@Transactional
	//@Transactional(isolation = Isolation.)
	@Secured({ "ROLE_LOGGEDUSER" })
	@RequestMapping(value = { "/message/{id}", "/poruku/{id}" }, method = RequestMethod.GET)
	public ResponseEntity<MessageChat> get(Authentication authentication,@PathVariable(value = "id") Integer id) throws IOException, InterruptedException {
		
		
	
		AuthUserDetails userDetails = (AuthUserDetails) (authentication.getPrincipal());
		

		System.out.println(userDetails.getAdminID() +  "  ADMINID");
		
		MessageChat m = messageChatRepository.findById(id).get();
		
		String s = new ObjectMapper().writeValueAsString(userDetails);
		System.out.println(s);
		
//		if (m == null || userDetails == null)
//			return new ResponseEntity(null, HttpStatus.NO_CONTENT);
//		else {
			
			
			System.out.println(userDetails.getAdminID() +  "  ADMINID " + m.getReceiverid() + " RECEIVER ID" + m.getSenderid() + " SENDER ID" );
			//Ako je u pitanju poruka tipa faj, onda ga i ucitaj
			if(m.getContenttype()==ContentType.FILE)
				m.setFile(loadFileFromName(m.getMessageid()));
			
			//markMessage(m, userDetails.getAdminID());
			
			//System.out.println( "Msg Type " + m.getMessagetype());
			synchronized (this) {
				
			
			if (m.getReceiverid() == userDetails.getAdminID())
				m.setMessagetype(MessageType.TO_ME);
			else if( m.getSenderid() == userDetails.getAdminID())
				m.setMessagetype(MessageType.FROM_ME);
			else
				System.out.println("Greska" + "adminID " + userDetails.getAdminID() +" receiverID:" + m.getReceiverid() + "senderID:" +  m.getSenderid() );
			
			}
			System.out.println( "Msg Type " + m.getMessagetype());
			
			System.out.println(new ObjectMapper().writeValueAsString(m));
			
			
			return new ResponseEntity(m, HttpStatus.OK);
			
			
//		}
		
		
	}
	
	//Uzmi 50 zadnjih poruka od ili ka datom prijatelju
	@Secured({ "ROLE_LOGGEDUSER" })
	@RequestMapping(value = { "/message-last/{friendid}", "/poruka-zadnje/{friendid}" }, method = RequestMethod.GET)
	public ResponseEntity<Iterable<MessageChat>> getAll(Authentication authentication,@PathVariable(value = "friendid")Integer friendid) {

		AuthUserDetails userDetails = (AuthUserDetails) (authentication.getPrincipal());

		Iterable<MessageChat> msgs = messageChatRepository.getLastMessages(userDetails.getAdminID(), friendid, 50);

		markMessages(msgs, userDetails.getAdminID());

		return new ResponseEntity(msgs, HttpStatus.OK);
	}
	
	
	@Transactional
	@RequestMapping(value = { "/message", "/poruka" }, method = RequestMethod.POST)
	public ResponseEntity<MessageChat> add(MessageChat message) {

		message.setContenttype(MessageChat.ContentType.TEXT);
		return new ResponseEntity(messageChatRepository.save(message), HttpStatus.OK);
	}
	
	@Secured({ "ROLE_LOGGEDUSER" })
	//@Transactional
	@RequestMapping(value = { "/message-proxy", "/poruka-proksi" }, method = RequestMethod.POST)
	public ResponseEntity<MessageChat> add(Authentication authentication,String text, Integer receiverid) {

		AuthUserDetails userDetails = (AuthUserDetails) (authentication.getPrincipal());
		
		
		
		
		//AdminDetails adminDetails = authUserDetailsRepository.findByUsername(userDetails.getUsername());
		
		MessageChat newMessage = new MessageChat(userDetails.getAdminID(), receiverid, text);
		newMessage.setContenttype(ContentType.TEXT);
		//newMessage = messageChatRepository.save(new MessageChat(userDetails.getAdminID(), receiverid, text));
		
		newMessage = messageChatRepository.save(newMessage);
		
		//prosledi poruku kome treba
		WebSocketSessionInformation.sendMessage(newMessage.getReceiverid(), newMessage.getMessageid(),
				com.nsa.chatapp.websocket.MessageSocketClient.MessageType.GET_MESSAGE);
		WebSocketSessionInformation.sendMessage(newMessage.getSenderid(), newMessage.getMessageid(),
				com.nsa.chatapp.websocket.MessageSocketClient.MessageType.GET_MESSAGE);
		
		//userDetails.get
		return new ResponseEntity(newMessage, HttpStatus.OK);
	}
	@Secured({ "ROLE_LOGGEDUSER" })
	//@Transactional
	@RequestMapping(value = { "/file-proxy", "/fajl-proksi" }, method = RequestMethod.POST)
	public void add(Authentication authentication,MultipartFile file, Integer receiverid, String text) throws IllegalStateException, IOException {
	
		System.out.println("fajl proksi metod je pozvan" + file + " receviver id" + receiverid);
		//nisam siguran da li je ovo potredno
		if(file==null)
			return;
		
		AuthUserDetails userDetails = (AuthUserDetails) (authentication.getPrincipal());
		MessageChat newMessage = new MessageChat(userDetails.getAdminID(), receiverid, ContentType.FILE);

		newMessage.setText(text);
		messageChatRepository.save(newMessage);
		
		file.transferTo(Paths.get(
				System.getProperty("user.dir") + "/src/main/resources/static/userfiles/" + newMessage.getMessageid()));
		
		
		
		//prosledi poruku kome treba
		WebSocketSessionInformation.sendMessage(newMessage.getReceiverid(), newMessage.getMessageid(),
				com.nsa.chatapp.websocket.MessageSocketClient.MessageType.GET_FILE);
		WebSocketSessionInformation.sendMessage(newMessage.getSenderid(), newMessage.getMessageid(),
				com.nsa.chatapp.websocket.MessageSocketClient.MessageType.GET_FILE);
		
	}
	
	//aktivni soketi i sesije
	@Transactional
	@RequestMapping(value = { "/active-sockets", "/aktivni-soketi" }, method = RequestMethod.GET)
	public ResponseEntity<List<String>> getAllSessions() {

		List<WebSocketSessionInformation> allConnections = WebSocketSessionInformation.getAllconnections();
		
		List<String> s = new ArrayList();
		for ( WebSocketSessionInformation w: allConnections)
		{
			s.add(w.getUserDetails().getUsername());
			s.add(w.getSessionInformation().isExpired()+"");
			System.out.println(w.getSessionID());
		}
		return new ResponseEntity(s, HttpStatus.OK);
	}
	
	//Obelezi poruku kao poslatu ili primljenu u odnosu na id - Ako je Id poslao onda je FROM_ME, a ako nije onda je TO_ME
	// ovo je Mrkonic oblilaznica dok ne shvatimo sta ce biti
	private void markMessages(Iterable<MessageChat> messages, Integer id)
	{

		for (MessageChat m : messages) 
				markMessage(m, id);
	}
	private void markMessage(MessageChat message, Integer id)
	{
		if (message.getReceiverid() == id)
			message.setMessagetype(MessageType.TO_ME);
		else
			message.setMessagetype(MessageType.FROM_ME);
	}

	
	private byte[] loadFileFromName(Integer fileID) throws IOException
	{
		//File file = new File( System.getProperty("user.dir") + "/src/main/resources/static/userfiles/" + 235 );
		String filePath = System.getProperty("user.dir") + "/src/main/resources/static/userfiles/" + 235 ;
		byte[] bFile = Files.readAllBytes(Paths.get(filePath));
		
		return bFile;
	}
	
	@RequestMapping(value = { "/file/{fileID}", "/file/{fileID}" }, method = RequestMethod.GET)
	public byte[] getFile(@PathVariable(value = "fileID") Integer fileID) throws IOException {
		String filePath = System.getProperty("user.dir") + "/src/main/resources/static/userfiles/" + fileID;
		byte[] bFile = Files.readAllBytes(Paths.get(filePath));

		return bFile;
	}
}
