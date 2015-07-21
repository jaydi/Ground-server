package protocols;

public class LoginResponse extends DefaultResponse {
	private String sessionKey;
	private int userId;
	private String name;
	private String imageUrl;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("sessionKey=").append(sessionKey).append("userId=").append(userId).append("name=").append(name).append("imageUrl").append(imageUrl)
				.toString();
	}
}
