package com.mir.ven;

import java.util.concurrent.Callable;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import com.mir.ems.globalVar.global;

public class EventACK_Client implements Callable<Boolean> {

	private String subPath, eventID;
	private StringBuilder httpResponse = new StringBuilder();
	static HttpClientBuilder hcb = HttpClientBuilder.create();
	static HttpClient clientEventACK = hcb.build();

	public EventACK_Client(String eventID) {
		setSubPath("EventACK");
		setEventID(eventID);
	}

	@Override
	public Boolean call() {

		System.out.println(global.vtnURL);
		String temp = global.vtnURL.split("/OpenADR2")[0];
		temp = temp.replaceAll("8080", "12345");
		temp += "/EMAP/"+global.ParentnNodeID+"/1.0b/";
		
		HttpPost post = new HttpPost(temp + getSubPath());

		
		try {

			JSONObject json = new JSONObject();
			json.put("SrcEMA", global.HASHED_VEN_NAME);
			json.put("DestEMA", global.ParentnNodeID);
			json.put("service", "eventACK");
			json.put("eventID", getEventID());

			post.setEntity(new StringEntity(json.toString()));

			clientEventACK.execute(post);
			
//			Async
			return true;

		} catch (Exception e) {

			e.printStackTrace();
			return false;

		}
	}

	public void setHttpResponse(String httpResponse) {
		this.httpResponse = this.httpResponse.append(httpResponse);
	}

	public String getSubPath() {
		return subPath;
	}

	public void setSubPath(String subPath) {
		this.subPath = subPath;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

}
