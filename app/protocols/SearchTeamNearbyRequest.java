package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SearchTeamNearbyRequest {
	private double latitude;
	private double longitude;
	private int distance;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("latitude", latitude).append("longitude", longitude).append("distance", distance).toString();
	}
}
