package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Query;
import javax.persistence.Table;

import models.TeamMemberEntity.MemberStatus;
import play.cache.Cache;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import protocols.Feed;
import protocols.TeamHint;
import protocols.User;
import protocols.UserInfo;
import utils.CacheKey;
import utils.Constants;

@Entity
@Table(name = "users")
public class UserEntity extends GenericModel {
	@Id
	@GeneratedValue
	private int id;

	@Required
	@Column(length = 40)
	private String email;

	@Required
	@Column(length = 64)
	private String password;

	@Column(length = 40)
	private String name;

	@Column(name = "birth_year")
	private int birthYear;

	@Column(name = "email_valid")
	private Boolean emailValid;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	@Required
	@Column(name = "created_time")
	private Date createdTime;

	@Required
	@Column(name = "modified_time")
	private Date modifiedTime;

	@Column(name = "phone_number", length = 32)
	private String phoneNumber;

	@Column
	private boolean active;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEmailValid() {
		return emailValid;
	}

	public void setEmailValid(Boolean emailValid) {
		this.emailValid = emailValid;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@PrePersist
	protected void onCreate() {
		createdTime = new Date();
		modifiedTime = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		modifiedTime = new Date();
	}

	public List<Feed> getFeedList(long cur) {
		List<Integer> managerTeamIds = TeamMemberEntity.find("select pk.teamId from TeamMemberEntity where pk.userId = ? and status = ?", id, MemberStatus.MANAGER.value()).fetch();
		List<Integer> memberTeamIds = TeamMemberEntity.find("select pk.teamId from TeamMemberEntity where pk.userId = ? and status = ?", id, MemberStatus.MEMBER.value()).fetch();

		StringBuilder sb = new StringBuilder("select f from FeedEntity f where (userId = ?");
		if (managerTeamIds.size() > 0) {
			sb.append("or (target = ? and teamId in (");
			boolean first = true;
			for (int teamId : managerTeamIds) {
				if (first)
					sb.append(teamId);
				else
					sb.append(",").append(teamId);
				first = false;
			}
			sb.append("))");
		}
		if (memberTeamIds.size() > 0) {
			sb.append("or (target = ? and teamId in (");
			boolean first = true;
			for (int teamId : memberTeamIds) {
				if (first)
					sb.append(teamId);
				else
					sb.append(",").append(teamId);
				first = false;
			}
			sb.append("))");
		}
		if (cur > 0)
			sb.append(") and id < ?");
		else
			sb.append(")");
		sb.append(" order by id desc");

		Query query = JPA.em().createQuery(sb.toString());
		int idx = 1;
		query.setParameter(idx++, id);
		if (managerTeamIds.size() > 0)
			query.setParameter(idx++, MemberStatus.MANAGER.value());
		if (memberTeamIds.size() > 0)
			query.setParameter(idx++, MemberStatus.MEMBER.value());
		if (cur > 0)
			query.setParameter(idx++, cur);
		query.setMaxResults(Constants.MAX_FEED_FETCH_COUNT);

		List<FeedEntity> feedEntityList = query.getResultList();

		List<Feed> feedList = new ArrayList<Feed>();
		for (FeedEntity entity : feedEntityList) {
			feedList.add(entity.toFeed());
		}
		return feedList;
	}

	public List<TeamHint> getTeamHintList() {
		Query query = JPA.em().createQuery("select pk.teamId, status from TeamMemberEntity where pk.userId = ? and leavedAt is null and (status = ? or status = ?)");
		query.setParameter(1, id);
		query.setParameter(2, MemberStatus.MEMBER.value());
		query.setParameter(3, MemberStatus.MANAGER.value());
		List<Object[]> resultList = query.getResultList();

		List<TeamHint> teamHintList = new ArrayList<TeamHint>();
		for (Object[] objects : resultList) {
			TeamEntity teamEntity = TeamEntity.findById(objects[0]);
			TeamHint teamHint = teamEntity.toTeamHint((Integer) objects[1]);
			teamHintList.add(teamHint);
		}
		return teamHintList;
	}

	public static List<UserEntity> findByIds(List<Integer> ids) {
		List<UserEntity> result = new ArrayList<UserEntity>();
		if (ids == null || ids.isEmpty())
			return result;
		boolean first = true;
		StringBuilder sb = new StringBuilder("id in (");
		for (int id : ids) {
			if (first)
				sb.append(id);
			else
				sb.append(",").append(id);
			first = false;
		}
		sb.append(")");
		return find(sb.toString()).fetch();
	}

	public User toUser(Boolean manager) {
		User user = new User();
		user.setId(id);
		user.setName(name);
		user.setImageUrl(profileImageUrl);
		user.setEmail(email);
		user.setManager(Boolean.TRUE.equals(manager));
		return user;
	}

	public UserInfo toUserInfo(UserInfoEntity info) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(id);
		userInfo.setName(name);
		userInfo.setProfileImageUrl(profileImageUrl);
		userInfo.setPhoneNumber(phoneNumber);
		userInfo.setBirthYear(birthYear);
		userInfo.setHeight(info.getHeight());
		userInfo.setLocation1(info.getLocation1());
		userInfo.setLocation2(info.getLocation2());
		userInfo.setMainFoot(info.getMainFoot());
		userInfo.setWeight(info.getWeight());
		userInfo.setPosition(info.getPosition());
		userInfo.setOccupation(info.getOccupation());
		return userInfo;
	}

	public static UserEntity findByUserId(int id) {
		String cacheKey = new StringBuilder().append(CacheKey.userByUserId).append(id).toString();
		UserEntity user = Cache.get(cacheKey, UserEntity.class);
		if (user == null) {
			user = JPA.em().find(UserEntity.class, id);
			Cache.set(cacheKey, user, "8h");
		}
		return user;
	}

	@Override
	public UserEntity save() {
		super.save();
		String cacheKey = new StringBuilder().append(CacheKey.userByUserId).append(id).toString();
		Cache.delete(cacheKey);
		return this;
	}

	@Override
	public UserEntity delete() {
		super.delete();
		String cacheKey = new StringBuilder().append(CacheKey.userByUserId).append(id).toString();
		Cache.delete(cacheKey);
		return this;
	}
}
