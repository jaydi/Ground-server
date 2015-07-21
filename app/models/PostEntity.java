package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Query;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import protocols.Comment;

@Entity
@Table(name = "posts")
public class PostEntity extends GenericModel {
	public static enum PostType {
		POST(0),
		FEED(1);
		private final int value;

		private PostType(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	};

	@Id
	@GeneratedValue
	private long id;

	@Required
	@Column(name = "team_id")
	private int teamId;

	@Required
	private int type;

	@Required
	@Column(name = "user_id")
	private int userId;

	private String message;

	private String extra;

	@Column(name = "comment_count")
	private int commentCount;

	@Required
	@Column(name = "created_at")
	private Date createdAt;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}

	public List<Comment> getCommentList() {
		List<Comment> commentList = new ArrayList<Comment>();

		Query query = JPA.em().createQuery("SELECT c, u.name, u.profileImageUrl from CommentEntity c, UserEntity u where c.postId = ? and c.userId = u.id order by c.id desc");
		query.setParameter(1, id);

		List<Object[]> queryResult = query.getResultList();
		for (Object[] obj : queryResult) {
			CommentEntity entity = (CommentEntity) obj[0];
			commentList.add(entity.toComment((String) obj[1], (String) obj[2]));
		}

		return commentList;
	}

	public void addComment() {
		Query query = JPA.em().createQuery("UPDATE PostEntity SET commentCount = commentCount + 1 where id = ?");
		query.setParameter(1, id);
		query.executeUpdate();
	}

	public void removeComment() {
		Query query = JPA.em().createQuery("UPDATE PostEntity SET commentCount = commentCount - 1 where id = ?");
		query.setParameter(1, id);
		query.executeUpdate();
	}
}
