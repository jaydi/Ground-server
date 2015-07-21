package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class WriteCommentRequest {
	private int teamId;
	private long postId;
	private String message;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("postId", postId).toString();
	}
}
