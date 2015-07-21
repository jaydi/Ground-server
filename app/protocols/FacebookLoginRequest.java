package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class FacebookLoginRequest {
	private String email;
	private String name;
	private String imageUrl;
	private String pushToken;
	private Integer os;
	private String appVer;
	private String deviceUuid;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public Integer getOs() {
		return os;
	}

	public void setOs(Integer os) {
		this.os = os;
	}

	public String getAppVer() {
		return appVer;
	}

	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}

	public String getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("email", email).append("name", name).append("imageUrl", imageUrl).append("pushToken", pushToken).append("os", os)
				.append("appVer", appVer).append("deviceUuid", deviceUuid).toString();
	}
}
