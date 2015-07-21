package protocols;

public class TeamHint {
	private int id;
	private String name;
	private String imageUrl;
	private boolean isManaged; // TOOD 이름 수정 

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

	public boolean isManaged() {
		return isManaged;
	}

	public void setManaged(boolean isManaged) {
		this.isManaged = isManaged;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("id=").append(id).append(",imageUrl=").append(imageUrl).toString();
	}
}
