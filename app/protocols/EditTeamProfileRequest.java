package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class EditTeamProfileRequest {
	private int teamId;
	private String imageUrl;
	private String address;
	private double longitude;
	private double latitude;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("address", address).append("latitude", latitude).append("longitude", longitude)
				.append("imageUrl", imageUrl).toString();
	}
}
