package protocols;

import java.util.List;

public class JoinMemberListResponse extends DefaultResponse {
	private List<JoinUser> userList;

	public List<JoinUser> getUserList() {
		return userList;
	}

	public void setUserList(List<JoinUser> userList) {
		this.userList = userList;
	}
}
