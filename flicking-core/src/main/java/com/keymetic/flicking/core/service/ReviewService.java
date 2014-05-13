package com.keymetic.flicking.core.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keymetic.flicking.core.dao.ReviewDao;
import com.keymetic.flicking.core.entity.Review;
import com.keymetic.flicking.core.events.ReviewCreatedEvent;
import com.keymetic.flicking.core.events.ReviewDeletedEvent;
import com.keymetic.flicking.core.events.ReviewDetailsEvent;
import com.keymetic.flicking.core.events.ReviewListEvent;
import com.keymetic.flicking.core.events.ReviewUpdatedEvent;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewDao reviewDao;

	public ReviewCreatedEvent addReview(Long movieID,Review review){
		review = reviewDao.addReview(movieID,review);
		return new ReviewCreatedEvent(review);
	}
	
	public ReviewUpdatedEvent updateReview(Review review){
		Review uReview = reviewDao.getReviewById(review.getId());

		if(uReview == null){
			return ReviewUpdatedEvent.notFound();
		}
		
		review = reviewDao.updateReview(review);
		return new ReviewUpdatedEvent(review);
		
	}

	public ReviewDetailsEvent getReviewById(Long id){

		Review review = reviewDao.getReviewById(id);

		if(review == null){
			return ReviewDetailsEvent.notFound();
		}

		return new ReviewDetailsEvent(review);
	}

	public ReviewDeletedEvent deleteReview(Long movieID,Long id){
		Review review = reviewDao.getReviewById(id);

		if(review == null){
			return ReviewDeletedEvent.notFound();
		}

		//copying bean before deleting
		Review review2 = new Review();
		BeanUtils.copyProperties(review, review2);

		reviewDao.deleteReview(movieID,id);

		return new ReviewDeletedEvent(review2);
	}
	
	public ReviewListEvent getLatestReviews(Long movieID,int limit, int offset){
		List<Review> reviews = reviewDao.getLatestReviews(movieID,limit,offset);		
		
		if(reviews == null || reviews.size() == 0 ){
			return ReviewListEvent.notFound();
		}
		
		return new ReviewListEvent(reviews);
	}

}
