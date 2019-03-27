package com.mir.ems.coap.client;

import java.sql.Date;
import java.util.Iterator;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.globalVar.global;
import com.mir.ems.main.Connection;

public class CoAPClient extends Thread {

	public int observeFlag = 0;
	public int summaryFlag = 0;

	// CoapClient client = new CoapClient();
	// CoapResponse resp;
	JSONObject json;

	String pathSet = "coap://" + global.coapServerIP + ":" + global.coapServerPort + "/EMAP/"
			+ global.getParentnNodeID() + "/" + global.version + "/";

	String openADRpathSet = "coap://" + global.coapServerIP + ":" + global.coapServerPort + "/OpenADR/"
			+ global.ParentnNodeID + "/" + global.openADRVersion + "/";

	Connection connection;

	public CoAPClient(Connection connection) {

		this.connection = connection;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		ConnectRegistration();

	}

	public void ConnectRegistration() {

		if (global.profile.equals("EMAP1.0b")) {
			com.mir.ems.profile.emap.v2.ConnectRegistration connectR = new com.mir.ems.profile.emap.v2.ConnectRegistration();
			connectR.setDestEMA(global.getParentnNodeID());
			connectR.setRequestID("requestID");
			connectR.setService("ConnectRegistration");
			connectR.setSrcEMA(global.CHILD_ID);
			connectR.setTime(new Date(System.currentTimeMillis()).toString());
			connectR.setVersion("1.0b");

			String uri = pathSet + "SessionSetup";
			CoapClient client = new CoapClient();
			client.setURI(uri);
			CoapResponse resp = client.put(connectR.toString(), MediaTypeRegistry.APPLICATION_JSON);

			try {
				json = new JSONObject(resp.getResponseText().toString());
				String responseDescription = json.getString("responseDescription");

				// if (responseDescription.matches("OK|Ok|ok|oK"))
				CreatePartyRegistration();

			} catch (JSONException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
				ConnectRegistration();
			}

		} else if (global.profile.equals("OpenADR2.0b")) {

			com.mir.ems.profile.openadr.recent.ConnectRegistration connectR = new com.mir.ems.profile.openadr.recent.ConnectRegistration();
			connectR.setRequestID("requestID");
			connectR.setService("oadrQueryRegistration");
			connectR.setSrcEMA(global.CHILD_ID);

			String uri = openADRpathSet + "EiRegisterParty";
			CoapClient client = new CoapClient();
			client.setURI(uri);
			CoapResponse resp = client.put(connectR.toString(), MediaTypeRegistry.APPLICATION_JSON);

			try {
				json = new JSONObject(resp.getResponseText().toString());
				String responseDescription = json.getString("responseDescription");

				// if (responseDescription.matches("OK|Ok|ok|oK"))
				CreatePartyRegistration();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ConnectRegistration();
			}

		}

	}

