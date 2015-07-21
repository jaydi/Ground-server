package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import protocols.Ground;

@Entity
@Table(name = "grounds")
public class GroundEntity extends GenericModel {
	@Id
	@GeneratedValue
	private int id;

	@Required
	@Column(length = 64)
	private String name;

	@Required
	@Column(length = 256)
	private String address;

	@Required
	private double latitude;

	@Required
	private double longitude;

	@Required
	@Column(name = "created_at")
	private Date createdAt;

	@Required
	private int freq;

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
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

	public Ground toGround() {
		Ground ground = new Ground();
		ground.setId(id);
		ground.setName(name);
		ground.setAddress(address);
		ground.setLatitude(latitude);
		ground.setLongitude(longitude);
		return ground;
	}
}
