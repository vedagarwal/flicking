package com.keymetic.flicking.rest.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import com.keymetic.flicking.core.entity.Review;
import com.keymetic.flicking.core.events.ReviewCreatedEvent;
import com.keymetic.flicking.core.events.ReviewDeletedEvent;
import com.keymetic.flicking.core.events.ReviewUpdatedEvent;
import com.keymetic.flicking.core.exception.ReviewNotFoundException;
import com.keymetic.flicking.core.service.ReviewService;
import com.keymetic.flicking.core.vo.ReviewVO;

@Controller
@RequestMapping("/api/movies/{id}/reviews")
public class ReviewCommandsController {
	@Autowired
	private ReviewService reviewService;	

    @RequestMapping(method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewVO> createReview(@PathVariable Long id,@RequestBody @Valid ReviewVO reviewVO, UriComponentsBuilder builder) {    	
        Review review = reviewVO.toReview();
    	ReviewCreatedEvent reviewCreatedEvent = reviewService.addReview(id,review);
    	
    	ReviewVO createdReviewVO = null;
    	if(reviewCreatedEvent.getReview() != null){
    		createdReviewVO = new ReviewVO(reviewCreatedEvent.getReview());
    	}
        return new ResponseEntity<ReviewVO>(createdReviewVO,null, HttpStatus.CREATED);
    }
    
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/{rid}",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewVO> deleteReview(@PathVariable Long id,@PathVariable Long rid) {
     
    	ReviewDeletedEvent reviewDeletedEvent = reviewService.deleteReview(id,rid);
        
        if (!reviewDeletedEvent.isEntityFound()) {
           // return new ResponseEntity<ReviewVO>(HttpStatus.NOT_FOUND);
        	throw new ReviewNotFoundException(rid);
        }

        Review review = reviewDeletedEvent.getReview();
        ReviewVO reviewVO = null;
		if(review != null){
			reviewVO = new ReviewVO(review);
		}

        if (reviewDeletedEvent.isDeletionCompleted()) {
            return new ResponseEntity<ReviewVO>(reviewVO, HttpStatus.OK);
        }

        return new ResponseEntity<ReviewVO>(reviewVO, HttpStatus.FORBIDDEN);
    }
    
    @RequestMapping(value="/{rid}",method = RequestMethod.PUT,produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewVO> updateReview(@PathVariable Long id,@RequestBody @Valid ReviewVO reviewVO) {    	
        Review review = reviewVO.toReview();
    	ReviewUpdatedEvent reviewUpdatedEvent = reviewService.updateReview(review);
    	
    	ReviewVO updatedReviewVO = null;
    	if(reviewUpdatedEvent.getReview() != null){
    		updatedReviewVO = new ReviewVO(reviewUpdatedEvent.getReview());
    	}
        return new ResponseEntity<ReviewVO>(updatedReviewVO,null, HttpStatus.CREATED);
    }
	


}
