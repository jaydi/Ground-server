package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class RemoveFeedRequest {
	private long feedId;

	public long getFeedId() {
		return feedId;
	}

	public void setFeedId(long feedId) {
		this.feedId = feedId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("feedId", feedId).toString();
	}
}
