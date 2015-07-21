package models;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Query;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import protocols.JoinUser;
import protocols.Match;
import protocols.MatchInfo;
import protocols.SMatch;

@Entity
@Table(name = "matches")
public class MatchEntity extends GenericModel {
	public enum MatchStatus {
		HOME_ONLY(0),
		INVITE(1),
		REQUEST(2),
		MATCHING_COMPLETED(3),
		READY_SCORE(4),
		HOME_SCORE(5),
		AWAY_SCORE(6),
		SCORE_COMPLETED(7);
		private final int value;

		private MatchStatus(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}

		public static MatchStatus getInstance(int value) {
			for (MatchStatus status : values())
				if (status.value == value)
					return status;
			return null;
		}
	};

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Required
	@Column(name = "home_team_id")
	private int homeTeamId;

	@Column(name = "away_team_id")
	private int awayTeamId;

	@Required
	private int status;

	@Column(name = "home_score")
	private int homeScore;

	@Column(name = "away_score")
	private int awayScore;

	@Column(length = 1024)
	private String description;

	@Column(name = "ground_id")
	private int groundId;

	private boolean open;

	@Required
	@Column(name = "created_at")
	private Date createdAt;

	@Required
	@Column(name = "date")
	private String date;

	@Required
	@Column(name = "start_time")
	private Date startTime;

	@Required
	@Column(name = "end_time")
	private Date endTime;

	@Column(name = "home_survey")
	private boolean homeSurvey;

	@Column(name = "away_survey")
	private boolean awaySurvey;

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getHomeTeamId() {
		return homeTeamId;
	}

