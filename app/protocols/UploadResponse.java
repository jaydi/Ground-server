package protocols;

public class UploadResponse {
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("path=").append(path).toString();
	}
}
