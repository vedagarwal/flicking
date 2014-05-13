package com.keymetic.flicking.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Range;

import com.keymetic.flicking.core.entity.Comment;

@XmlRootElement
public class CommentVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;	
	private String message;	
	private Date createdDate;
	private String user;
	
	@NotNull(message="Rating/Score Should Not Be Empty Or Null")
	@Range(min=0,max=5,message="Rating/Score Should Be Between 1 To 5")
	private Double score;



	public CommentVO() {
	}

	public CommentVO(Comment comment) {
		this.id = comment.getId();
		this.message = comment.getMessage();
		this.createdDate = comment.getCreatedDate();		
		this.score = comment.getScore();
		this.user = comment.getUser().getFirstName();
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}	
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	
	

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Comment toComment(){
		Comment comment = new Comment();
		comment.setId(this.getId());
		comment.setMessage(this.getMessage());
		comment.setCreatedDate(this.getCreatedDate());		
		comment.setScore(this.getScore());
		return comment;
	}
	
	public static List<CommentVO> toCommentVOList(List<Comment> comments){
		List<CommentVO> commentVOList = new ArrayList<CommentVO>();
		
		for(Comment c:comments){
			CommentVO cVO = new CommentVO(c);
			commentVOList.add(cVO);
		}
		
		return commentVOList;
	}



}
