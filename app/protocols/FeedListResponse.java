package protocols;

import java.util.List;

public class FeedListResponse extends DefaultResponse {
	private List<Feed> feedList;

	public List<Feed> getFeedList() {
		return feedList;
	}

	public void setFeedList(List<Feed> feedList) {
		this.feedList = feedList;
	}
}
