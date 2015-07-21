package protocols;

import java.util.List;

public class PostListResponse extends DefaultResponse {
	private List<Post> postList;

	public List<Post> getPostList() {
		return postList;
	}

	public void setPostList(List<Post> postList) {
		this.postList = postList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("postList=").append(postList).toString();
	}
}
