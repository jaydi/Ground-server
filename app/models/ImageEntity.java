package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.GenericModel;

@Entity
@Table(name = "images")
public class ImageEntity extends GenericModel {
	@Id
	private String path;

	@Required
	private Blob data;

	@Column(name = "th_data")
	private Blob thData;

	@Required
	@Column(name = "file_name")
	private String fileName;

	@Required
	@Column(name = "created_time")
	private Date createdTime;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Blob getData() {
		return data;
	}

	public void setData(Blob data) {
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Blob getThData() {
		return thData;
	}

	public void setThData(Blob thData) {
		this.thData = thData;
	}

	@PrePersist
	protected void onCreate() {
		createdTime = new Date();
	}
}
