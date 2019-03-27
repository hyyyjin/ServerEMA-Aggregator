package com.mir.ven;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.Callable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mir.ems.globalVar.global;

public class HTTPRequest {

	private String subPath;
	private StringBuilder httpResponse = new StringBuilder();
	private StringBuilder httpResponse_updateR = new StringBuilder();
	private StringBuilder httpResponse_event = new StringBuilder();

	public HTTPRequest() {

	}

	public HTTPRequest(String subPath) throws ParserConfigurationException, SAXException, TransformerException {

		setSubPath(subPath);

		HttpClientBuilder hcb = HttpClientBuilder.create();
		HttpClient client = hcb.build();
		HttpPost post = new HttpPost(global.vtnURL + getSubPath());

		HttpClientBuilder hcb_updateReport = HttpClientBuilder.create();
		HttpClient client_updateReport = hcb_updateReport.build();
		HttpPost post_updateReport = new HttpPost(global.vtnURL + getSubPath());

		try {

			post.setEntity(new StringEntity(new VENImpl().QueryRegistration("ba4d8f5c0a")));
			// POST
			HttpResponse response = client.execute(post);
			HttpResponse updateReport_response;
			// HttpResponse eventResponse;
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				// line = line.replace(" ", "");
				setHttpResponse(line);
			}

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(getHttpResponse().toString())));
			NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

			String requestID = "";

			while (true) {

				if (getHttpResponse().toString().contains("oadrCreatedPartyRegistration")) {

					for (int i = 0; i < nodes.getLength(); i++) {
						Node node = nodes.item(i);
						if (node.getNodeName().contains("requestID"))
							requestID = node.getTextContent();
					}

					post.setEntity(new StringEntity(new VENImpl().CreatePartyRegistration("2.0b", "simpleHttp", null,
							global.reportOnly, false, global.pullModel, requestID)));
					// POST
					response = client.execute(post);
					rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

					line = "";

					httpResponse = new StringBuilder();
					while ((line = rd.readLine()) != null) {
						// line = line.replace(" ", "");
						setHttpResponse(line);
					}

				}

				// System.out.println(getHttpResponse().toString());

				if (getHttpResponse().toString().length() > 1) {
					doc = dBuilder.parse(new InputSource(new StringReader(getHttpResponse().toString())));
					nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");
				}
				if (getHttpResponse().toString().contains("oadrCreatedPartyRegistration")) {

					post = new HttpPost(global.vtnURL + "EiReport");
					// String venID = "";
					for (int i = 0; i < nodes.getLength(); i++) {
						Node node = nodes.item(i);
						if (node.getNodeName().contains("requestID"))
							requestID = node.getTextContent();

						if (node.getNodeName().contains("venID")) {
							global.HASHED_VEN_NAME = node.getTextContent();
						}

					}

					post.setEntity(new StringEntity(
							new VENImpl().RegisterReport(new TimeFormat().getCurrentTime(), global.HASHED_VEN_NAME)));

					// POST
					response = client.execute(post);
					rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

					line = "";
					httpResponse = new StringBuilder();

					while ((line = rd.readLine()) != null) {
						// line = line.replace(" ", "");
						setHttpResponse(line);
					}

				}

				if (getHttpResponse().toString().contains("oadrRegisteredReport")) {

					post = new HttpPost(global.vtnURL + "OadrPoll");

					post.setEntity(new StringEntity(new VENImpl().Poll()));

					// POST
					response = client.execute(post);
					rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

					line = "";
					httpResponse = new StringBuilder();
					while ((line = rd.readLine()) != null) {
						// line = line.replace(" ", "");
						setHttpResponse(line);
					}

				}

				if (getHttpResponse().toString().contains("oadrRegisterReport")) {

					post = new HttpPost(global.vtnURL + "EiReport");

					for (int i = 0; i < nodes.getLength(); i++) {
						Node node = nodes.item(i);
						if (node.getNodeName().contains("requestID"))
							requestID = node.getTextContent();

					}

					post.setEntity(new StringEntity(new VENImpl().RegisteredReport(requestID, "200", "OK")));

					// POST
					response = client.execute(post);
					rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

					line = "";
					httpResponse = new StringBuilder();
					while ((line = rd.readLine()) != null) {
						// line = line.replace(" ", "");
						setHttpResponse(line);
					}

				}

				if (getHttpResponse().toString().contains("oadrResponse")) {

					if (!global.RegistrationFlag) {
						post = new HttpPost(global.vtnURL + "EiEvent");

						for (int i = 0; i < nodes.getLength(); i++) {
							Node node = nodes.item(i);
							if (node.getNodeName().contains("requestID"))
								requestID = node.getTextContent();

						}

						post.setEntity(new StringEntity(new VENImpl().RequestEvent()));

						// POST
						response = client.execute(post);
						rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

						line = "";
						httpResponse = new StringBuilder();
						while ((line = rd.readLine()) != null) {
							// line = line.replace(" ", "");
							setHttpResponse(line);
						}
					} else if (global.RegistrationFlag) {

						// System.out.println("여기는?");

						try {
							Thread.sleep(global.httpPollingInterval);

							post = new HttpPost(global.vtnURL + "OadrPoll");

							post.setEntity(new StringEntity(new VENImpl().Poll()));

							// POST
							response = client.execute(post);
							rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

							line = "";
							httpResponse = new StringBuilder();
							while ((line = rd.readLine()) != null) {
								// line = line.replace(" ", "");
								setHttpResponse(line);
							}

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SocketException socE) {
							client = hcb.build();
						}

					}

				}

				if (getHttpUPDResponse().toString().contains("oadrUpdatedReport")) {

					new UpdateReport().start();
					httpResponse_updateR = new StringBuilder();

				}

				if (getHttpResponse().toString().contains("oadrDistributeEvent")) {

//					System.out.println(getHttpResponse().toString());

					if (!global.RegistrationFlag) {
						httpResponse = new StringBuilder();

						if (global.pullModel) {
							post = new HttpPost(global.vtnURL + "OadrPoll");
							post.setEntity(new StringEntity(new VENImpl().Poll()));

							// POST
							response = client.execute(post);
							rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

							line = "";
							httpResponse = new StringBuilder();
							while ((line = rd.readLine()) != null) {
								setHttpResponse(line);
							}
						}
						post_updateReport = new HttpPost(global.vtnURL + "EiReport");
						post_updateReport.setEntity(new StringEntity(new VENImpl().UpdateReport()));

						// POST
						updateReport_response = client_updateReport.execute(post_updateReport);
						BufferedReader upd_rd = new BufferedReader(
								new InputStreamReader(updateReport_response.getEntity().getContent()));

						String upd_line = "";
						httpResponse_updateR = new StringBuilder();
						while ((upd_line = upd_rd.readLine()) != null) {
							setHttpUPDResponse(upd_line);
						}

						global.RegistrationFlag = true;

						
						//Summary Register
						if(global.summaryReport){
							new SummaryRegister().start();
						}
						
						if (!global.pullModel) {

							new SendGet_EventPush().start();

						}

					}

					else if (global.RegistrationFlag) {

						DocumentBuilderFactory dbFactory_event = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder_event = dbFactory_event.newDocumentBuilder();
						Document doc_event = dBuilder_event
								.parse(new InputSource(new StringReader(getHttpResponse().toString())));
						NodeList nodes_event = doc_event.getDocumentElement().getElementsByTagNameNS("*", "*");

						double value = 0;
						String modificationNumber = "", eventID = "";
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

						post = new HttpPost(global.vtnURL + "EiEvent");
						post.setEntity(new StringEntity(
								new VENImpl().CreatedEvent("200", "OK", eventID, modificationNumber, requestID)));

						// POST
						response = client.execute(post);
						rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

						line = "";
						httpResponse = new StringBuilder();
						while ((line = rd.readLine()) != null) {
							setHttpResponse(line);
						}

						double disVal = global.currentVal / global.emaProtocolCoAP.keySet().size();

						// CLIENT_EMA1에게만 보내는 경우
						Calendar now = Calendar.getInstance();

						int sYear = now.get(Calendar.YEAR);
						int sMonth = now.get(Calendar.MONTH) + 1;
						int sDate = now.get(Calendar.DATE);
						String strYMD = sYear + "" + sMonth + "" + sDate;
						int sHour = now.get(Calendar.HOUR_OF_DAY);
						int sMin = now.get(Calendar.MINUTE);

						String sTime = sHour + "" + sMin + "" + "11";
						String eTime = (sHour + 1) + "" + sMin + "" + "11";

						String emaID = "CLIENT_EMA1";

						if (global.emaProtocolCoAP.get(emaID).isPullModel()) {
							global.emaProtocolCoAP_EventFlag.get(emaID).setEventFlag(true)
									.setStartYMD(Integer.parseInt(strYMD)).setStartTime(Integer.parseInt(sTime))
									.setEndYMD(Integer.parseInt(strYMD)).setEndTime(Integer.parseInt(eTime))
									.setThreshold(disVal).setEventID(eventID);
							global.eventFromServer = true;
						}

						// 전체에게 보내는 경우 - 시작
						// Iterator<String> keys =
						// global.emaProtocolCoAP.keySet().iterator();
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
						// global.emaProtocolCoAP_EventFlag.get(key).setEventFlag(true)
						// .setStartYMD(Integer.parseInt(strYMD)).setStartTime(Integer.parseInt(sTime))
						// .setEndYMD(Integer.parseInt(strYMD)).setEndTime(Integer.parseInt(eTime))
						// .setThreshold(disVal).setEventID(eventID);
						// global.eventFromServer = true;
						//
						// }
						//
						// // PUSH MODEL
						// else if
						// (!global.emaProtocolCoAP.get(key).isPullModel()) {
						// global.eventFromServer = true;
						//
						// if
						// (global.emaProtocolCoAP.get(key).getProtocol().equals("MQTT"))
						// {
						// global.initiater.eventOccur(key, 1,
						// Integer.parseInt(strYMD),
						// Integer.parseInt(sTime), Integer.parseInt(strYMD),
						// Integer.parseInt(eTime),
						// disVal);
						// } else {
						//
						// global.obs_emaProtocolCoAP_EventFlag.get(key).setEventFlag(true)
						// .setStartYMD(Integer.parseInt(strYMD))
						// .setStartTime(Integer.parseInt(sTime + "11"))
						// .setEndYMD(Integer.parseInt(strYMD))
						// .setEndTime(Integer.parseInt(eTime +
						// "11")).setThreshold(disVal).setEventID(eventID);
						//
						// }
						// }
						//
						// }
						// 전체에게 보내는 경우 - 끝


					}

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public boolean RDRrequest(String optID) {
	//
	// setSubPath("EiOpt");
	// HttpClientBuilder hcb = HttpClientBuilder.create();
	// HttpClient client = hcb.build();
	// HttpPost post = new HttpPost(global.vtnURL + getSubPath());
	//
	// try {
	//
	// post.setEntity(new StringEntity(
	// new VENImpl().CreateOptSchedule("Emergency", "RdrRequest", new
	// TimeFormat().getCurrentTime(), optID)));
	// // POST
	// HttpResponse response = client.execute(post);
	//
	// // HttpResponse eventResponse;
	// BufferedReader rd = new BufferedReader(new
	// InputStreamReader(response.getEntity().getContent()));
	//
	// String line = "";
	// while ((line = rd.readLine()) != null) {
	// // line = line.replace(" ", "");
	// setHttpResponse(line);
	// }
	//
	// return true;
	//
	// } catch (Exception e) {
	//
	// return false;
	//
	// }
	//
	// }

	public void currentThresholdSet(double value) {

//		System.out.println("GET Threshold Value" + value);

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

	public StringBuilder getHttpUPDResponse() {
		return httpResponse_updateR;
	}

	public HTTPRequest setHttpUPDResponse(String httpResponse) {
		this.httpResponse_updateR = this.httpResponse_updateR.append(httpResponse);
		return this;
	}

	public StringBuilder getHttpResponse() {
		return httpResponse;
	}

	public HTTPRequest setHttpResponse(String httpResponse) {
		this.httpResponse = this.httpResponse.append(httpResponse);
		return this;
	}

	public String getSubPath() {
		return subPath;
	}

	public HTTPRequest setSubPath(String subPath) {
		this.subPath = subPath;
		return this;
	}

	public StringBuilder getHttpResponse_event() {
		return httpResponse_event;
	}

	public void setHttpResponse_event(String httpResponse_event) {
		this.httpResponse_event = this.httpResponse_event.append(httpResponse_event);
	}

}
