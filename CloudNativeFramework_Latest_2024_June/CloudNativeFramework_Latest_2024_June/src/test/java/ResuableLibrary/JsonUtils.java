package ResuableLibrary;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
	public static Map<String, Object> readJsonAsMap(String filename) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		try {
			@SuppressWarnings("resource")
			String myJson = new Scanner(new File(filename)).useDelimiter("\\Z").next();
			JSONObject myJsonObject = new JSONObject(myJson);
			mapObj = getJsonValue(myJsonObject.toString());
		} catch (Exception ex) {
			System.out.println("Failed to read " + filename + " JSON file");
		}
		return mapObj;
	}
	
	

	public static String getJsonValueByKey(String jsonReq, String key) {
		JSONObject json = new JSONObject(jsonReq);
		boolean exists = json.has(key);
		Iterator<?> keys;
		String nextKeys;
		String val = "";
		if (!exists) {
			keys = json.keys();
			while (keys.hasNext()) {
				nextKeys = (String) keys.next();
				System.out.println("Key :" + nextKeys);
				try {
					if (json.get(nextKeys) instanceof JSONObject) {
						return getJsonValueByKey(json.getJSONObject(nextKeys).toString(), key);
					} else if (json.get(nextKeys) instanceof JSONArray) {
						JSONArray jsonArray = json.getJSONArray(nextKeys);
						int i = 0;
						if (i < jsonArray.length())
							do {
								String jsonArrayString = jsonArray.get(i).toString();
								JSONObject innerJson = new JSONObject(jsonArrayString);
								return getJsonValueByKey(innerJson.toString(), key);
							} while (i < jsonArray.length());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			val = json.get(key).toString();
		}
		return val;
	}

	public static Map<String, Object> getJsonValue(String jsonReq) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject object = new JSONObject(jsonReq);
			JSONArray array = object.getJSONArray("interactions");
			map.put("description", getValue(array.getJSONObject(0), "description"));
			map.put("method", getValue(array.getJSONObject(0).getJSONObject("request"), "method").toLowerCase());
			map.put("path", getValue(array.getJSONObject(0).getJSONObject("request"), "path"));
			map.put("status", getValue(array.getJSONObject(0).getJSONObject("response"), "status"));
			map.put("contentType",
					getValue(array.getJSONObject(0).getJSONObject("request").getJSONObject("headers"), "Content-Type"));
			map.put("reqBody", getValue(array.getJSONObject(0).getJSONObject("request"), "body"));
			map.put("resBody", getValue(array.getJSONObject(0).getJSONObject("response"), "body"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static String getValue(JSONObject object, String key) {
		String value = "";
		try {
			value = object.get(key).toString().trim();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return value;

	}

}
