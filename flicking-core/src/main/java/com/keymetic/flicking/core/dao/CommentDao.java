package com.keymetic.flicking.core.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.keymetic.flicking.core.entity.Comment;
import com.keymetic.flicking.core.entity.Movie;
import com.keymetic.flicking.core.entity.Review;

@Repository
public class CommentDao {

	@Autowired
	private SessionFactory sessionFactory;	
	
	@Transactional
	public Comment addComment(Long movieID,Comment comment) throws DataAccessException{			
		Movie movie = (Movie)sessionFactory.getCurrentSession().get(Movie.class, movieID);
		comment.setMovie(movie);
		comment.setCreatedDate(new Date());
		sessionFactory.getCurrentSession().save(comment);
		return comment;
	}	
	
	
	@Transactional
	public Comment getCommentById(Long cid) throws DataAccessException{		
		return (Comment)sessionFactory.getCurrentSession().get(Comment.class, cid);
	}
	
	@Transactional
	public Boolean deleteComment(Long movieID,Long id) throws DataAccessException{
		Comment comment = (Comment)sessionFactory.getCurrentSession().get(Comment.class,id);	
		Movie movie =(Movie)sessionFactory.getCurrentSession().get(Movie.class, movieID);
		if(comment != null){
			movie.getComments().remove(comment);
			sessionFactory.getCurrentSession().save(movie);
			return true;
		}
		else{
			return false;
		}		
	}
	
	@Transactional
	public List<Comment> getLatestComments(Long movieID,int limit,int offset) throws DataAccessException{
		List<Comment> comments = null;		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Comment.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);
		criteria.addOrder(Order.desc("createdDate"));
		criteria.add(Restrictions.eq("movie.id", movieID));
		comments = criteria.list();
		return comments;
	}
	
	@Transactional
	public Comment getCommentByUserMovie(Long movieID,Long userID) throws DataAccessException{		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Comment.class);		
		criteria.add(Restrictions.eq("movie.id", movieID));
		criteria.add(Restrictions.eq("user.id", userID));
		return (Comment)criteria.uniqueResult();		
	}
	
	
}
