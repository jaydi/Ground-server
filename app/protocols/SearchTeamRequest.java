package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SearchTeamRequest {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}
}
