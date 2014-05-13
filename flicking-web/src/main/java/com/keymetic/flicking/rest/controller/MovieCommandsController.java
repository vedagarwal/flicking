package com.keymetic.flicking.rest.controller;

import java.io.IOException;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.keymetic.flicking.core.entity.Movie;
import com.keymetic.flicking.core.entity.Review;
import com.keymetic.flicking.core.events.MovieCreatedEvent;
import com.keymetic.flicking.core.events.MovieDeletedEvent;
import com.keymetic.flicking.core.events.MovieDetailsEvent;
import com.keymetic.flicking.core.events.MovieUpdatedEvent;
import com.keymetic.flicking.core.events.ReviewUpdatedEvent;
import com.keymetic.flicking.core.exception.ImageUploadException;
import com.keymetic.flicking.core.exception.MovieNotFoundException;
import com.keymetic.flicking.core.service.MovieService;
import com.keymetic.flicking.core.vo.MovieVO;
import com.keymetic.flicking.core.vo.ReviewVO;
import com.keymetic.flicking.rest.service.UploadService;

@Controller
@RequestMapping("/api/movies")
public class MovieCommandsController {

	@Autowired
	private MovieService movieService;

	@Autowired
	private UploadService uploadService;

	@RequestMapping(method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MovieVO> createMovie(@RequestBody @Valid MovieVO movieVO, UriComponentsBuilder builder) {

		/*String email = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("The LOgged on user email is"+email);*/

		Movie movie = movieVO.toMovie();
		MovieCreatedEvent movieCreatedEvent = movieService.addMovie(movie);

		MovieVO createdMovieVO = null;
		if(movieCreatedEvent.getMovie() != null){
			createdMovieVO = new MovieVO(movieCreatedEvent.getMovie());
		}

		return new ResponseEntity<MovieVO>(createdMovieVO,null, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/{id}",method = RequestMethod.PUT,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MovieVO> updateMovie(@PathVariable Long id,@RequestBody @Valid MovieVO movieVO) {

		 	Movie movie = movieVO.toMovie();
	    	MovieUpdatedEvent movieUpdatedEvent = movieService.updateMovie(movie);
	    	
	    	MovieVO updatedMovieVO = null;
	    	if(movieUpdatedEvent.getMovie() != null){
	    		updatedMovieVO = new MovieVO(movieUpdatedEvent.getMovie());
	    	}
	        return new ResponseEntity<MovieVO>(updatedMovieVO,null, HttpStatus.CREATED);
	}


	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MovieVO> deleteMovie(@PathVariable Long id) {

		MovieDeletedEvent movieDeletedEvent = movieService.deleteMovie(id);

		if (!movieDeletedEvent.isEntityFound()) {
			throw new MovieNotFoundException(id);
		}

		Movie movie = movieDeletedEvent.getMovie();
		MovieVO movieVO = null;
		if(movie != null){
			movieVO = new MovieVO(movie);
		}

		if (movieDeletedEvent.isDeletionCompleted()) {
			return new ResponseEntity<MovieVO>(movieVO, HttpStatus.OK);
		}

		return new ResponseEntity<MovieVO>(movieVO, HttpStatus.FORBIDDEN);
	}

	@RequestMapping(value="/{id}/image", method=RequestMethod.POST)
	public ResponseEntity<String> uploadImage(@PathVariable Long id,MultipartFile file) {
		MovieDetailsEvent details = movieService.getMovieById(id);
		String imageName = "";
		if(file != null){			
			try {
				//first delete file on cdn before uploading new
				if(details.getMovie()!=null && details.getMovie().getImageURL()!=null){
					uploadService.deleteFile(details.getMovie().getImageURL());
				}

				imageName = uploadService.uploadFile(file);
				details = movieService.addImageToMovie(id, imageName);

				if (!details.isEntityFound()) {
					throw new MovieNotFoundException(id);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				throw new ImageUploadException(id);
			}			

		}
		return new ResponseEntity<String>(details.getMovie().getImageURL(),null,HttpStatus.CREATED);

	}


}
