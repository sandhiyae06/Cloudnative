package com.MicroserviceTesting;

import java.util.HashMap;
import java.util.Map;

public class EndPoints {

	public static Map<String, String> endPointList = new HashMap<String, String>() {{
	    put("ALLUSER", "/api/users?page=2");
	    put("CREATEUSER", "/api/users");
	}};
}
