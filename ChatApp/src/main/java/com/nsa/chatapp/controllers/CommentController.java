package com.nsa.chatapp.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.nsa.chatapp.model.AuthUserDetails;
import com.nsa.chatapp.model.Comment;
import com.nsa.chatapp.repo.CommentRepository;
import com.nsa.chatapp.utils.CommentState;


@RestController
//@EnableWebSecurity
@CrossOrigin
public class CommentController {

	@Autowired
	CommentRepository commentRepository;

	// svi mogu da gledaju odobrene komentare
	@RequestMapping(value = { "/comment", "/komentar" }, method = RequestMethod.GET)
	public ResponseEntity<Iterable<Comment>> getAllApproved() {

		return new ResponseEntity(commentRepository.findByCommentState(CommentState.APPROVED), HttpStatus.OK);
	}

	//admin moze da pregleda komentare na cekanju
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = { "/admin/comment-pending", "admin/komentar-nacekanju" }, method = RequestMethod.GET)
	public ResponseEntity<Iterable<Comment>> getAllPending() {

		return new ResponseEntity(commentRepository.findByCommentState(CommentState.PENDING), HttpStatus.OK);
	}

	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = { "/admin/comment-all", "/admin/komentar-svi" }, method = RequestMethod.GET)
	public ResponseEntity<Iterable<Comment>> getAllComment() {

		return new ResponseEntity(commentRepository.findAll(), HttpStatus.OK);
	}

	//admin moze da pregleda svaki komentar
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = { "/admin/comment/{id}", "/admin/komentar/{id}" }, method = RequestMethod.GET)
	public ResponseEntity<Comment> get(Authentication authentication, @PathVariable(value = "id") Integer id) {

		return new ResponseEntity(commentRepository.findById(id), HttpStatus.OK);
	}

	//korisnik moze da dobije kometar koji je odobren, tj. ne moze da dobije komentar koji je u obradi
	@RequestMapping(value = { "/comment/{id}", "/komentar/{id}" }, method = RequestMethod.GET)
	public ResponseEntity<Comment> get(@PathVariable(value = "id") Integer id) {
		
		Comment comment = commentRepository.findByCommentIDAndCommentState(id, CommentState.APPROVED);
		
		if(comment !=null)
			 return new ResponseEntity(comment, HttpStatus.OK);
		
		return new ResponseEntity(comment, HttpStatus.FORBIDDEN);

//		try {
//			Comment comment = commentRepository.findById(id).get();
//
//			if (comment != null && comment.getCommentState() != CommentState.PENDING)
//				return new ResponseEntity(comment, HttpStatus.OK);
//
//			return new ResponseEntity(null, HttpStatus.FORBIDDEN);
//		} catch (Exception e) {
//			return new ResponseEntity(null, HttpStatus.GONE);
//		}
	}

	// svi mogu da postavljaju komentare
	@Transactional
	@RequestMapping(value = { "/comment", "/komentar" }, method = RequestMethod.POST)
	public ResponseEntity<Comment> add(Comment comment) {

		comment.setCommentState(CommentState.PENDING);
		return new ResponseEntity(commentRepository.save(comment), HttpStatus.OK);
	}

	//samo admin moze da menja komentar
	@RequestMapping(value = { "/admin/comment", "/admin/komentar" }, method = RequestMethod.PUT)
	@Secured({ "ROLE_ADMIN" })
	public ResponseEntity<Comment> update(Authentication authentication, Comment comment) {
		AuthUserDetails userDetails = (AuthUserDetails) (authentication.getPrincipal());
		
		Integer adminID = userDetails.getAdminID();
		comment.setAdminID(adminID);

		return new ResponseEntity(commentRepository.save(comment), HttpStatus.OK);
	}

	@Transactional
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = { "/admin/comment/{id}", "/admin/komentar/{id}" }, method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable(value = "id") Integer id) {

		try {
			commentRepository.deleteById(id);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity("sve je u redu", HttpStatus.OK);
	}

	// svi mogu da vide komentare za datu vest
	@RequestMapping(value = { "/comment/news/{id}", "/komentar/vest/{id}" }, method = RequestMethod.GET)
	public ResponseEntity<Iterable<Comment>> getAllApprovedCommentsForGivenNews(
			@PathVariable(value = "id") Integer newsID) {
		return new ResponseEntity(commentRepository.findByNewsIDAndCommentState(newsID, CommentState.APPROVED),
				HttpStatus.OK);
	}

	// samo admini mogu da vide odbrene komentare za datu vest
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = { "/admin/comment/news/{id}", "/admin/komentar/vest/{id}" }, method = RequestMethod.GET)
	public ResponseEntity<Iterable<Comment>> getAllCommentForGivenNews(@PathVariable(value = "id") Integer newsID) {
		return new ResponseEntity(commentRepository.findByNewsID(newsID), HttpStatus.OK);
	}

	@Transactional
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = { "/admin/comment-approve/{id}",
			"/admin/komentar-odobri/{id}" }, method = RequestMethod.GET)
	public ResponseEntity<Comment> approveComment(Authentication authentication,
			@PathVariable(value = "id") Integer id) {

		Comment comment = commentRepository.findById(id).get();
		if (comment == null || comment.getCommentState() == CommentState.APPROVED) {
			return new ResponseEntity(comment, HttpStatus.GONE);
		}

		AuthUserDetails userDetails = (AuthUserDetails) (authentication.getPrincipal());
		Integer adminID = userDetails.getAdminID();
		comment.setAdminID(adminID);
		comment.setCommentState(CommentState.APPROVED);

		return new ResponseEntity(commentRepository.save(comment), HttpStatus.OK);

	}

}