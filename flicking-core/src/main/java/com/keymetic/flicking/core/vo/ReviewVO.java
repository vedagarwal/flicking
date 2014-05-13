package com.keymetic.flicking.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.keymetic.flicking.core.entity.Review;

@XmlRootElement
public class ReviewVO implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	
	@NotEmpty(message="Reviewer Should Not Be Empty Or Null.")
	private String reviewer;
	
	private String review;
	
	@NotNull(message="Rating Should Not Be Empty Or Null.")
	@Range(min=0,max=5,message="Rating Should Be Between 1 To 5")
	private Double rating;
	
	private Date createdDate;
	
	
	public ReviewVO() {
	}
	
	public ReviewVO(Review review) {
		this.id = review.getId();
		this.reviewer = review.getReviewer();
		this.review = review.getReview();
		this.rating = review.getRating();
		this.createdDate = review.getCreatedDate();
	}	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Review toReview(){
		Review review = new Review();
		review.setId(this.id);
		review.setReviewer(this.reviewer);
		review.setReview(this.review);
		review.setRating(this.rating);
		review.setCreatedDate(this.createdDate);
		return review;
	}
	
	public static List<ReviewVO> toReviewVOList(List<Review> reviews){
		List<ReviewVO> reviewVOList = new ArrayList<ReviewVO>();
		
		for(Review r:reviews){
			ReviewVO rVO = new ReviewVO(r);
			reviewVOList.add(rVO);
		}
		
		return reviewVOList;
	}
	
	

}
