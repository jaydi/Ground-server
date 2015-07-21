package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class RemoveCommentRequest {
	private long postId;
	private int commentId;

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("postId", postId).append("commentId", commentId).toString();
	}
}
