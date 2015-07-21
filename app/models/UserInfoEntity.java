package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "user_infos")
public class UserInfoEntity extends GenericModel {
	@Id
	@Column(name = "user_id")
	private int userId;

	@Column(name = "position")
	private int position = -1;

	@Column(name = "height")
	private int height;

	@Column(name = "weight")
	private int weight;

	@Column(name = "mainFoot")
	private int mainFoot = -1;

	@Column(name = "location1")
	private String location1;

	@Column(name = "location2")
	private String location2;

	@Column(name = "occupation")
	private int occupation = -1;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getMainFoot() {
		return mainFoot;
	}

	public void setMainFoot(int mainFoot) {
		this.mainFoot = mainFoot;
	}

	public String getLocation1() {
		return location1;
	}

	public void setLocation1(String location1) {
		this.location1 = location1;
	}

	public String getLocation2() {
		return location2;
	}

	public void setLocation2(String location2) {
		this.location2 = location2;
	}

	public int getOccupation() {
		return occupation;
	}

	public void setOccupation(int occupation) {
		this.occupation = occupation;
	}
}
