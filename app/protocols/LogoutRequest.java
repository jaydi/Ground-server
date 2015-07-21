package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class LogoutRequest {
	private String deviceUuid;

	public String getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("deviceUuid", deviceUuid).toString();
	}
}