	public void CreatePartyRegistration() {

		if (global.profile.equals("EMAP1.0b")) {

			com.mir.ems.profile.emap.v2.CreatePartyRegistration cp = new com.mir.ems.profile.emap.v2.CreatePartyRegistration();

			cp.setDestEMA(global.getParentnNodeID());
			// global.pull
			cp.setHttpPullModel(global.pullModel);
			cp.setProfileName("EMAP1.0b");
			cp.setReportOnly(false);
			cp.setRequestID("requestID");
			cp.setService("CreatePartyRegistration");
			cp.setSrcEMA(global.CHILD_ID);
			cp.setTime(new Date(System.currentTimeMillis()).toString());
			cp.setTransportName("MQTT");
			cp.setXmlSignature(true);

			String uri = pathSet + "SessionSetup";
			CoapClient client = new CoapClient();
			client.setURI(uri);
			CoapResponse resp = client.put(cp.toString(), MediaTypeRegistry.APPLICATION_JSON);

			try {
				json = new JSONObject(resp.getResponseText().toString());
				// String responseDescription =
				// json.getString("responseDescription");
				//
				// if (responseDescription.matches("OK|Ok|ok|oK"))
				RegisterReport();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CreatePartyRegistration();
			}

		} else if (global.profile.equals("OpenADR2.0b")) {

			com.mir.ems.profile.openadr.recent.CreatePartyRegistration cp = new com.mir.ems.profile.openadr.recent.CreatePartyRegistration();

			cp.setHttpPullModel(global.pullModel);
			cp.setProfileName("OpenADR2.0b");
			cp.setReportOnly(false);
			cp.setRequestID("requestID");
			cp.setService("oadrCreatePartyRegistration");
			cp.setSrcEMA(global.CHILD_ID);
			cp.setTransportName("MQTT");
			cp.setXmlSignature(true);

			String uri = openADRpathSet + "EiRegisterParty";
			CoapClient client = new CoapClient();
			client.setURI(uri);
			CoapResponse resp = client.put(cp.toString(), MediaTypeRegistry.APPLICATION_JSON);

			try {
				json = new JSONObject(resp.getResponseText().toString());
				// String responseDescription =
				// json.getString("responseDescription");
				//
				// if (responseDescription.matches("OK|Ok|ok|oK"))
				RegisterReport();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CreatePartyRegistration();
			}

		}
	}

