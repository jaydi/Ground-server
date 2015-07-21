package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.cache.Cache;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.CacheKey;

@Entity
@Table(name = "devices")
public class DeviceEntity extends GenericModel {
	public static enum OS {
		ANDROID(0),
		IOS(1);

		private final int value;

		private OS(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	}

	@Id
	@GeneratedValue
	private int id;

	@Required
	@Column(name = "user_id")
	private int userId;

	@Column(name = "device_uuid")
	private String deviceUuid;

	private int os;

	@Column(name = "push_token", length = 1024)
	private String pushToken;

	@Column(name = "app_ver", length = 16)
	private String appVer;

	@Column(name = "push_alert")
	private boolean pushAlert = true;

	private boolean active = true;

	@Column(name = "session_key")
	private String sessionKey;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	public int getOs() {
		return os;
	}

	public void setOs(int os) {
		this.os = os;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public String getAppVer() {
		return appVer;
	}

	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}

	public boolean isPushAlert() {
		return pushAlert;
	}

	public void setPushAlert(boolean pushAlert) {
		this.pushAlert = pushAlert;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public static List<DeviceEntity> findByUserIds(List<Integer> ids) {
		List<DeviceEntity> result = new ArrayList<DeviceEntity>();
		if (ids == null || ids.isEmpty())
			return result;
		boolean first = true;
		StringBuilder sb = new StringBuilder("userId in (");
		for (int id : ids) {
			if (first)
				sb.append(id);
			else
				sb.append(",").append(id);
			first = false;
		}
		sb.append(") and active = 1");
		return find(sb.toString()).fetch();
	}

	public static DeviceEntity findBySessionKey(String sessionKey) {
		String cacheKey = CacheKey.deviceBySessionKey + sessionKey;
		DeviceEntity device = Cache.get(cacheKey, DeviceEntity.class);
		if (device == null) {
			device = DeviceEntity.find("sessionKey = ?", sessionKey).first();
			Cache.set(cacheKey, device, "8h");
		}
		return device;
	}

	@Override
	public DeviceEntity save() {
		super.save();
		String cacheKey = CacheKey.deviceBySessionKey + sessionKey;
		Cache.delete(cacheKey);
		return this;
	}

	@Override
	public DeviceEntity delete() {
		super.delete();
		String cacheKey = CacheKey.deviceBySessionKey + sessionKey;
		Cache.delete(cacheKey);
		return this;
	}
}
