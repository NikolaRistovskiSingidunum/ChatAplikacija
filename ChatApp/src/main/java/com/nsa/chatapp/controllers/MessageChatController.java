package com.nsa.chatapp.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nsa.chatapp.model.LoggedUser;
import com.nsa.chatapp.model.AuthUserDetails;
import com.nsa.chatapp.model.Comment;
import com.nsa.chatapp.model.MessageChat;
import com.nsa.chatapp.model.MessageChat.MessageType;
import com.nsa.chatapp.repo.LoggedUserRepository;
import com.nsa.chatapp.repo.MessageChatRepository;
import com.nsa.chatapp.utils.CommentState;
import com.nsa.chatapp.websocket.WebSocketSessionInformation;

@RestController
@CrossOrigin
public class MessageChatController {

	
	@Autowired
	private LoggedUserRepository authUserDetailsRepository;
	
	@Autowired
	MessageChatRepository messageChatRepository;
	
	//transactional nije potrebno
	@Transactional
	@Secured({ "ROLE_LOGGEDUSER" })
	@RequestMapping(value = { "/message/{id}", "/poruku/{id}" }, method = RequestMethod.GET)
	public ResponseEntity<MessageChat> get(Authentication authentication,@PathVariable(value = "id") Integer id) {
		
		AuthUserDetails userDetails = (AuthUserDetails) (authentication.getPrincipal());
		

		
		MessageChat m = messageChatRepository.findById(id).get();
		
		if(m==null || userDetails == null)
		return new ResponseEntity(m,HttpStatus.NO_CONTENT);
		else
		{
			if(m.getReceiverid() == userDetails.getAdminID())
			m.setMessagetype(MessageType.TO_ME);
			else
			m.setMessagetype(MessageType.FROM_ME);
			
			return new ResponseEntity(m, HttpStatus.OK);
		}
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

		
		return new ResponseEntity(messageChatRepository.save(message), HttpStatus.OK);
	}
	
	@Secured({ "ROLE_LOGGEDUSER" })
	//@Transactional
	@RequestMapping(value = { "/message-proxy", "/poruka-proksi" }, method = RequestMethod.POST)
	public ResponseEntity<MessageChat> add(Authentication authentication,String text, Integer receiverid) {

		AuthUserDetails userDetails = (AuthUserDetails) (authentication.getPrincipal());
		
		
		
		
		//AdminDetails adminDetails = authUserDetailsRepository.findByUsername(userDetails.getUsername());
		
		MessageChat newMessage = new MessageChat(userDetails.getAdminID(), receiverid, text);
		newMessage = messageChatRepository.save(new MessageChat(userDetails.getAdminID(), receiverid, text));
		
		
		//prosledi poruku kome treba
		WebSocketSessionInformation.sendMessage(newMessage.getReceiverid(), newMessage.getMessageid());
		WebSocketSessionInformation.sendMessage(newMessage.getSenderid(), newMessage.getMessageid());
		
		//userDetails.get
		return new ResponseEntity(newMessage, HttpStatus.OK);
	}
	@Transactional
	@RequestMapping(value = { "/active-sockets", "/aktivni-soketi" }, method = RequestMethod.GET)
	public ResponseEntity<List<String>> getAllSessions() {

		List<WebSocketSessionInformation> allConnections = WebSocketSessionInformation.getAllconnections();
		
		List<String> s = new ArrayList();
		for ( WebSocketSessionInformation w: allConnections)
		{
			s.add(w.getUserDetails().getUsername());
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

}
