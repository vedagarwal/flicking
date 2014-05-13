package com.keymetic.flicking.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import com.keymetic.flicking.core.entity.Comment;
import com.keymetic.flicking.core.events.CommentCreatedEvent;
import com.keymetic.flicking.core.events.CommentDeletedEvent;
import com.keymetic.flicking.core.exception.AlreadyCommentedException;
import com.keymetic.flicking.core.exception.CommentNotFoundException;
import com.keymetic.flicking.core.service.CommentService;
import com.keymetic.flicking.core.vo.CommentVO;

@Controller
@RequestMapping("/api/movies/{id}/comments")
public class CommentCommandsController {
	
	@Autowired
	private CommentService commentService;	

    @RequestMapping(method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentVO> createComment(@PathVariable Long id,@RequestBody CommentVO commentVO, UriComponentsBuilder builder) {    	
        Comment comment = commentVO.toComment();
        
        //get user
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(commentService.hasAlreadyCommented(id, userDetails.getUsername())){
        	throw new AlreadyCommentedException();
        }
               
    	CommentCreatedEvent commentCreatedEvent = commentService.addComment(id,userDetails.getUsername(),comment);
    	
    	CommentVO createdCommentVO = null;
    	if(commentCreatedEvent.getComment() != null){
    		createdCommentVO = new CommentVO(commentCreatedEvent.getComment());
    	}
        return new ResponseEntity<CommentVO>(createdCommentVO,null, HttpStatus.CREATED);
    }
    
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/{cid}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentVO> deleteComment(@PathVariable Long id,@PathVariable Long cid) {
     
       CommentDeletedEvent commentDeletedEvent = commentService.deleteComment(id,cid);
               
        if (!commentDeletedEvent.isEntityFound()) {
            //return new ResponseEntity<CommentVO>(HttpStatus.NOT_FOUND);
        	throw new CommentNotFoundException(cid);
        }

        Comment comment = commentDeletedEvent.getComment();
        CommentVO commentVO = null;
		if(comment != null){
			commentVO = new CommentVO(comment);
		}

        if (commentDeletedEvent.isDeletionCompleted()) {
            return new ResponseEntity<CommentVO>(commentVO, HttpStatus.OK);
        }

        return new ResponseEntity<CommentVO>(commentVO, HttpStatus.FORBIDDEN);
    }
	

}