	public void setHomeTeamId(int homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public int getAwayTeamId() {
		return awayTeamId;
	}

	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getGroundId() {
		return groundId;
	}

	public void setGroundId(int groundId) {
		this.groundId = groundId;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean isHomeSurvey() {
		return homeSurvey;
	}

	public void setHomeSurvey(boolean homeSurvey) {
		this.homeSurvey = homeSurvey;
	}

	public boolean isAwaySurvey() {
		return awaySurvey;
	}

	public void setAwaySurvey(boolean awaySurvey) {
		this.awaySurvey = awaySurvey;
	}

	public Match toMatch() {
		GroundEntity groundEntity = GroundEntity.findById(groundId);
		String groundName = "";
		if (groundEntity != null)
			groundName = groundEntity.getName();
		// TODO

		List<TeamEntity> teams = TeamEntity.find("id = ? or id = ?", homeTeamId, awayTeamId).fetch();

		TeamEntity homeTeam, awayTeam = null;

		if (teams.get(0).getId() == homeTeamId) {
			homeTeam = teams.get(0);
			if (teams.size() == 2)
				awayTeam = teams.get(1);
		} else {
			homeTeam = teams.get(1);
			awayTeam = teams.get(0);
		}

		Match match = new Match();
		match.setId(id);
		match.setHomeTeamId(homeTeamId);
		match.setAwayTeamId(awayTeamId);
		match.setHomeTeamName(homeTeam.getName());
		if (awayTeam != null)
			match.setAwayTeamName(awayTeam.getName());
		match.setStatus(status);
		match.setHomeScore(homeScore);
		match.setAwayScore(awayScore);
		match.setDescription(description);
		match.setStartTime(startTime.getTime());
		match.setEndTime(endTime.getTime());
		match.setGroundName(groundName);
		match.setOpen(open);
		return match;
	}

	public SMatch toSMatch(String groundName, double distance) {
		TeamEntity homeTeam = TeamEntity.findById(homeTeamId);

		SMatch match = new SMatch();
		match.setHomeTeamId(homeTeamId);
		match.setHomeTeamName(homeTeam.getName());
		match.setGroundName(groundName);
		match.setStartTime(startTime.getTime());
		match.setEndTime(endTime.getTime());
		match.setId(id);
		match.setDistance(distance);
		return match;
	}

	public MatchInfo toMatchInfo(int userId, int teamId) {
		MatchInfo matchInfo = new MatchInfo();
		List<TeamEntity> teams = TeamEntity.find("id = ? or id = ?", homeTeamId, awayTeamId).fetch();

		// TODO throw exception if invalid match

		TeamEntity homeTeam, awayTeam = null;

		if (teams.get(0).getId() == homeTeamId) {
			homeTeam = teams.get(0);
			if (teams.size() == 2)
				awayTeam = teams.get(1);
		} else {
			homeTeam = teams.get(1);
			awayTeam = teams.get(0);
		}

		GroundEntity groundEntity = GroundEntity.findById(groundId);
		// TODO

		matchInfo.setId(id);
		matchInfo.setHomeTeamId(homeTeamId);
		matchInfo.setAwayTeamId(awayTeamId);
		matchInfo.setHomeImageUrl(homeTeam.getImageUrl());
		matchInfo.setHomeTeamName(homeTeam.getName());
		matchInfo.setHomeScore(homeScore);
		matchInfo.setHomeJoinedMembersCount(JoinInfoEntity.getJoinedMembersCount(id, homeTeamId));
		if (awayTeam != null) {
			matchInfo.setAwayImageUrl(awayTeam.getImageUrl());
			matchInfo.setAwayTeamName(awayTeam.getName());
			matchInfo.setAwayScore(awayScore);
			matchInfo.setAwayJoinedMembersCount(JoinInfoEntity.getJoinedMembersCount(id, awayTeamId));
		}
		matchInfo.setStartTime(startTime.getTime());
		matchInfo.setEndTime(endTime.getTime());
		if (groundEntity != null)
			matchInfo.setGround(groundEntity.toGround());

		JoinInfoEntity joinInfo = JoinInfoEntity.findById(new JoinInfoEntity.PK(id, teamId, userId));
		if (joinInfo == null)
			matchInfo.setJoin(0);
		else
			matchInfo.setJoin(joinInfo.getJoined() ? 2 : 1);

		matchInfo.setStatus(status);
		matchInfo.setDescription(description);
		matchInfo.setOpen(open);
		if (teamId == homeTeamId)
			matchInfo.setAskSurvey(homeSurvey);
		else
			matchInfo.setAskSurvey(awaySurvey);
		return matchInfo;
	}

	public List<JoinUser> getJoinUserList(int teamId) {
		List<JoinInfoEntity> joinList = JoinInfoEntity.find("pk.matchId = ? and pk.teamId = ?", id, teamId).fetch();
		Map<Integer, Boolean> joinMap = new HashMap<Integer, Boolean>();
		for (JoinInfoEntity entity : joinList)
			joinMap.put(entity.getUserId(), entity.getJoined());

		List<JoinUser> joinUserList = new ArrayList<JoinUser>();
		List<UserEntity> userList = TeamMemberEntity.findActiveUsersByTeamId(teamId);

		for (UserEntity user : userList) {
			Boolean joined = joinMap.get(user.getId());
			joinUserList.add(new JoinUser(user, joined));
		}
		return joinUserList;
	}

	public static List<SMatch> searchMatch(long startTime, long endTime, double longitude, double latitude, int distance) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

		Date startDate = new Date(startTime);
		Date endDate = new Date(endTime);

		String dateStart = dateFormatter.format(startDate);
		String dateEnd = dateFormatter.format(endDate);
		String timeStart = timeFormatter.format(startDate);
		String timeEnd = timeFormatter.format(endDate);

		Query query = JPA
				.em()
				.createNativeQuery(
						"SELECT m.id, m.home_team_id, m.start_time, m.end_time, g.name, (6371 * acos( cos(radians(g.latitude)) * cos(radians(?)) * cos(radians(g.longitude)-radians(?)) + sin(radians(g.latitude)) * sin(radians(?)) )) as dis "
								+ "FROM matches as m inner join grounds as g on m.ground_id = g.id WHERE "
								+ "g.latitude BETWEEN ? + ? / (111.1/COS(RADIANS(?))) AND ? - ? / (111.1/COS(RADIANS(?))) "
								+ "AND g.longitude BETWEEN ? - ? / 111.1 AND ? + ? / 111.1 "
								+ "AND left(m.start_time, 10) BETWEEN ? and ? AND substr(m.start_time, 12,5) between ? and ? AND status = 0 GROUP BY m.id HAVING dis < ?");

		query.setParameter(1, latitude);
		query.setParameter(2, longitude);
		query.setParameter(3, latitude);

		query.setParameter(4, latitude);
		query.setParameter(5, (double) distance);
		query.setParameter(6, longitude);
		query.setParameter(7, latitude);
		query.setParameter(8, (double) distance);
		query.setParameter(9, longitude);

		query.setParameter(10, longitude);
		query.setParameter(11, (double) distance);
		query.setParameter(12, longitude);
		query.setParameter(13, (double) distance);

		query.setParameter(14, dateStart);
		query.setParameter(15, dateEnd);
		query.setParameter(16, timeStart);
		query.setParameter(17, timeEnd);

		query.setParameter(18, (double) distance);

		List<Object[]> queryResult = query.getResultList();
		List<SMatch> result = new ArrayList<SMatch>();
		for (Object[] obj : queryResult) {
			MatchEntity entity = new MatchEntity();
			if (obj[0] instanceof Long)
				entity.setId(((Long) obj[0]).longValue());
			else
				entity.setId(((BigInteger) obj[0]).longValue());
			entity.setHomeTeamId((Integer) obj[1]);
			entity.setStartTime((Date) obj[2]);
			entity.setEndTime((Date) obj[3]);
			result.add(entity.toSMatch((String) obj[4], (Double) obj[5]));
		}
		return result;
	}
}