package com.nsa.chatapp.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.nsa.chatapp.utils.CommentState;




@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer commentID; 

	@Column(insertable = false, updatable = false, columnDefinition = "DATETIME on update CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP", nullable = false)
	private LocalDateTime date;

	@Column(nullable = false, updatable = true, insertable = true, length = 1000)
	private String text;

	@Column(nullable = false, updatable = true, insertable = true)
	private Integer newsID;

	@Column(nullable = false, updatable = true, insertable = true)
	private Integer adminID;

	@Column(nullable = false, updatable = true, insertable = true, length = 50)
	private String author;
	/* Getter and setters */
	@Column(nullable = false, updatable = true, insertable = true)
	private CommentState commentState = CommentState.PENDING;

	protected Comment() {
	}


	public Comment(LocalDateTime date, String text, Integer newsID, Integer adminID, String author,
			CommentState commentState) {
		super();
		this.date = date;
		this.text = text;
		this.newsID = newsID;
		this.adminID = adminID;
		this.author = author;
		this.commentState = commentState;
	}


	public Integer getCommentID() {
		return commentID;
	}

	public void setCommentID(Integer commentID) {
		this.commentID = commentID;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}



	public Integer getNewsID() {
		return newsID;
	}

	public void setNewsID(Integer newsID) {
		this.newsID = newsID;
	}

	public Integer getAdminID() {
		return adminID;
	}

	public void setAdminID(Integer adminID) {
		this.adminID = adminID;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}


	public CommentState getCommentState() {
		return commentState;
	}


	public void setCommentState(CommentState commentState) {
		this.commentState = commentState;
	};

	

}
