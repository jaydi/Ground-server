package protocols;

public class RegisterResponse extends DefaultResponse {
	private String sessionKey;
	private int userId;

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("sessionKey=").append(sessionKey).append("userId=").append(userId).toString();
	}
}
