package utils;

import java.io.ByteArrayOutputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProtocolCodec {
	private static ObjectMapper mapper = new ObjectMapper();

	public static String encode(Object obj) throws Exception {
		String jsonString;
		try {
			jsonString = mapper.writeValueAsString(obj);
			return jsonString;
		} catch (JsonProcessingException e) {
			throw e;
		}
	}

	public static <T> T decode(ByteArrayOutputStream stream, Class<T> klass) throws Exception {
		try {
			T t = mapper.readValue(stream.toByteArray(), klass);
			return t;
		} catch (Exception e) {
			throw e;
		}
	}

	public static <T> T decode(String body, Class<T> klass) throws Exception {
		try {
			T t = mapper.readValue(body, klass);
			return t;
		} catch (Exception e) {
			throw e;
		}
	}
}
