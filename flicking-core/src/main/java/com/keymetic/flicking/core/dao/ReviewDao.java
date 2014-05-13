package com.keymetic.flicking.core.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.keymetic.flicking.core.entity.Movie;
import com.keymetic.flicking.core.entity.Review;

@Repository
public class ReviewDao {

	@Autowired
	private SessionFactory sessionFactory;	
	
	@Transactional
	public Review addReview(Long movieID,Review review) throws DataAccessException{			
		Movie movie = (Movie)sessionFactory.getCurrentSession().get(Movie.class, movieID);
		review.setMovie(movie);
		//setting current date
		review.setCreatedDate(new Date());
		sessionFactory.getCurrentSession().save(review);
		return review;
	}	
	
	@Transactional
	public Review updateReview(Review review) throws DataAccessException{
		Review uReview = (Review)sessionFactory.getCurrentSession().get(Review.class, review.getId());
		uReview.setRating(review.getRating());
		uReview.setReview(review.getReview());
		uReview.setReviewer(review.getReviewer());
		sessionFactory.getCurrentSession().update(uReview);
		return uReview;
	}	
	
	
	@Transactional
	public Review getReviewById(Long id) throws DataAccessException{		
		return (Review)sessionFactory.getCurrentSession().get(Review.class, id);
	}
	
	@Transactional
	public Boolean deleteReview(Long movieID,Long id) throws DataAccessException{
		Review review = (Review)sessionFactory.getCurrentSession().get(Review.class,id);
		Movie movie =(Movie)sessionFactory.getCurrentSession().get(Movie.class, movieID);
		if(review != null){
			movie.getReviews().remove(review);
			//sessionFactory.getCurrentSession().delete(review);
			sessionFactory.getCurrentSession().save(movie);
			return true;
		}
		else{
			return false;
		}
	}
	
	@Transactional
	public List<Review> getLatestReviews(Long movieID,int limit,int offset) throws DataAccessException{
		List<Review> reviews = null;		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Review.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);
		criteria.addOrder(Order.desc("createdDate"));
		criteria.add(Restrictions.eq("movie.id", movieID));
		reviews = criteria.list();
		return reviews;
	}
	
	
}
