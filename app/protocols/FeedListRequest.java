package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class FeedListRequest {
	private long cur;

	public long getCur() {
		return cur;
	}

	public void setCur(long cur) {
		this.cur = cur;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("cur", cur).toString();
	}
}
