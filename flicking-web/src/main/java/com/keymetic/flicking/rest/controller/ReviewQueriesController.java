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

import com.keymetic.flicking.core.entity.Review;
import com.keymetic.flicking.core.events.ReviewDetailsEvent;
import com.keymetic.flicking.core.events.ReviewListEvent;
import com.keymetic.flicking.core.exception.ReviewNotFoundException;
import com.keymetic.flicking.core.service.ReviewService;
import com.keymetic.flicking.core.vo.ReviewVO;

@Controller
@RequestMapping("/api/movies/{id}/reviews")
public class ReviewQueriesController {
	
	@Autowired
	private ReviewService reviewService;
	
	@RequestMapping(method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ReviewVO>> getLatestReviews(@PathVariable Long id,@RequestParam(defaultValue = "10") int limit,@RequestParam(defaultValue = "0") int offset) {
		
		ReviewListEvent reviewListEvent = reviewService.getLatestReviews(id, limit, offset);
		
		if (!reviewListEvent.isEntityFound()) {
			return new  ResponseEntity<List<ReviewVO>>(HttpStatus.NOT_FOUND);
		}
		
		List<ReviewVO> reviewsResult = null;
		List<Review> reviews = reviewListEvent.getReviews();
		
		if(reviews != null){
			reviewsResult = ReviewVO.toReviewVOList(reviews);
		}		
		
		return new ResponseEntity<List<ReviewVO>>(reviewsResult, HttpStatus.OK);
	}
	
	
	 	@RequestMapping(method = RequestMethod.GET, value = "/{rid}",produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<ReviewVO> viewReview(@PathVariable Long rid) {

	        ReviewDetailsEvent details = reviewService.getReviewById(rid);

	        if (!details.isEntityFound()) {
	        	throw new ReviewNotFoundException(rid);
	        }

	        Review review = details.getReview();
	        ReviewVO reviewVO = null;
			if(review != null){
				reviewVO = new ReviewVO(review);
			}			
	        return new ResponseEntity<ReviewVO>(reviewVO, HttpStatus.OK);
	    }   
	


}
