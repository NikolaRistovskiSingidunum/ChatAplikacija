package com.nsa.chatapp.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class MessageChat {

	public enum MessageType
	{
		TO_ME,FROM_ME;
	}
	public enum ContentType
	{
		FILE, TEXT;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer messageid; 
	
	@Column(insertable = false, updatable = false, columnDefinition = "DATETIME on update CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP", nullable = false)
	private LocalDateTime dateaccepted;
	
	//ovo moze biti null kada porukua nije jos dostavljenja
	@Column(insertable = true, updatable = true, nullable = true)
	private LocalDateTime datedeliverd;
	
	@Column(nullable = false, updatable = true, insertable = true)
	private Integer senderid;
	
	@Column(nullable = false, updatable = true, insertable = true)
	private Integer receiverid;

	@Column(nullable = false, updatable = true, insertable = true,columnDefinition = "varchar(1024)")
	private String text;

	//ovo treba refaktorisati - messagetype je tipa 
	@Transient
	private MessageType messagetype;
	
	//za sada moze da bude null i onda je podrazumevano tipa text 
	@Column(nullable = true, updatable = true, insertable = true)
	private ContentType contenttype;

	@javax.persistence.Transient
	@JsonIgnore
	private MultipartFile file;
	
	
	
	
	public ContentType getContenttype() {
		return contenttype;
	}

	public void setContenttype(ContentType contenttype) {
		this.contenttype = contenttype;
	}

	public MessageType getMessagetype() {
		return messagetype;
	}

	public void setMessagetype(MessageType messagetype) {
		this.messagetype = messagetype;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


	public Integer getMessageid() {
		return messageid;
	}

	
	
	public void setMessageid(Integer messageid) {
		this.messageid = messageid;
	}

	public LocalDateTime getDateaccepted() {
		return dateaccepted;
	}

	public void setDateaccepted(LocalDateTime dateaccepted) {
		this.dateaccepted = dateaccepted;
	}

	public LocalDateTime getDatedeliverd() {
		return datedeliverd;
	}

	public void setDatedeliverd(LocalDateTime datedeliverd) {
		this.datedeliverd = datedeliverd;
	}

	public Integer getSenderid() {
		return senderid;
	}

	public void setSenderid(Integer senderid) {
		this.senderid = senderid;
	}

	public Integer getReceiverid() {
		return receiverid;
	}

	public void setReceiverid(Integer receiverid) {
		this.receiverid = receiverid;
	}

	protected MessageChat() {
		super();
	}
	

	public MessageChat(Integer messageid, LocalDateTime dateaccepted, LocalDateTime datedeliverd, Integer senderid,
			Integer receiverid, String text, ContentType contenttype) {
		super();
		this.messageid = messageid;
		this.dateaccepted = dateaccepted;
		this.datedeliverd = datedeliverd;
		this.senderid = senderid;
		this.receiverid = receiverid;
		this.text = text;
		this.contenttype = contenttype;
	}

	public MessageChat(Integer senderid, Integer receiverid, String text) {
		super();
		this.senderid = senderid;
		this.receiverid = receiverid;
		this.text = text;
	}
	
	
	
	
}
