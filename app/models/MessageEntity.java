package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import protocols.IMessage;
import utils.Constants;

@Entity
@Table(name = "messages")
public class MessageEntity extends GenericModel {
	@Id
	@GeneratedValue
	private long id;

	@Required
	@Column(name = "match_id")
	private long matchId;

	@Required
	@Column(name = "team_id")
	private int teamId;

	@Required
	private String message;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static List<IMessage> getIMessageList(long matchId, long cur) {
		List<IMessage> messageList = new ArrayList<IMessage>();

		Query query;
		if (cur == 0) {
			query = JPA.em().createQuery(
					"select m, t.name, t.imageUrl from MessageEntity m, TeamEntity t where m.teamId = t.id and m.matchId = ? order by m.id desc");
		} else {
			query = JPA
					.em()
					.createQuery(
							"select m, t.name, t.imageUrl from MessageEntity m, TeamEntity t where m.teamId = t.id and m.matchId = ? and m.id < ? order by m.id desc");
			query.setParameter(2, cur);
		}
		
		query.setParameter(1, matchId);
		query.setMaxResults(Constants.MAX_MESSAGE_FETCH_COUNT);
		
		List<Object[]> resultList = query.getResultList();
		for (Object[] result : resultList) {
			MessageEntity entity = (MessageEntity) result[0];
			messageList.add(entity.toIMessage((String) result[1], (String) result[2]));
		}

		return messageList;
	}

	public IMessage toIMessage(String teamName, String teamImageUrl) {
		IMessage iMessage = new IMessage(id, matchId, teamId, message);
		iMessage.setTeamName(teamName);
		iMessage.setTeamImageUrl(teamImageUrl);
		return iMessage;
	}
}
