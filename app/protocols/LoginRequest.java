package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class LoginRequest {
	private String email;
	private String password;
	private String pushToken;
	private int os;
	private String appVer;
	private String deviceUuid;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public int getOs() {
		return os;
	}

	public void setOs(int os) {
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
		return new ToStringBuilder(this).append("email", email).append("password", password).append("pushToken", pushToken).append("os", os).append("appVer", appVer)
				.append("deviceUuid", deviceUuid).toString();
	}
}
