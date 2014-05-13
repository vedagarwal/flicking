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

import com.keymetic.flicking.core.entity.Movie;
import com.keymetic.flicking.core.events.MovieDetailsEvent;
import com.keymetic.flicking.core.events.MovieListEvent;
import com.keymetic.flicking.core.exception.MovieNotFoundException;
import com.keymetic.flicking.core.service.MovieService;
import com.keymetic.flicking.core.vo.MovieVO;

@Controller
@RequestMapping("/api/movies")
public class MovieQueriesController{

	@Autowired
	private MovieService movieService;
	
	@RequestMapping(method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MovieVO>> getMovies(@RequestParam(defaultValue = "5") int limit,@RequestParam(defaultValue = "0") int offset) {
		
		MovieListEvent movieListEvent = movieService.getMovies(limit, offset);
		
		if (!movieListEvent.isEntityFound()) {
			return new  ResponseEntity<List<MovieVO>>(HttpStatus.NOT_FOUND);
		}
		
		List<MovieVO> moviesResult = null;
		List<Movie> movies = movieListEvent.getMovies();
		
		if(movies != null){
			moviesResult = MovieVO.toMovieVOList(movies);
		}		
		
		return new ResponseEntity<List<MovieVO>>(moviesResult, HttpStatus.OK);
	}


	@RequestMapping(method = RequestMethod.GET, value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MovieVO> viewMovie(@PathVariable Long id) {

		MovieDetailsEvent details = movieService.getMovieById(id);

		if (!details.isEntityFound()) {
			throw new MovieNotFoundException(id);
		}

		Movie movie = details.getMovie();
		MovieVO movieVO = null;
		if(movie != null){
			movieVO = new MovieVO(movie);
		}

		return new ResponseEntity<MovieVO>(movieVO, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/latest",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MovieVO>> getLatestMovies(@RequestParam(defaultValue = "5") int limit,@RequestParam(defaultValue = "0") int offset) {
		
		MovieListEvent movieListEvent = movieService.getLatestMovies(limit, offset);
		
		if (!movieListEvent.isEntityFound()) {
			return new  ResponseEntity<List<MovieVO>>(HttpStatus.NOT_FOUND);
		}
		
		List<MovieVO> moviesResult = null;
		List<Movie> movies = movieListEvent.getMovies();
		
		if(movies != null){
			moviesResult = MovieVO.toMovieVOList(movies);
		}		
		
		return new ResponseEntity<List<MovieVO>>(moviesResult, HttpStatus.OK);
	}
	
	@RequestMapping(value="/upcoming",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MovieVO>> getUpcomingMovies(@RequestParam(defaultValue = "5") int limit,@RequestParam(defaultValue = "0") int offset) {
		
		MovieListEvent movieListEvent = movieService.getUpcomingMovies(limit, offset);
		
		if (!movieListEvent.isEntityFound()) {
			return new  ResponseEntity<List<MovieVO>>(HttpStatus.NOT_FOUND);
		}
		
		List<MovieVO> moviesResult = null;
		List<Movie> movies = movieListEvent.getMovies();
		
		if(movies != null){
			moviesResult = MovieVO.toMovieVOList(movies);
		}		
		
		return new ResponseEntity<List<MovieVO>>(moviesResult, HttpStatus.OK);
	}
	

}
