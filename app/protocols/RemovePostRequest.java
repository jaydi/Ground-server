package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class RemovePostRequest {
	private int teamId;
	private long postId;

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

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("postId", postId).toString();
	}
}
