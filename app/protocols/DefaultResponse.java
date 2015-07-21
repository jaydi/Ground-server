package protocols;

public class DefaultResponse {
	public static enum ResponseCode {
		OK(0),
		INVALID_PARAMETERS(100),
		INVALID_SESSION_KEY(101),
		EMAIL_NOT_EXIST(102),
		DUPLICATED_EMAIL(200),
		WRONG_PASSWORD(201),
		WRONG_EMAIL(202),
		ERROR(500);

		private final int value;

		private ResponseCode(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	}

	private int code = 0;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