	public void RegisterReport() {
		if (global.profile.equals("EMAP1.0b")) {
			double generate = 0, storage = 0, power = 0;

			com.mir.ems.profile.emap.v2.PowerAttributes pa = new com.mir.ems.profile.emap.v2.PowerAttributes();
			pa.addPowerAttributesParams(200.1, 300.1, 400.1);

			com.mir.ems.profile.emap.v2.ReportDescription rd = new com.mir.ems.profile.emap.v2.ReportDescription();

			// Explicit 일 경우
			if (global.reportType.equals("Explicit")) {

				Iterator<String> it = global.emaProtocolCoAP.keySet().iterator();

				double minValue = 0, maxValue = 0, avgValue = 0;

				while (it.hasNext()) {

					String key = it.next();

					if (global.emaProtocolCoAP.get(key).getEmaID().equals(global.CHILD_ID)) {

						avgValue += global.emaProtocolCoAP.get(key).getAvgValue();
						minValue += global.emaProtocolCoAP.get(key).getMinValue();
						maxValue += global.emaProtocolCoAP.get(key).getMaxValue();
						power += global.emaProtocolCoAP.get(key).getPower();
						generate += global.emaProtocolCoAP.get(key).getGenerate();
						storage += global.emaProtocolCoAP.get(key).getStorage();
					}
				}

				this.connection.setAvgValue(avgValue);
				this.connection.setMinValue(minValue);
				this.connection.setMaxValue(maxValue);
				this.connection.setGenerate(generate);
				this.connection.setStorage(storage);
				this.connection.setCurrentPower(power);

				// (1) 가장 상위 노드 정보를 전달한다.
				rd.addReportDescriptionParams(global.CHILD_ID, global.CHILD_ID, "EMA", this.connection.getReportType(),
						"itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod", false, "itemDescription",
						pa.getPowerAttributesParams(), "state", "Controllable", this.connection.getCurrentPower(), -1,
						0, this.connection.getGenerate(), this.connection.getStorage(), this.connection.getMaxValue(),
						this.connection.getMinValue(), this.connection.getAvgValue(),
						new Date(System.currentTimeMillis()).toString(),
						new Date(System.currentTimeMillis()).toString(),
						new Date(System.currentTimeMillis()).toString(), 9);

				// (2) 하위 노드에 대한 각 정보를 전달한다.

				it = global.emaProtocolCoAP.keySet().iterator();

				while (it.hasNext()) {

					String key = it.next();

					if (global.emaProtocolCoAP.get(key).getEmaID().equals(global.CHILD_ID)) {

						avgValue = global.emaProtocolCoAP.get(key).getAvgValue();
						minValue = global.emaProtocolCoAP.get(key).getMinValue();
						maxValue = global.emaProtocolCoAP.get(key).getMaxValue();
						power = global.emaProtocolCoAP.get(key).getPower();
						generate = global.emaProtocolCoAP.get(key).getGenerate();
						storage = global.emaProtocolCoAP.get(key).getStorage();

						rd.addReportDescriptionParams(key, key, "LED", this.connection.getReportType(), "itemUnits",
								"siScaleCode", "marketContext", "minPeriod", "maxPeriod", false, "itemDescription",
								pa.getPowerAttributesParams(), "state", "Controllable",
								global.emaProtocolCoAP.get(key).getPower(),
								global.emaProtocolCoAP.get(key).getDimming(), 0,
								global.emaProtocolCoAP.get(key).getGenerate(),
								global.emaProtocolCoAP.get(key).getStorage(),
								global.emaProtocolCoAP.get(key).getMaxValue(),
								global.emaProtocolCoAP.get(key).getMinValue(),
								global.emaProtocolCoAP.get(key).getAvgValue(),
								new Date(System.currentTimeMillis()).toString(),
								new Date(System.currentTimeMillis()).toString(),
								new Date(System.currentTimeMillis()).toString(), 9);
					}
				}

			}

			// Implicit 일 경우
			else if (global.reportType.equals("Implicit")) {

				Iterator<String> it = global.emaProtocolCoAP.keySet().iterator();

				double minValue = 0, maxValue = 0, avgValue = 0;

				while (it.hasNext()) {

					String key = it.next();
					if (global.emaProtocolCoAP.get(key).getEmaID().equals(global.CHILD_ID)) {

						avgValue += global.emaProtocolCoAP.get(key).getAvgValue();
						minValue += global.emaProtocolCoAP.get(key).getMinValue();
						maxValue += global.emaProtocolCoAP.get(key).getMaxValue();
						power += global.emaProtocolCoAP.get(key).getPower();
						generate += global.emaProtocolCoAP.get(key).getGenerate();
						storage += global.emaProtocolCoAP.get(key).getStorage();

					}
				}
				this.connection.setAvgValue(avgValue);
				this.connection.setMinValue(minValue);
				this.connection.setMaxValue(maxValue);
				this.connection.setGenerate(generate);
				this.connection.setStorage(storage);
				this.connection.setCurrentPower(power);

				rd.addReportDescriptionParams(global.CHILD_ID, global.CHILD_ID, "EMA", this.connection.getReportType(),
						"itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod", false, "itemDescription",
						pa.getPowerAttributesParams(), "state", "Controllable", this.connection.getCurrentPower(), -1,
						0, this.connection.getGenerate(), this.connection.getStorage(), this.connection.getMaxValue(),
						this.connection.getMinValue(), this.connection.getAvgValue(),
						new Date(System.currentTimeMillis()).toString(),
						new Date(System.currentTimeMillis()).toString(),
						new Date(System.currentTimeMillis()).toString(), 9);

			}

			com.mir.ems.profile.emap.v2.Report report = new com.mir.ems.profile.emap.v2.Report();
			report.addReportParams("duration", rd.getReportDescriptionParams(), "reportRequestID", "reportSpecifierID",
					"reportName", "createdDateTime");

			com.mir.ems.profile.emap.v2.RegisterReport rt = new com.mir.ems.profile.emap.v2.RegisterReport();
			rt.setDestEMA(global.getParentnNodeID());
			rt.setReport(report.getReportParams());
			rt.setRequestID("requestID");
			rt.setService("RegisterReport");
			rt.setSrcEMA(global.CHILD_ID);
			rt.setType(global.reportType);
			rt.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));

			String uri = pathSet + "SessionSetup";
			CoapClient client = new CoapClient();
			client.setURI(uri);
			CoapResponse resp = client.put(rt.toString(), MediaTypeRegistry.APPLICATION_JSON);

