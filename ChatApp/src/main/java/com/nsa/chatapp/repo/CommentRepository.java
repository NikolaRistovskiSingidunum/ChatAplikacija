package com.nsa.chatapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.nsa.chatapp.model.Comment;
import com.nsa.chatapp.utils.CommentState;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	Iterable<Comment> findByAdminID(Integer adminID);
	
	Iterable<Comment> findByNewsID(Integer newsID);
	
	Iterable<Comment> findByCommentState(CommentState commentState);

	
	Iterable<Comment> findByNewsIDAndCommentState(Integer newsID, CommentState commentState);
	
	Comment findByCommentIDAndCommentState(Integer commentID, CommentState commentState );
}
