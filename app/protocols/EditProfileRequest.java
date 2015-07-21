package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class EditProfileRequest {
	private String name; // TODO 이름을 바꿀 수 있나?
	private int birthYear; // TODO 생년월일을 바꿀 수 있나?
	private int position;
	private int height;
	private int weight;
	private int mainFoot;
	private String location1;
	private String location2;
	private int occupation;
	private String phoneNumber;
	private String profileImageUrl;

	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append("birthYear", birthYear).append("position", position).append("height", height).append("weight", weight)
				.append("mainFoot", mainFoot).append("location1", location1).append("location2", location2).append("occupation", occupation).append("phoneNumber", phoneNumber)
				.append("profileImageUrl", profileImageUrl).toString();
	}
}
