package protocols;

public class WritePostResponse extends DefaultResponse {
	private long postId;

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}
}
