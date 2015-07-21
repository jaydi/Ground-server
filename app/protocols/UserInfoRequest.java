package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class UserInfoRequest {
	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("userId", userId).toString();
	}
}
