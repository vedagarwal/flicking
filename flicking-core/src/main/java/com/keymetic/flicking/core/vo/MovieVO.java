package com.keymetic.flicking.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

import com.keymetic.flicking.core.entity.Movie;
import com.keymetic.flicking.core.entity.Review;

@XmlRootElement
public class MovieVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	@NotEmpty(message="Movie Name Should Not Be Empty Or Null.")	
	@Size(min=5,max=30,message="Movie Name Should Be Between 5 To 30 chars.")
	private String movieName;

	@NotEmpty(message="Director Name Should Not Be Empty Or Null.")
	private String director;

	@NotEmpty(message="Writer Name Should Not Be Empty Or Null.")
	private String writer;

	@NotEmpty(message="Cast Should Not Be Empty Or Null.")
	private String cast;

	private int duration;

	@NotNull(message="Release Date Should Not Be Null")
	private Date releaseDate;
	private String imageURL;

	@NotEmpty(message="Description Should Not Be Empty Or Null.")
	private String description;

	private Double averageUserRating = 0.0;
	private Double averageReviewerRating = 0.0;	
	
	private Date createdDate;
	private List<ReviewVO> reviews = new ArrayList<ReviewVO>();
	
	private boolean released;
	private boolean published;



	public MovieVO() {		
	}



	public MovieVO(Movie movie) {
		this.id = movie.getId();
		this.movieName = movie.getMovieName();
		this.director = movie.getDirector();
		this.writer = movie.getWriter();
		this.cast = movie.getCast();
		this.duration = movie.getDuration();
		this.description = movie.getDescription();
		this.imageURL = movie.getImageURL();
		this.releaseDate = movie.getReleaseDate();
		this.released = movie.isReleased();
		this.published = movie.isPublished();
		
		if(movie.getAverageReviewerRating() != null && movie.getAverageReviewerRating() > 0){
			this.averageReviewerRating = movie.getAverageReviewerRating();
		}
		if(movie.getAverageUserRating() != null && movie.getAverageUserRating() > 0){
			this.averageUserRating = movie.getAverageUserRating();
		}
		this.createdDate = movie.getCreatedDate();
		if(movie.getReviews() != null || movie.getReviews().size() != 0){
			this.reviews = ReviewVO.toReviewVOList(movie.getReviews());
		}
	}

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



	public List<ReviewVO> getReviews() {
		return reviews;
	}
	public void setReviews(List<ReviewVO> reviews) {
		this.reviews = reviews;
	}



	public Date getCreatedDate() {
		return createdDate;
	}



	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	



	public boolean isReleased() {
		return released;
	}



	public void setReleased(boolean released) {
		this.released = released;
	}



	public boolean isPublished() {
		return published;
	}



	public void setPublished(boolean published) {
		this.published = published;
	}



	public Movie toMovie(){
		Movie movie = new Movie();
		movie.setId(this.getId());
		movie.setMovieName(this.getMovieName());
		movie.setDirector(this.getDirector());
		movie.setWriter(this.getWriter());
		movie.setCast(this.getCast());
		movie.setDuration(this.getDuration());
		movie.setDescription(this.getDescription());
		movie.setImageURL(this.getImageURL());
		movie.setReleaseDate(this.getReleaseDate());
		movie.setAverageReviewerRating(this.getAverageReviewerRating());
		movie.setAverageUserRating(this.getAverageUserRating());
		movie.setCreatedDate(this.getCreatedDate());
		movie.setPublished(this.isPublished());
		movie.setReleased(this.isReleased());

		List<Review> reviews = new ArrayList<Review>();
		for(ReviewVO r : this.getReviews()){
			Review rev = r.toReview();
			rev.setMovie(movie);
			reviews.add(rev);
		}

		movie.setReviews(reviews);

		return movie;
	}

	public static List<MovieVO> toMovieVOList(List<Movie> movies){
		List<MovieVO> movieVOList = new ArrayList<MovieVO>();

		for(Movie m:movies){
			MovieVO mVO = new MovieVO(m);
			movieVOList.add(mVO);
		}

		return movieVOList;
	}




}
