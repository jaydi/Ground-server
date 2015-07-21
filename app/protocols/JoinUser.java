package protocols;

import models.UserEntity;

public class JoinUser {
	private int id;
	private String name;
	private String imageUrl;
	private int join;

	public JoinUser(UserEntity user, Boolean join) {
		id = user.getId();
		name = user.getName();
		imageUrl = user.getProfileImageUrl();
		if (join == null)
			this.join = 0;
		else
			this.join = join ? 2 : 1;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getJoin() {
		return join;
	}

	public void setJoin(int join) {
		this.join = join;
	}
}
