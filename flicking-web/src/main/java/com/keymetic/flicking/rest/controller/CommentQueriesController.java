package com.keymetic.flicking.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.keymetic.flicking.core.entity.Comment;
import com.keymetic.flicking.core.events.CommentDetailsEvent;
import com.keymetic.flicking.core.events.CommentListEvent;
import com.keymetic.flicking.core.exception.CommentNotFoundException;
import com.keymetic.flicking.core.service.CommentService;
import com.keymetic.flicking.core.vo.CommentVO;

@Controller
@RequestMapping("/api/movies/{id}/comments")
public class CommentQueriesController {
	
	@Autowired
	private CommentService commentService;
	
	@RequestMapping(method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CommentVO>> getLatestComments(@PathVariable Long id,@RequestParam(defaultValue = "10") int limit,@RequestParam(defaultValue = "0") int offset) {
		
		CommentListEvent commentListEvent = commentService.getLatestComments(id, limit, offset);
		
		if (!commentListEvent.isEntityFound()) {
			return new  ResponseEntity<List<CommentVO>>(HttpStatus.NOT_FOUND);
		}
		
		List<CommentVO> commentsResult = null;
		List<Comment> comments = commentListEvent.getComments();
		
		if(comments != null){
			commentsResult = CommentVO.toCommentVOList(comments);
		}		
		
		return new ResponseEntity<List<CommentVO>>(commentsResult, HttpStatus.OK);
	}
	
	
	 	@RequestMapping(method = RequestMethod.GET, value = "/{cid}",produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<CommentVO> viewComment(@PathVariable Long cid) {

	        CommentDetailsEvent details = commentService.getCommentById(cid);

	        if (!details.isEntityFound()) {
	            //return new ResponseEntity<CommentVO>(HttpStatus.NOT_FOUND);
	        	throw new CommentNotFoundException(cid);
	        }

	        Comment comment = details.getComment();
	        CommentVO commentVO = null;
			if(comment != null){
				commentVO = new CommentVO(comment);
			}			
	        return new ResponseEntity<CommentVO>(commentVO, HttpStatus.OK);
	    }   
	

}
