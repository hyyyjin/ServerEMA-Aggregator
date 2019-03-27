package com.mir.ven;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.mir.ems.globalVar.global;
import com.ning.http.client.AsyncHttpClient;

public class SummaryRegister extends Thread {

	private StringBuilder httpResponse_event = new StringBuilder();
	HttpClientBuilder hcb = HttpClientBuilder.create();
	HttpClient client = hcb.build();
	HttpPost post;
	public String eventID;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			sendGet();
		} catch (UnsupportedOperationException | IOException | ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendGet() throws UnsupportedOperationException, IOException, ParserConfigurationException, SAXException,
			TransformerException {

		HttpGet get = new HttpGet(global.summaryURL + global.CHILD_ID);
		HttpClientBuilder hcb_push_event = HttpClientBuilder.create();
		HttpClient client_push_event = hcb_push_event.build();
		HttpResponse eventResponse = client_push_event.execute(get);

		BufferedReader event_rd = new BufferedReader(new InputStreamReader(eventResponse.getEntity().getContent()));

		String event_line = "";
		httpResponse_event = new StringBuilder();
		while ((event_line = event_rd.readLine()) != null) {
			setHttpResponse_event(event_line);
		}

		AsyncHttpClient client = new AsyncHttpClient();
		JSONObject json = new JSONObject();
		try {
			json.put("SrcEMA", global.HASHED_VEN_NAME);
			json.put("DestEMA", global.ParentnNodeID);
			json.put("service", "summaryACK");
			json.put("reportID", "reportID");

			client.preparePost("http://166.104.28.51:12345/EMAP/EMS1/1.0b/SummaryACK")
					.setBody(json.toString().getBytes()).setHeader("Content-Type", "application/json").execute();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (getHttpResponse_event().toString().contains("SummaryReport"))
			sendGet();

	}

	public StringBuilder getHttpResponse_event() {
		return httpResponse_event;
	}

	public void setHttpResponse_event(String httpResponse_event) {
		this.httpResponse_event = this.httpResponse_event.append(httpResponse_event);
	}

}
