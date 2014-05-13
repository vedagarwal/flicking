package com.keymetic.flicking.core.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keymetic.flicking.core.dao.MovieDao;
import com.keymetic.flicking.core.entity.Movie;
import com.keymetic.flicking.core.entity.Review;
import com.keymetic.flicking.core.events.MovieCreatedEvent;
import com.keymetic.flicking.core.events.MovieDeletedEvent;
import com.keymetic.flicking.core.events.MovieDetailsEvent;
import com.keymetic.flicking.core.events.MovieListEvent;
import com.keymetic.flicking.core.events.MovieUpdatedEvent;
import com.keymetic.flicking.core.events.ReviewUpdatedEvent;

@Service
public class MovieService {
	
	@Autowired
	private MovieDao movieDao;

	public MovieCreatedEvent addMovie(Movie movie){
		movieDao.addMovie(movie);
		return new MovieCreatedEvent(movie);
	}
	
	public MovieUpdatedEvent updateMovie(Movie movie){
		Movie uReview = movieDao.getMovieById(movie.getId());

		if(uReview == null){
			return MovieUpdatedEvent.notFound();
		}
		
		movie = movieDao.updateMovie(movie);
		return new MovieUpdatedEvent(movie);
		
	}
	
	public MovieDetailsEvent getMovieById(Long id){
		
		Movie movie = movieDao.getMovieById(id);
		
		if(movie == null){
			return MovieDetailsEvent.notFound();
		}
		
		return new MovieDetailsEvent(movie);
	}
	
	public MovieDeletedEvent deleteMovie(Long id){
		Movie movie = movieDao.getMovieById(id);
		
		if(movie == null){
			return MovieDeletedEvent.notFound();
		}
		
		//copying bean before deleting
		Movie movie2 = new Movie();
		BeanUtils.copyProperties(movie, movie2);
		
		movieDao.deleteMovie(id);
		
		return new MovieDeletedEvent(movie2);
	}
	
	public MovieListEvent getMovies(int limit, int offset){
		List<Movie> movies = movieDao.getMovies(limit, offset);		
		
		if(movies == null || movies.size() == 0 ){
			return MovieListEvent.notFound();
		}
		
		return new MovieListEvent(movies);
	}
	
	public MovieListEvent getLatestMovies(int limit, int offset){
		List<Movie> movies = movieDao.getLatestMovies(limit, offset);		
		
		if(movies == null || movies.size() == 0 ){
			return MovieListEvent.notFound();
		}
		
		return new MovieListEvent(movies);
	}
	
	public MovieListEvent getUpcomingMovies(int limit, int offset){
		List<Movie> movies = movieDao.getUpcomingMovies(limit, offset);		
		
		if(movies == null || movies.size() == 0 ){
			return MovieListEvent.notFound();
		}
		
		return new MovieListEvent(movies);
	}
	
	
	public MovieDetailsEvent addImageToMovie(Long id,String imageURL){
		
		Movie movie = movieDao.addImageToMovie(id, imageURL);
		
		if(movie == null){
			return MovieDetailsEvent.notFound();
		}		
		
		return new MovieDetailsEvent(movie);
		
	}

}
