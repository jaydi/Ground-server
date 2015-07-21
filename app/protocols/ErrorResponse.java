package protocols;

import play.Logger;
import utils.ProtocolCodec;

public class ErrorResponse extends DefaultResponse {
	private ErrorResponse() {
	}

	public static String newInstance(ResponseCode code) {
		return newInstance(code, null);
	}

	public static String newInstance(ResponseCode code, String message) {
		try {
			ErrorResponse response = new ErrorResponse();
			response.setCode(code.value());
			Logger.error(String.format("ProtocolError: errorCode=%d, message=%s", code.value(), message));
			return ProtocolCodec.encode(response);
		} catch (Exception e) {
		}
		return "";
	}
}
