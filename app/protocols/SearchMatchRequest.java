package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SearchMatchRequest {
	private long startTime;
	private long endTime;
	private double latitude;
	private double longitude;
	private int distance;

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

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("startTime", startTime).append("endTime", endTime).append("latitude", latitude).append("longitude", longitude)
				.append("distance", distance).toString();
	}
}
