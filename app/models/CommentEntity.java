package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import protocols.Comment;

@Entity
@Table(name = "comments")
public class CommentEntity extends GenericModel {
	@Id
	@GeneratedValue
	private int id;

	@Required
	@Column(name = "post_id")
	private long postId;

	@Required
	@Column(name = "user_id")
	private int userId;

	@Required
	private String message;

	@Required
	@Column(name = "created_at")
	private Date createdAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}

	public Comment toComment(String userName, String userImageUrl) {
		Comment comment = new Comment();
		comment.setId(id);
		comment.setPostId(postId);
		comment.setUserId(userId);
		comment.setMessage(message);
		comment.setCreatedAt(createdAt.getTime());
		comment.setUserName(userName);
		comment.setUserImageUrl(userImageUrl);
		return comment;
	}
}
