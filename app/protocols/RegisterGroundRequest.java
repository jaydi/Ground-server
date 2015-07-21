package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class RegisterGroundRequest {
	private String name;
	private String address;
	private double latitude;
	private double longitude;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

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

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append("address", address).append("latitude", latitude).append("longitude", longitude).toString();
	}
}
