package com.mir.update.database;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Test {


	public static int cnt = 0;
	
	public static void main(String... args) throws JSONException {

		JSONObject object = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		object.put("deviceEMAID", "LED1");
		object.put("deviceType", "LED1");
		object.put("power", 100);
		object.put("state", "ON");
		object.put("dimming", 9);
		object.put("priority", 1);

		jsonArray.put(object);
		object = new JSONObject();

		object.put("deviceEMAID", "LED2");
		object.put("deviceType", "LED2");
		object.put("power", 200);
		object.put("state", "OFF");
		object.put("dimming", 8);
		object.put("priority", 2);
		jsonArray.put(object);

		System.out.println(jsonArray.toString());

		ConcurrentHashMap<String, String> aa = new ConcurrentHashMap<>();

		JSONObject top = new JSONObject();

		top.put("topology", jsonArray);
		top.put("emaID", "HYUNJIN");
		top.put("time", new Date(System.currentTimeMillis()).toString());
		System.out.println(top);

		// ====================================

		String recvData = top.toString();
		JSONObject jsonParse = new JSONObject(recvData);

		int length = jsonParse.getJSONArray("topology").length();

		for (int i = 0; i < length; i++) {
			System.out.println(jsonParse.getJSONArray("topology").get(i).toString());

		}

		final int SIZE = 7;

		String[][] save = new String[2000][SIZE];

		save[0][0] = "DeviceType";
		save[0][1] = "DeviceEMAID";
		save[0][2] = "POWER";
		save[0][3] = "DIMMING";
		save[0][4] = "STATE";
		save[0][5] = "PRIORITY";
		save[0][6] = "TIMESTAMP";

		String timeStamp = jsonParse.getString("time");

		for (int i = 1; i <= length; i++) {
			JSONObject temp = new JSONObject(jsonParse.getJSONArray("topology").get(i - 1).toString());

			for (int j = 0; j < SIZE; j++) {

				if (j == 0) {
					save[i][j] = temp.getString("deviceType");
				}
				if (j == 1) {
					save[i][j] = temp.getString("deviceEMAID");
				}
				if (j == 2) {
					save[i][j] = temp.getString("power");
				}
				if (j == 3) {
					save[i][j] = temp.getString("dimming");
				}
				if (j == 4) {
					save[i][j] = temp.getString("state");
				}
				if (j == 5) {
					save[i][j] = temp.getString("priority");
				}
				if (j == 6) {
					save[i][j] = timeStamp;
				}

			}
		}

		for (int i = 1; i <= length; i++) {
			JSONObject temp = new JSONObject(jsonParse.getJSONArray("topology").get(i - 1).toString());

			for (int j = 0; j < SIZE; j++) {

				if (j == 0) {
					save[i][j] = temp.getString("deviceType");
				}
				if (j == 1) {
					save[i][j] = temp.getString("deviceEMAID");
				}
				if (j == 2) {
					save[i][j] = temp.getString("power");
				}
				if (j == 3) {
					save[i][j] = temp.getString("dimming");
				}
				if (j == 4) {
					save[i][j] = temp.getString("state");
				}
				if (j == 5) {
					save[i][j] = temp.getString("priority");
				}
				if (j == 6) {
					save[i][j] = timeStamp;
				}

			}
		}

		for (int i = 0; i < 23; i++) {
			System.out.println("cnt"+cnt);
//			System.out.print(save[i][2] + "\t");
			if (cnt == 20) {
				for (int j = 0; j <= 6; j++) {
//					if ((save[i][j]) != null) {
						System.out.print(save[i][j] + "\t");
						System.out.println("test");
//					}

					if (save[i][j] == null) {
						break;
					}
				}
			}

			else if (cnt < 21) {
				cnt++;
			}

		}

		// if (cnt == 20) {
		// System.out.println();
		//
		// System.out.println();
		//
		// }

		// for(int i=0; i<= 10; i++){
		// for (int j=0; j<SIZE; j++){
		// System.out.print(save[i][j]+"\t");
		// }
		// System.out.println();
		// }

	}

	class Aa {

		Aa() {

		}

		void bb() {
			System.out.println("aa");
		}
	}

}
