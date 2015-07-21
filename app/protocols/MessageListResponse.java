package protocols;

import java.util.List;

public class MessageListResponse extends DefaultResponse {
	private List<IMessage> messageList;

	public List<IMessage> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<IMessage> messageList) {
		this.messageList = messageList;
	}
}
