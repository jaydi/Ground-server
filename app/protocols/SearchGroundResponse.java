package protocols;

import java.util.List;

public class SearchGroundResponse extends DefaultResponse {
	private List<Ground> groundList;

	public List<Ground> getGroundList() {
		return groundList;
	}

	public void setGroundList(List<Ground> groundList) {
		this.groundList = groundList;
	}
}
