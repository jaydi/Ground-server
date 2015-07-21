package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Query;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

@Entity
@Table(name = "team_members")
public class TeamMemberEntity extends GenericModel {
	public static enum MemberStatus { // TODO value 정리 // TODO status 에 INDEX 잘 걸려 있는지 검사 
		INVITED(-1),
		PENDING(0),
		MANAGER(1),
		MEMBER(2),
		DEACTIVATED(3);

		private final int value;

		private MemberStatus(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}

		public static MemberStatus getInstance(int value) {
			for (MemberStatus status : values())
				if (status.value == value)
					return status;
			return null;
		}
	};

	@Embeddable
	public static class PK implements Serializable {
		@Column(name = "team_id")
		private int teamId;

		@Column(name = "user_id")
		private int userId;

		public PK() {
		}

		public PK(int teamId, int userId) {
			this.teamId = teamId;
			this.userId = userId;
		}

		@Override
		public boolean equals(Object pk) {
			return teamId == ((PK) pk).teamId && userId == ((PK) pk).userId;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(teamId).append(userId).hashCode();
		}
	}

	@EmbeddedId
	private TeamMemberEntity.PK pk;

	@Required
	@Column(name = "joined_at")
	private Date joinedAt;

	@Column(name = "leaved_at")
	private Date leavedAt;

	@Required
	private int status;

	public TeamMemberEntity() {
		pk = new TeamMemberEntity.PK();
	}

	public int getTeamId() {
		return pk.teamId;
	}

	public void setTeamId(int teamId) {
		pk.teamId = teamId;
	}

	public int getUserId() {
		return pk.userId;
	}

	public void setUserId(int userId) {
		pk.userId = userId;
	}

	public Date getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(Date joinedAt) {
		this.joinedAt = joinedAt;
	}

	public Date getLeavedAt() {
		return leavedAt;
	}

	public void setLeavedAt(Date leavedAt) {
		this.leavedAt = leavedAt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static List<Integer> findActiveUserIdsByTeamId(int teamId) {
		Query query = JPA.em().createQuery(
				"select pk.userId from TeamMemberEntity where pk.teamId = ? and (status = 1 or status = 2)");
		query.setParameter(1, teamId);
		return query.getResultList();
	}

	public static List<Integer> findManagerIdsByTeamId(int teamId) {
		Query query = JPA.em().createQuery("select pk.userId from TeamMemberEntity where pk.teamId = ? and status = 1");
		query.setParameter(1, teamId);
		return query.getResultList();
	}

	public static List<UserEntity> findActiveUsersByTeamId(int teamId) {
		Query query = JPA.em().createQuery(
				"select pk.userId from TeamMemberEntity where pk.teamId = ? and (status = 1 or status = 2)");
		query.setParameter(1, teamId);
		List<Integer> userIdList = query.getResultList();
		return UserEntity.findByIds(userIdList);
	}

	public static List<UserEntity> findPendingUsersByTeamId(int teamId) {
		Query query = JPA.em().createQuery("select pk.userId from TeamMemberEntity where pk.teamId = ? and status = 0");
		query.setParameter(1, teamId);
		List<Integer> userIdList = query.getResultList();
		return UserEntity.findByIds(userIdList);
	}

	public static List<TeamEntity> findPendingTeamsByUserId(int userId) {
		Query query = JPA.em().createQuery("select pk.teamId from TeamMemberEntity where pk.userId = ? and status = -1");
		query.setParameter(1, userId);
		List<Integer> userIdList = query.getResultList();
		return TeamEntity.findByIds(userIdList);
	}

	public static List<TeamMemberEntity> findByIds(int teamId, List<Integer> ids) {
		List<TeamMemberEntity> result = new ArrayList<TeamMemberEntity>();
		if (ids == null || ids.isEmpty())
			return result;
		boolean first = true;
		StringBuilder sb = new StringBuilder("pk.teamId = ");
		sb.append(teamId);
		sb.append(" and pk.userId in (");
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

	public static List<Integer> findNotYetSurveyMemberIds(int teamId, long matchId) {
		Query query = JPA
				.em()
				.createNativeQuery(
						"SELECT user_id FROM team_members where team_id = ? and user_id NOT IN ( SELECT user_id from join_infos where match_id = ? and team_id = ?)");
		query.setParameter(1, teamId);
		query.setParameter(2, matchId);
		query.setParameter(3, teamId);
		return query.getResultList();
	}
}