			try {
				json = new JSONObject(resp.getResponseText().toString());
				String responseDescription = json.getString("responseDescription");

				if (responseDescription.matches("OK|Ok|ok|oK"))
					Poll();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// RegisterReport();
			}

		} else if (global.profile.equals("OpenADR2.0b")) {

			double generate = 0, storage = 0, power = 0;

			com.mir.ems.profile.openadr.recent.PowerAttributes pa = new com.mir.ems.profile.openadr.recent.PowerAttributes();
			pa.addPowerAttributesParams(200.1, 300.1, 400.1);

			com.mir.ems.profile.openadr.recent.ReportDescription rd = new com.mir.ems.profile.openadr.recent.ReportDescription();

			// Explicit 일 경우
			if (global.reportType.equals("Explicit")) {

				Iterator<String> it = global.emaProtocolCoAP.keySet().iterator();

				double minValue = 0, maxValue = 0, avgValue = 0;

				while (it.hasNext()) {

					String key = it.next();

					if (global.emaProtocolCoAP.get(key).getEmaID().equals(global.CHILD_ID)) {

						avgValue += global.emaProtocolCoAP.get(key).getAvgValue();
						minValue += global.emaProtocolCoAP.get(key).getMinValue();
						maxValue += global.emaProtocolCoAP.get(key).getMaxValue();
						power += global.emaProtocolCoAP.get(key).getPower();
						generate += global.emaProtocolCoAP.get(key).getGenerate();
						storage += global.emaProtocolCoAP.get(key).getStorage();
					}
				}
				this.connection.setAvgValue(avgValue);
				this.connection.setMinValue(minValue);
				this.connection.setMaxValue(maxValue);
				this.connection.setGenerate(generate);
				this.connection.setStorage(storage);
				this.connection.setCurrentPower(power);

				// (1) 가장 상위 노드 정보를 전달한다.
				rd.addReportDescriptionParams(global.CHILD_ID, global.CHILD_ID, global.reportType, "itemUnits",
						"siScaleCode", "marketContext", "minPeriod", "maxPeriod", false, "itemDescription",
						pa.getPowerAttributesParams());

				// (2) 하위 노드에 대한 각 정보를 전달한다.

				it = global.emaProtocolCoAP.keySet().iterator();

				while (it.hasNext()) {

					String key = it.next();

					avgValue = global.emaProtocolCoAP.get(key).getAvgValue();
					minValue = global.emaProtocolCoAP.get(key).getMinValue();
					maxValue = global.emaProtocolCoAP.get(key).getMaxValue();
					power = global.emaProtocolCoAP.get(key).getPower();
					generate = global.emaProtocolCoAP.get(key).getGenerate();
					storage = global.emaProtocolCoAP.get(key).getStorage();

					rd.addReportDescriptionParams(key, key, global.reportType, "itemUnits", "siScaleCode",
							"marketContext", "minPeriod", "maxPeriod", false, "itemDescription",
							pa.getPowerAttributesParams());

				}

			}

			// Implicit 일 경우
			else if (global.reportType.equals("Implicit")) {

				Iterator<String> it = global.emaProtocolCoAP.keySet().iterator();

				double minValue = 0, maxValue = 0, avgValue = 0;

				while (it.hasNext()) {

					String key = it.next();

					avgValue += global.emaProtocolCoAP.get(key).getAvgValue();
					minValue += global.emaProtocolCoAP.get(key).getMinValue();
					maxValue += global.emaProtocolCoAP.get(key).getMaxValue();
					power += global.emaProtocolCoAP.get(key).getPower();
					generate += global.emaProtocolCoAP.get(key).getGenerate();
					storage += global.emaProtocolCoAP.get(key).getStorage();

				}
				this.connection.setAvgValue(avgValue);
				this.connection.setMinValue(minValue);
				this.connection.setMaxValue(maxValue);
				this.connection.setGenerate(generate);
				this.connection.setStorage(storage);
				this.connection.setCurrentPower(power);

				rd.addReportDescriptionParams(global.CHILD_ID, global.CHILD_ID, global.reportType, "itemUnits",
						"siScaleCode", "marketContext", "minPeriod", "maxPeriod", false, "itemDescription",
						pa.getPowerAttributesParams());

			}

			com.mir.ems.profile.openadr.recent.Report report = new com.mir.ems.profile.openadr.recent.Report();
			report.addReportParams("duration", rd.getReportDescriptionParams(), "reportRequestID", "reportSpecifierID",
					"reportName", "createdDateTime");

			com.mir.ems.profile.openadr.recent.RegisterReport rt = new com.mir.ems.profile.openadr.recent.RegisterReport();
			rt.setSrcEMA(global.CHILD_ID);
			rt.setReport(report.getReportParams());
			rt.setRequestID("requestID");
			rt.setService("oadrRegisterReport");

			String uri = openADRpathSet + "EiReport";
			CoapClient client = new CoapClient();
			client.setURI(uri);
			CoapResponse resp = client.put(rt.toString(), MediaTypeRegistry.APPLICATION_JSON);

			try {
				json = new JSONObject(resp.getResponseText().toString());
				String responseDescription = json.getString("responseDescription");

				if (responseDescription.matches("OK|Ok|ok|oK"))
					Poll();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// RegisterReport();
			}

		}
	}

	public void Poll() {
		if (global.profile.equals("EMAP1.0b")) {
			com.mir.ems.profile.emap.v2.Poll poll = new com.mir.ems.profile.emap.v2.Poll();
			poll.setDestEMA(global.getParentnNodeID());
			poll.setService("Poll");
			poll.setSrcEMA(global.CHILD_ID);
			poll.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));

			String uri = pathSet + "SessionSetup";
			CoapClient client = new CoapClient();

			client.setURI(uri);
			CoapResponse resp = client.put(poll.toString(), MediaTypeRegistry.APPLICATION_JSON);

			try {
				json = new JSONObject(resp.getResponseText().toString());
				String responseDescription = json.getString("service");

				if (responseDescription.matches("RegisterReport"))
					RegisteredReport();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Poll();
			}

		} else if (global.profile.equals("OpenADR2.0b")) {

			com.mir.ems.profile.openadr.recent.Poll poll = new com.mir.ems.profile.openadr.recent.Poll();

			poll.setService("OadrPoll");
			poll.setVenID(global.CHILD_ID);

			String uri = openADRpathSet + "OadrPoll";
			CoapClient client = new CoapClient();

			client.setURI(uri);
			CoapResponse resp = client.put(poll.toString(), MediaTypeRegistry.APPLICATION_JSON);

			try {
				json = new JSONObject(resp.getResponseText().toString());
				String responseDescription = json.getString("service");

				if (responseDescription.matches("oadrRegisterReport"))
					RegisteredReport();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Poll();
			}
		}

	}

	public void RegisteredReport() {
		if (global.profile.equals("EMAP1.0b")) {
			com.mir.ems.profile.emap.v2.RegisteredReport rdt = new com.mir.ems.profile.emap.v2.RegisteredReport();
			rdt.setDestEMA(global.getParentnNodeID());
			rdt.setRequestID("requestID");
			rdt.setResponseCode(200);
			rdt.setResponseDescription("OK");
			rdt.setService("RegisteredReport");
			rdt.setSrcEMA(global.CHILD_ID);
			rdt.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));

			String uri = pathSet + "SessionSetup";
			CoapClient client = new CoapClient();

			client.setURI(uri);
			CoapResponse resp = client.put(rdt.toString(), MediaTypeRegistry.APPLICATION_JSON);

			try {
				json = new JSONObject(resp.getResponseText().toString());
				String responseDescription = json.getString("responseDescription");

				if (responseDescription.matches("OK|Ok|ok|oK"))
					RequestEvent();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				RegisteredReport();
			}
		} else if (global.profile.equals("OpenADR2.0b")) {

			com.mir.ems.profile.openadr.recent.RegisteredReport rdt = new com.mir.ems.profile.openadr.recent.RegisteredReport();
			rdt.setRequestID("requestID");
			rdt.setResponseCode(200);
			rdt.setResponseDescription("OK");
			rdt.setService("oadrRegisteredReport");
			rdt.setVenID(global.CHILD_ID);
			String uri = openADRpathSet + "EiReport";
			CoapClient client = new CoapClient();

			client.setURI(uri);
			CoapResponse resp = client.put(rdt.toString(), MediaTypeRegistry.APPLICATION_JSON);

			try {
				json = new JSONObject(resp.getResponseText().toString());
				String responseDescription = json.getString("responseDescription");

				if (responseDescription.matches("OK|Ok|ok|oK"))
					RequestEvent();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				RegisteredReport();
			}

		}
	}

	public void RequestEvent() {
		if (global.profile.equals("EMAP1.0b")) {
			com.mir.ems.profile.emap.v2.RequestEvent requestEvent = new com.mir.ems.profile.emap.v2.RequestEvent();
			requestEvent.setDestEMA(global.getParentnNodeID());
			requestEvent.setRequestID("requestID");
			requestEvent.setService("RequestEvent");
			requestEvent.setSrcEMA(global.CHILD_ID);
			requestEvent.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));

			String uri = pathSet + "SessionSetup";
			CoapClient client = new CoapClient();

			client.setURI(uri);
			CoapResponse resp = client.put(requestEvent.toString(), MediaTypeRegistry.APPLICATION_JSON);
			// summary();

			try {
				json = new JSONObject(resp.getResponseText().toString());
				String responseDescription = json.getString("service");

				if (responseDescription.matches("DistributeEvent")) {

					double threshold = 0;

					JSONArray jsonArr = new JSONArray(json.getString("event"));

					for (int i = 0; i < jsonArr.length(); i++) {

						JSONObject subJson = new JSONObject(jsonArr.get(i).toString());
						JSONArray subJsonArr = new JSONArray(subJson.getString("eventSignals"));

						for (int j = 0; j < subJsonArr.length(); j++) {
							JSONObject subJson2 = new JSONObject(subJsonArr.get(i).toString());
							threshold = subJson2.getDouble("threshold");
						}
					}

					System.out.println("SET THRESHOLD" + threshold);
					this.connection.setThreshold(threshold);

					if (global.summaryReport) {
						new SummaryObs(global.CHILD_ID).start();
					}
					if (global.pullModel) {
						new Poll_Periodic().start();
					} else {
						new EventObserve().start();
					}
					new UpdateReport().start();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				RequestEvent();
			}
		} else if (global.profile.equals("OpenADR2.0b")) {

			com.mir.ems.profile.openadr.recent.RequestEvent requestEvent = new com.mir.ems.profile.openadr.recent.RequestEvent();
			requestEvent.setVenID(global.CHILD_ID);
			requestEvent.setRequestID("requestID");
			requestEvent.setService("OadrRequestEvent");

			String uri = openADRpathSet + "EiEvent";
			CoapClient client = new CoapClient();

			client.setURI(uri);
			CoapResponse resp = client.put(requestEvent.toString(), MediaTypeRegistry.APPLICATION_JSON);

			try {
				json = new JSONObject(resp.getResponseText().toString());
				String responseDescription = json.getString("service");

				if (responseDescription.matches("oadrDistributeEvent")) {

					double threshold = 0;
					JSONArray jsonArr = new JSONArray(json.getString("event"));

					for (int i = 0; i < jsonArr.length(); i++) {

						JSONObject subJson = new JSONObject(jsonArr.get(i).toString());
						JSONArray subJsonArr = new JSONArray(subJson.getString("eventSignals"));

						for (int j = 0; j < subJsonArr.length(); j++) {

							JSONObject subJson2 = new JSONObject(subJsonArr.get(i).toString());
							JSONArray subJsonArr2 = new JSONArray(subJson2.getString("intervals"));

							for (int k = 0; k < subJsonArr2.length(); k++) {
								JSONObject subJson3 = new JSONObject(subJsonArr2.get(i).toString());
								threshold = subJson3.getDouble("value");
							}
						}

					}

					this.connection.setThreshold(threshold);

					new Poll_Periodic().start();
					// Poll_Periodical();
					new UpdateReport().start();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				RequestEvent();
			}

		}
	}

}
