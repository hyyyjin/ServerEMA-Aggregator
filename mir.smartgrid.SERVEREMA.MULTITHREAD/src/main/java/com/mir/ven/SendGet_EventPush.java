package com.mir.ven;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mir.ems.globalVar.global;

public class SendGet_EventPush extends Thread {

	private StringBuilder httpResponse_event = new StringBuilder();
	HttpClientBuilder hcb = HttpClientBuilder.create();
	HttpClient client = hcb.build();
	HttpPost post;
	private StringBuilder httpResponse = new StringBuilder();
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

		HttpGet get = new HttpGet(global.eventURL+global.CHILD_ID);
		HttpClientBuilder hcb_push_event = HttpClientBuilder.create();
		HttpClient client_push_event = hcb_push_event.build();
		HttpResponse eventResponse = client_push_event.execute(get);

		BufferedReader event_rd = new BufferedReader(new InputStreamReader(eventResponse.getEntity().getContent()));

		String event_line = "";
		httpResponse_event = new StringBuilder();
		while ((event_line = event_rd.readLine()) != null) {
			setHttpResponse_event(event_line);
		}

		DocumentBuilderFactory dbFactory_event = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder_event = dbFactory_event.newDocumentBuilder();
		Document doc_event = dBuilder_event
				.parse(new InputSource(new StringReader(getHttpResponse_event().toString())));
		NodeList nodes_event = doc_event.getDocumentElement().getElementsByTagNameNS("*", "*");

		double value = 0;
		String requestID = "", modificationNumber = "";

		for (int i = 0; i < nodes_event.getLength(); i++) {
			Node node = nodes_event.item(i);
			if (node.getNodeName().contains("value"))
				value = Double.parseDouble(node.getTextContent());
			if (node.getNodeName().contains("modificationNumber"))
				modificationNumber = node.getTextContent();
			if (node.getNodeName().contains("requestID"))
				requestID = node.getTextContent();
			if (node.getNodeName().contains("eventID")) {
				eventID = node.getTextContent();
			}

		}

		currentThresholdSet(value);

		HttpPost post = new HttpPost(global.vtnURL + "EiEvent");
		post.setEntity(
				new StringEntity(new VENImpl().CreatedEvent("200", "OK", eventID, modificationNumber, requestID)));

		// POST
		HttpResponse response = client.execute(post);

