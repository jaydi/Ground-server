package utils;

import java.util.List;
import java.util.Map;

import play.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	private static ObjectMapper mapper = new ObjectMapper();

	public static String toJson(Map<String, Object> obj) {
		if (obj == null || obj.isEmpty())
			return "";
		String jsonString;
		try {
			jsonString = mapper.writeValueAsString(obj);
			return jsonString;
		} catch (JsonProcessingException e) {
			Logger.info("JsonError", e.getMessage());
			return "";
		}
	}

	public static String toJson(List<String> obj) {
		if (obj == null || obj.isEmpty())
			return "";
		String jsonString;
		try {
			jsonString = mapper.writeValueAsString(obj);
			return jsonString;
		} catch (JsonProcessingException e) {
			Logger.info("JsonError", e.getMessage());
			return "";
		}
	}
}
