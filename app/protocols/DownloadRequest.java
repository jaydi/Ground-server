package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class DownloadRequest {
	private String path;
	private boolean thumbnail;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(boolean thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("path", path).append("thumbnail", thumbnail).toString();
	}
}
