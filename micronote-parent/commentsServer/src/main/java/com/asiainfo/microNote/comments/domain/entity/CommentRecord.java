package com.asiainfo.microNote.comments.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 评论条目
 * @author yi
 */
@Entity
@Table(name="comment_record")
public class CommentRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "comment_record_id")
	long id;
	
	@Column(name = "user_id",length=40)
	String userId;
	
	/**
	 * 自動加載會導致無法刪除
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="comment_id")
	@JsonIgnoreProperties("comment")
	private Comment comment;
	
	String content;
	
	@Column(name = "create_date")
	long createDate = System.currentTimeMillis();
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public Comment getComment() {
		return comment;
	}
	
	
	
}
