package com.pwse.communicationserver.controllers.helpers;

import org.json.JSONObject;

public class Messenger {

	private Messenger() { }



	public static JSONObject createMsgWithAction(String action) {
		JSONObject json = new JSONObject();
		json.put("action", action);

		return json;
	}

	public static String getActionFromJson(JSONObject json) {
		return json.getString("action");
	}
}
