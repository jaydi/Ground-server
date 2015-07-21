package models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
@Table(name = "join_infos")
public class JoinInfoEntity extends GenericModel {
	@Embeddable
	public static class PK implements Serializable {
		@Column(name = "match_id")
		private long matchId;

		@Column(name = "user_id")
		private int userId;

		@Column(name = "team_id")
		private int teamId;

		public PK() {
		}

		public PK(long matchId, int teamId, int userId) {
			this.matchId = matchId;
			this.userId = userId;
			this.teamId = teamId;
		}

		@Override
		public boolean equals(Object pk) {
			return matchId == ((PK) pk).matchId && userId == ((PK) pk).userId && teamId == ((PK) pk).teamId;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(matchId).append(userId).append(teamId).hashCode();
		}
	}

	@EmbeddedId
	private JoinInfoEntity.PK pk;

	@Required
	private boolean joined;

	public JoinInfoEntity() {
		pk = new JoinInfoEntity.PK();
	}

	public long getMatchId() {
		return pk.matchId;
	}

	public void setMatchId(long matchId) {
		pk.matchId = matchId;
	}

	public int getUserId() {
		return pk.userId;
	}

	public void setUserId(int userId) {
		pk.userId = userId;
	}

	public int getTeamId() {
		return pk.teamId;
	}

	public void setTeamId(int teamId) {
		pk.teamId = teamId;
	}

	public boolean getJoined() {
		return joined;
	}

	public void setJoined(boolean joined) {
		this.joined = joined;
	}

	public static int getJoinedMembersCount(long matchId, int teamId) {
		// TODO true로 바꿀 수 있는지
		return (int) count("pk.matchId = ? and pk.teamId = ? and joined = ?", matchId, teamId, true);
	}
}
