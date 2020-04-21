package com.nsa.chatapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.nsa.chatapp.model.Comment;
import com.nsa.chatapp.model.MessageChat;
import com.nsa.chatapp.utils.CommentState;

public interface MessageChatRepository extends JpaRepository<MessageChat, Integer> {
	
//	Iterable<Comment> findByAdminID(Integer adminID);
//	
//	Iterable<Comment> findByNewsID(Integer newsID);
//	
//	Iterable<Comment> findByCommentState(CommentState commentState);
//
//	
//	Iterable<Comment> findByNewsIDAndCommentState(Integer newsID, CommentState commentState);
//	
//	Comment findByCommentIDAndCommentState(Integer commentID, CommentState commentState );
	
//	select * from message_chat
//	where (senderid=2 and receiverid=4) or (senderid=4 and receiverid=2)
//	order by messageid desc LIMIT 10
		@Query(
			  value = "	select * from message_chat\r\n" + 
			  		"	where (senderid=?1 and receiverid=?2) or (senderid=?2 and receiverid=?1)\r\n" + 
			  		"	order by messageid desc LIMIT ?3" , 
			  nativeQuery = true)
	Iterable<MessageChat> getLastMessages(Integer friend1, Integer friend2, Integer last);
}
