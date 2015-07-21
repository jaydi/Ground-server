package models;

import java.math.BigInteger;
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
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import protocols.Post;
import protocols.Team;
import protocols.TeamHint;
import protocols.TeamInfo;
import utils.Constants;

@Entity
@Table(name = "teams")
public class TeamEntity extends GenericModel {
	@Id
	@GeneratedValue
	private int id;

	@Required
	@Column(length = 40)
	private String name;

	private String address;
	private double latitude;
	private double longitude;

	@Column(name = "image_url", length = 40)
	private String imageUrl;

	@Column(name = "total_birth")
	private int totalBirth;

	@Required
	private int score;

	private int win;
	private int lose;
	private int draw;

	@Column(name = "member_count")
	private int memberCount;

	@Required
	@Column(name = "created_at")
	private Date createdAt;

	@Required
	@Column(name = "modified_at")
	private Date modifiedAt;

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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTotalBirth() {
		return totalBirth;
	}

	public void setTotalBirth(int totalBirth) {
		this.totalBirth = totalBirth;
	}

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
		modifiedAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		modifiedAt = new Date();
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getLose() {
		return lose;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("id=").append(id).append(",address=").append(address).append(",modifiedAt=").append(modifiedAt).append(",createdAt=").append(createdAt).toString();
	}

	public TeamHint toTeamHint(int status) {
		TeamHint teamHint = new TeamHint();
		teamHint.setId(id);
		teamHint.setName(name);
		teamHint.setImageUrl(imageUrl);
		teamHint.setManaged(status == MemberStatus.MANAGER.value());
		return teamHint;
	}

	public static List<TeamEntity> findByIds(List<Integer> ids) {
		List<TeamEntity> result = new ArrayList<TeamEntity>();
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

	public List<Post> getPostList(long cur) {
		Query query;
		if (cur == 0) {
			query = JPA
					.em()
					.createNativeQuery(
							"SELECT p.id, p.team_id, p.user_id, p.type, p.message, p.extra, p.created_at, p.comment_count, u.name, u.profile_image_url FROM posts p LEFT OUTER JOIN users u ON p.user_id = u.id WHERE p.team_id = ? ORDER BY p.id DESC");

		} else {
			query = JPA
					.em()
					.createNativeQuery(
							"SELECT p.id, p.team_id, p.user_id, p.type, p.message, p.extra, p.created_at, p.comment_count, u.name, u.profile_image_url FROM posts p LEFT OUTER JOIN users u ON p.user_id = u.id WHERE p.team_id = ? AND p.id < ? ORDER BY p.id DESC");
			query.setParameter(2, cur);
		}

		query.setParameter(1, id);
		query.setMaxResults(Constants.MAX_POST_FETCH_COUNT);
		List<Object[]> queryResult = query.getResultList();

		List<Post> postList = new ArrayList<Post>();
		for (Object[] obj : queryResult) {
			Post post = new Post();
			if (obj[0] instanceof Long)
				post.setId(((Long) obj[0]).longValue());
			else
				post.setId(((BigInteger) obj[0]).longValue());
			post.setTeamId((Integer) obj[1]);
			post.setUserId((Integer) obj[2]);
			post.setType((Integer) obj[3]);
			post.setMessage((String) obj[4]);
			post.setExtra((String) obj[5]);
			post.setCreatedAt(((Date) obj[6]).getTime());
			post.setCommentCount((Integer) obj[7]);
			post.setUserName((String) obj[8]);
			post.setUserImageUrl((String) obj[9]);
			postList.add(post);
		}
		return postList;
	}

	public Team toTeam() {
		Team team = new Team();
		team.setId(id);
		team.setName(name);
		team.setImageUrl(imageUrl);
		team.setScore(score);
		team.setAvgBirth(memberCount == 0 ? 0 : (float) totalBirth / memberCount);
		team.setWin(win);
		team.setDraw(draw);
		team.setLose(lose);
		return team;
	}

	public TeamInfo toTeamInfo() {
		TeamInfo teamInfo = new TeamInfo();
		teamInfo.setId(id);
		teamInfo.setName(name);
		teamInfo.setAddress(address);
		teamInfo.setImageUrl(imageUrl);
		teamInfo.setScore(score);
		teamInfo.setAvgBirth(memberCount == 0 ? 0 : (float) totalBirth / memberCount);
		teamInfo.setWin(win);
		teamInfo.setDraw(draw);
		teamInfo.setLose(lose);
		teamInfo.setMembersCount(memberCount);
		teamInfo.setLatitude(latitude);
		teamInfo.setLongitude(longitude);
		return teamInfo;
	}

	public void addMember(int birthYear) {
		Query query = JPA.em().createQuery("UPDATE TeamEntity SET totalBirth = totalBirth + ?, memberCount = memberCount + 1 where id = ?");
		query.setParameter(1, birthYear);
		query.setParameter(2, id);
		query.executeUpdate();
	}

	public void removeMember(int birthYear) {
		Query query = JPA.em().createQuery("UPDATE TeamEntity SET totalBirth = totalBirth - ?, memberCount = memberCount - 1 where id = ?");
		query.setParameter(1, birthYear);
		query.setParameter(2, id);
		query.executeUpdate();
	}

	public static List<TeamInfo> searchTeamNearby(double latitude, double longitude, int distance) {
		List<TeamInfo> teamList = new ArrayList<TeamInfo>();

		Query query = JPA
				.em()
				.createNativeQuery(
						"select id, name, address, image_url, total_birth, member_count, win, lose, draw from teams where latitude between ? + ?/(111.1/cos(radians(?))) and ? - ?/(111.1/cos(radians(?))) and longitude between ? - ?/111.1 and ? + ?/111.1");

		query.setParameter(1, latitude);
		query.setParameter(2, distance);
		query.setParameter(3, longitude);

		query.setParameter(4, latitude);
		query.setParameter(5, distance);
		query.setParameter(6, longitude);

		query.setParameter(7, longitude);
		query.setParameter(8, distance);

		query.setParameter(9, longitude);
		query.setParameter(10, distance);

		List<Object[]> resultList = query.getResultList();
		for (Object[] results : resultList) {
			TeamInfo team = new TeamInfo();
			team.setId((Integer) results[0]);
			team.setName((String) results[1]);
			team.setAddress((String) results[2]);
			team.setImageUrl((String) results[3]);
			team.setAvgBirth((Integer) results[4] / (Integer) results[5]);
			team.setWin((Integer) results[6]);
			team.setLose((Integer) results[7]);
			team.setDraw((Integer) results[8]);

			teamList.add(team);
		}

		return teamList;
	}
}
