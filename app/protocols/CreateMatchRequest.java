package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class CreateMatchRequest {
	private int teamId;
	private int groundId;
	private int awayTeamId = 0;
	private long startTime;
	private long endTime;
	private String description;
	private boolean open;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getGroundId() {
		return groundId;
	}

	public void setGroundId(int groundId) {
		this.groundId = groundId;
	}

	public int getAwayTeamId() {
		return awayTeamId;
	}

	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("groundId", groundId).append("startTime", startTime).append("endTime", endTime)
				.append("awayTeamId", awayTeamId).append("description", description).append("open", open).toString();
	}
}
