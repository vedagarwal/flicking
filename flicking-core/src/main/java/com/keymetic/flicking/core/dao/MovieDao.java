package com.keymetic.flicking.core.dao;


import java.util.ArrayList;
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
public class MovieDao {

	@Autowired
	private SessionFactory sessionFactory;


	@Transactional
	public Movie addMovie(Movie movie) throws DataAccessException{
		movie.setCreatedDate(new Date());
		sessionFactory.getCurrentSession().save(movie);
		return movie;
	}

	@Transactional
	public Movie getMovieById(Long id) throws DataAccessException{
		return (Movie)sessionFactory.getCurrentSession().get(Movie.class,id);
	}

	@Transactional
	public Movie addImageToMovie(Long id,String imageURL) throws DataAccessException{
		Movie movie = null;
		movie = (Movie) sessionFactory.getCurrentSession().get(Movie.class,id);
		if(movie != null){
			movie.setImageURL(imageURL);
			sessionFactory.getCurrentSession().update(movie);
		}
		return movie;		
	}

	@Transactional
	public Movie updateMovie(Movie movie) throws DataAccessException{
		Movie uMovie = (Movie)sessionFactory.getCurrentSession().get(Movie.class, movie.getId());	
		uMovie.setMovieName(movie.getMovieName());		
		uMovie.setDirector(movie.getDirector());
		uMovie.setWriter(movie.getWriter());
		uMovie.setCast(movie.getCast());
		uMovie.setDuration(movie.getDuration());
		uMovie.setDescription(movie.getDescription());		
		uMovie.setReleaseDate(movie.getReleaseDate());		
		uMovie.setPublished(movie.isPublished());
		uMovie.setReleased(movie.isReleased());		
		sessionFactory.getCurrentSession().update(uMovie);		
		return uMovie;
	}

	@Transactional
	public Boolean deleteMovie(Long id) throws DataAccessException{
		Movie movie = null;
		movie = (Movie) sessionFactory.getCurrentSession().get(Movie.class,id);
		if(movie != null){
			sessionFactory.getCurrentSession().delete(movie);
			return true;
		}
		else{
			return false;
		}
	}


	@Transactional
	public List<Movie> getMovies(int limit,int offset) throws DataAccessException{
		List<Movie> movies = null;		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Movie.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);
		criteria.addOrder(Order.desc("createdDate"));		
		movies = criteria.list();
		return movies;
	}
	
	
	@Transactional
	public List<Movie> getLatestMovies(int limit,int offset) throws DataAccessException{
		List<Movie> movies = null;		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Movie.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);
		criteria.add(Restrictions.eq("published",true));
		criteria.add(Restrictions.eq("released",true));
		criteria.addOrder(Order.desc("releaseDate"));
		criteria.addOrder(Order.desc("createdDate"));		
		movies = criteria.list();
		return movies;
	}	
	
	@Transactional
	public List<Movie> getUpcomingMovies(int limit,int offset) throws DataAccessException{
		List<Movie> movies = null;		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Movie.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);
		criteria.add(Restrictions.eq("published",true));
		criteria.add(Restrictions.eq("released",false));
		criteria.addOrder(Order.asc("releaseDate"));
		criteria.addOrder(Order.desc("createdDate"));		
		movies = criteria.list();
		return movies;
	}	

	@Transactional
	public List<Movie> getAllMovies() throws DataAccessException{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Movie.class);
		List<Movie> movies = new ArrayList<Movie>();		
		movies = criteria.list();
		return movies;
	}	

	@Transactional
	public List<Movie> getMoviesBySearchString(String searchString,int limit,int offset) throws DataAccessException{
		List<Movie> movies = null;		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Movie.class);
		criteria.add(Restrictions.like("movieName","%"+searchString+"%" ));
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);
		criteria.addOrder(Order.desc("createdDate"));		
		movies = criteria.list();
		return movies;
	}

}
