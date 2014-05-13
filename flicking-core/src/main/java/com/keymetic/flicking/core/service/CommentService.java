package com.keymetic.flicking.core.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keymetic.flicking.core.dao.CommentDao;
import com.keymetic.flicking.core.dao.UserDao;
import com.keymetic.flicking.core.entity.Comment;
import com.keymetic.flicking.core.entity.User;
import com.keymetic.flicking.core.events.CommentCreatedEvent;
import com.keymetic.flicking.core.events.CommentDeletedEvent;
import com.keymetic.flicking.core.events.CommentDetailsEvent;
import com.keymetic.flicking.core.events.CommentListEvent;

@Service
public class CommentService {

	@Autowired
	private CommentDao commentDao;
	
	@Autowired
	private UserDao userDao;

	public CommentCreatedEvent addComment(Long movieID,String email,Comment comment){
		
		User user = userDao.getUserByEmail(email);
		if(user!=null){
			comment.setUser(user);
		}
		
		comment = commentDao.addComment(movieID,comment);
		return new CommentCreatedEvent(comment);
	}

	public CommentDetailsEvent getCommentById(Long id){

		Comment comment = commentDao.getCommentById(id);

		if(comment == null){
			return CommentDetailsEvent.notFound();
		}

		return new CommentDetailsEvent(comment);
	}

	public CommentDeletedEvent deleteComment(Long movieID,Long id){
		Comment comment = commentDao.getCommentById(id);

		if(comment == null){
			return CommentDeletedEvent.notFound();
		}

		//copying bean before deleting
		Comment comment2 = new Comment();
		BeanUtils.copyProperties(comment, comment2);

		commentDao.deleteComment(movieID,id);

		return new CommentDeletedEvent(comment2);
	}
	
	public CommentListEvent getLatestComments(Long movieID,int limit, int offset){
		List<Comment> comments = commentDao.getLatestComments(movieID,limit, offset);		
		
		if(comments == null || comments.size() == 0 ){
			return CommentListEvent.notFound();
		}
		
		return new CommentListEvent(comments);
	}
	
	public CommentDetailsEvent getCommentByUserMovie(Long movieID,Long userID){

		Comment comment = commentDao.getCommentByUserMovie(movieID, userID);

		if(comment == null){
			return CommentDetailsEvent.notFound();
		}

		return new CommentDetailsEvent(comment);
	}
	
	public boolean hasAlreadyCommented(Long movieID,String email){
		User user = userDao.getUserByEmail(email);
		
		if(null != commentDao.getCommentByUserMovie(movieID, user.getId())){
			return true;
		}else{
			return false;
		}
		
	}
}
