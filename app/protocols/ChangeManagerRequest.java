package protocols;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ChangeManagerRequest {
	private int teamId;
	private List<Integer> newManagerIdList;
	private List<Integer> oldManagerIdList;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public List<Integer> getNewManagerIdList() {
		return newManagerIdList;
	}

	public void setNewManagerIdList(List<Integer> newManagerIdList) {
		this.newManagerIdList = newManagerIdList;
	}

	public List<Integer> getOldManagerIdList() {
		return oldManagerIdList;
	}

	public void setOldManagerIdList(List<Integer> oldManagerIdList) {
		this.oldManagerIdList = oldManagerIdList;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("newManagerIdList", newManagerIdList).append("oldManagerIdList", oldManagerIdList).toString();
	}
}