		new Thread(new Runnable() {

			public void run() {
				double disVal = 100;
//				double disVal = global.currentVal / global.emaProtocolCoAP.keySet().size();

				Calendar now = Calendar.getInstance();

				int sYear = now.get(Calendar.YEAR);
				int sMonth = now.get(Calendar.MONTH) + 1;
				int sDate = now.get(Calendar.DATE);
				String strYMD = sYear + "" + sMonth + "" + sDate;
				int sHour = now.get(Calendar.HOUR_OF_DAY);
				int sMin = now.get(Calendar.MINUTE);

				String sTime = sHour + "" + sMin + "" + "11";
				String eTime = (sHour + 1) + "" + sMin + "" + "11";

				String key = "CLIENT_EMA1";

				if (global.emaProtocolCoAP.keySet().contains(key)) {
					if (global.emaProtocolCoAP.get(key).isPullModel()) {
						global.emaProtocolCoAP_EventFlag.get(key).setEventFlag(true).setStartYMD(Integer.parseInt(strYMD))
								.setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(strYMD))
								.setEndTime(Integer.parseInt(eTime)).setThreshold(disVal);
					} else if (!global.emaProtocolCoAP.get(key).isPullModel()) {

						if (global.emaProtocolCoAP.get(key).getProtocol().equals("MQTT")) {
							global.initiater.eventOccur(key, 1, Integer.parseInt(strYMD), Integer.parseInt(sTime),
									Integer.parseInt(strYMD), Integer.parseInt(eTime), disVal);
						} else {

							global.obs_emaProtocolCoAP_EventFlag.get(key).setEventFlag(true)
									.setStartYMD(Integer.parseInt(strYMD)).setStartTime(Integer.parseInt(sTime + "11"))
									.setEndYMD(Integer.parseInt(strYMD)).setEndTime(Integer.parseInt(eTime + "11"))
									.setThreshold(disVal).setEventID(eventID);;
							global.observeManager.get(key).changed();
							global.eventFromServer = true;

						}
					}
				}
			}
		}).start();
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		// 전체에게 보내는 경우 - 시작

		// double disVal = global.currentVal /
		// global.emaProtocolCoAP.keySet().size();
		//
		// Iterator<String> keys = global.emaProtocolCoAP.keySet().iterator();
		//
		// while (keys.hasNext()) {
		// String key = keys.next();
		//
		// Calendar now = Calendar.getInstance();
		//
		// int sYear = now.get(Calendar.YEAR);
		// int sMonth = now.get(Calendar.MONTH) + 1;
		// int sDate = now.get(Calendar.DATE);
		// String strYMD = sYear + "" + sMonth + "" + sDate;
		// int sHour = now.get(Calendar.HOUR_OF_DAY);
		// int sMin = now.get(Calendar.MINUTE);
		//
		// String sTime = sHour + "" + sMin + "" + "11";
		// String eTime = (sHour + 1) + "" + sMin + "" + "11";
		//
		// // PULL MODEL
		// if (global.emaProtocolCoAP.get(key).isPullModel()) {
		// global.emaProtocolCoAP_EventFlag.get(key).setEventFlag(true).setStartYMD(Integer.parseInt(strYMD))
		// .setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(strYMD))
		// .setEndTime(Integer.parseInt(eTime)).setThreshold(disVal);
		// }
		//
		// // PUSH MODEL
		// else if (!global.emaProtocolCoAP.get(key).isPullModel()) {
		//
		// if (global.emaProtocolCoAP.get(key).getProtocol().equals("MQTT")) {
		// global.initiater.eventOccur(key, 1, Integer.parseInt(strYMD),
		// Integer.parseInt(sTime),
		// Integer.parseInt(strYMD), Integer.parseInt(eTime), disVal);
		// } else {
		//
		// global.obs_emaProtocolCoAP_EventFlag.get(key).setEventFlag(true)
		// .setStartYMD(Integer.parseInt(strYMD)).setStartTime(Integer.parseInt(sTime
		// + "11"))
		// .setEndYMD(Integer.parseInt(strYMD)).setEndTime(Integer.parseInt(eTime
		// + "11"))
		// .setThreshold(disVal);
		//
		// global.aa.get(key).changed();
		//
		// }
		// }
		//
		// }
		// 전체에게 보내는 경우 - 끝

		String line = "";
		httpResponse = new StringBuilder();
		while ((line = rd.readLine()) != null) {
			setHttpResponse(line);
		}

		if (getHttpResponse_event().toString().contains("oadrDistributeEvent"))
			sendGet();

	}

	public void currentThresholdSet(double value) {

		System.out.println("GET Threshold Value" + value);

		if (value < 80)
			global.currentVal = 0;
		else if (value < 160 && value >= 80)
			global.currentVal = 80;
		else if (value < 240 && value >= 160)
			global.currentVal = 160;
		else if (value < 320 && value >= 240)
			global.currentVal = 240;
		else if (value < 400 && value >= 320)
			global.currentVal = 320;
		else if (value >= 400)
			global.currentVal = 400;

		new CurrentThresholdTimer(30).start();

	}

	public StringBuilder getHttpResponse() {
		return httpResponse;
	}

	public void setHttpResponse(String httpResponse) {
		this.httpResponse = this.httpResponse_event.append(httpResponse);
	}

	public StringBuilder getHttpResponse_event() {
		return httpResponse_event;
	}

	public void setHttpResponse_event(String httpResponse_event) {
		this.httpResponse_event = this.httpResponse_event.append(httpResponse_event);
	}

}
