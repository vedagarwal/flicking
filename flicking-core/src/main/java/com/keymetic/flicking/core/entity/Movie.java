package com.keymetic.flicking.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;

@Entity
public class Movie implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String movieName;
	private String director;
	private String writer;
	private String cast;
	private String imageURL;
	private String description;
	private int duration;
	private Date releaseDate;
	
	
	private boolean published;
	private boolean released;
	
	@Formula("(select avg(c.score) from Comment c where c.movie_id = id)")
	private Double averageUserRating;
	
	@Formula("(select avg(r.rating) from Review r where r.movie_id = id)")
	private Double averageReviewerRating;
	
	private Date createdDate;
	
	@OneToMany(mappedBy = "movie",cascade = CascadeType.ALL)
	private List<Comment> comments =  new ArrayList<Comment>();
	
	@OneToMany(mappedBy = "movie",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval=true)
	@Fetch (FetchMode.SELECT)
	private List<Review> reviews =  new ArrayList<Review>();
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}	
	
	public Double getAverageUserRating() {
		return averageUserRating;
	}

	public void setAverageUserRating(Double averageUserRating) {
		this.averageUserRating = averageUserRating;
	}

	public Double getAverageReviewerRating() {
		return averageReviewerRating;
	}

	public void setAverageReviewerRating(Double averageReviewerRating) {
		this.averageReviewerRating = averageReviewerRating;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public boolean isReleased() {
		return released;
	}

	public void setReleased(boolean released) {
		this.released = released;
	}

	
	
	

	

}
