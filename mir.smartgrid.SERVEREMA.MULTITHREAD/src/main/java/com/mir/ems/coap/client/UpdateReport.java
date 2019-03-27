package com.mir.ems.coap.client;

import java.sql.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.json.JSONObject;

import com.mir.ems.globalVar.global;

//import com.mir.smartgrid.simulator.coap.CoAPClient.UpdateTask;

public class UpdateReport extends Thread {

	String pathSet = "coap://" + global.coapServerIP + ":" + global.coapServerPort + "/EMAP/"
			+ global.getParentnNodeID() + "/" + global.version + "/";

	String openADRpathSet = "coap://" + global.coapServerIP + ":" + global.coapServerPort + "/OpenADR/"
			+ global.ParentnNodeID + "/" + global.openADRVersion + "/";

	JSONObject json;

	public UpdateReport() {
		// this.connection = connection;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Timer timer = new Timer();
		timer.schedule(new UpdateTask(), 0, global.reportInterval);
	}

	private class UpdateTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			if (global.profile.equals("EMAP1.0b")) {
				double generate = 0, storage = 0, power = 0;

				// com.mir.ems.profile.emap.v2
				com.mir.ems.profile.emap.v2.PowerAttributes pa = new com.mir.ems.profile.emap.v2.PowerAttributes();
				pa.addPowerAttributesParams(200.1, 300.1, 400.1);

				com.mir.ems.profile.emap.v2.ReportDescription rd = new com.mir.ems.profile.emap.v2.ReportDescription();

				// Explicit �� ���
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

					// connection.setAvgValue(avgValue);
					// connection.setMinValue(minValue);
					// connection.setMaxValue(maxValue);
					// connection.setGenerate(generate);
					// connection.setStorage(storage);
					// connection.setCurrentPower(power);

					// (1) ���� ���� ��� ������ �����Ѵ�.
					rd.addReportDescriptionParams(global.CHILD_ID, global.CHILD_ID, "EMA", global.reportType,
							"itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod", false,
							"itemDescription", pa.getPowerAttributesParams(), "state", "Controllable", power, -1, 0,
							generate, storage, maxValue, minValue, avgValue,
							new Date(System.currentTimeMillis()).toString(),
							new Date(System.currentTimeMillis()).toString(),
							new Date(System.currentTimeMillis()).toString(), 9);

					// (2) ���� ��忡 ���� �� ������ �����Ѵ�.

					it = global.emaProtocolCoAP.keySet().iterator();

//					System.out.println("여기");
//					System.out.println(global.emaProtocolCoAP.keySet());

					while (it.hasNext()) {

						String key = it.next();

						// if
						// (global.emaProtocolCoAP.get(key).getEmaID().equals(global.CHILD_ID))
						// {

						avgValue = global.emaProtocolCoAP.get(key).getAvgValue();
						minValue = global.emaProtocolCoAP.get(key).getMinValue();
						maxValue = global.emaProtocolCoAP.get(key).getMaxValue();
						power = global.emaProtocolCoAP.get(key).getPower();
						generate = global.emaProtocolCoAP.get(key).getGenerate();
						storage = global.emaProtocolCoAP.get(key).getStorage();

						rd.addReportDescriptionParams(key, key, "LED", global.reportType, "itemUnits", "siScaleCode",
								"marketContext", "minPeriod", "maxPeriod", false, "itemDescription",
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
						// }
					}

				}

				// Implicit �� ���
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
					// connection.setAvgValue(avgValue);
					// connection.setMinValue(minValue);
					// connection.setMaxValue(maxValue);
					// connection.setGenerate(generate);
					// connection.setStorage(storage);
					// connection.setCurrentPower(power);

					rd.addReportDescriptionParams(global.CHILD_ID, global.CHILD_ID, "EMA", global.reportType,
							"itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod", false,
							"itemDescription", pa.getPowerAttributesParams(), "state", "Controllable", power, -1, 0,
							generate, storage, maxValue, minValue, avgValue,
							new Date(System.currentTimeMillis()).toString(),
							new Date(System.currentTimeMillis()).toString(),
							new Date(System.currentTimeMillis()).toString(), 9);

				}

				com.mir.ems.profile.emap.v2.Report report = new com.mir.ems.profile.emap.v2.Report();
				report.addReportParams("duration", rd.getReportDescriptionParams(), "reportRequestID",
						"reportSpecifierID", "reportName", "createdDateTime");

				com.mir.ems.profile.emap.v2.RegisterReport rt = new com.mir.ems.profile.emap.v2.RegisterReport();
				rt.setDestEMA(global.getParentnNodeID());
				rt.setReport(report.getReportParams());
				rt.setRequestID("requestID");
				rt.setService("UpdateReport");
				rt.setSrcEMA(global.CHILD_ID);
				rt.setType(global.reportType);
				rt.setTime(new Date(System.currentTimeMillis()).toString());

				String uri = pathSet + "Report";
				CoapClient client = new CoapClient();

				client = new CoapClient();
				client.setURI(uri);

				client.put(new CoapHandler() {
					@Override
					public void onLoad(CoapResponse response) {
						// String content = response.getResponseText();
						// System.out.println(content);
					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}
				}, rt.toString(), MediaTypeRegistry.APPLICATION_JSON);

				// CoapResponse resp = client.put(rt.toString(),
				// MediaTypeRegistry.APPLICATION_JSON);

				// try {
				//
				// System.out.println("ù����");
				// System.out.println("�Ӿ�");
				// System.out.println(resp.getResponseText().toString());
				//
				// json = new JSONObject(resp.getResponseText().toString());
				// String responseDescription = json.getString("service");
				//
				// if (responseDescription.matches("UpdatedReport")) {
				// if (!connection.isPullModel()) {
				// Thread.sleep(global.interval);
				//// UpdateReport();
				// }
				// }
				//
				// } catch (Exception e) {
				// e.printStackTrace();
				// // UpdateReport();
				// }

				// global.profile.equals("OpenADR2.0b")
			} else if (global.profile.equals("OpenADR2.0b")) {
				double generate = 0, storage = 0, power = 0;

				// com.mir.ems.profile.openadr.recent
				com.mir.ems.profile.openadr.recent.ReportDescription rd = new com.mir.ems.profile.openadr.recent.ReportDescription();

				// Explicit �� ���
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

					// (1) ���� ���� ��� ������ �����Ѵ�.
					rd.addReportDescriptionParams(global.CHILD_ID, global.CHILD_ID, global.reportType, "itemUnits",
							"siScaleCode", "marketContext", "minPeriod", "maxPeriod", false, "itemDescription",
							new com.mir.ems.profile.openadr.recent.PowerAttributes()
									.addPowerAttributesParams(0, power, 0).getPowerAttributesParams());

					// (2) ���� ��忡 ���� �� ������ �����Ѵ�.

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
								new com.mir.ems.profile.openadr.recent.PowerAttributes()
										.addPowerAttributesParams(0, power, 0).getPowerAttributesParams());

					}

				}

				// Implicit �� ���
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

					rd.addReportDescriptionParams(global.CHILD_ID, global.CHILD_ID, global.reportType, "itemUnits",
							"siScaleCode", "marketContext", "minPeriod", "maxPeriod", false, "itemDescription",
							new com.mir.ems.profile.openadr.recent.PowerAttributes()
									.addPowerAttributesParams(0, power, 0).getPowerAttributesParams());

				}

				com.mir.ems.profile.openadr.recent.Report report = new com.mir.ems.profile.openadr.recent.Report();
				report.addReportParams("duration", rd.getReportDescriptionParams(), "reportRequestID",
						"reportSpecifierID", "reportName", "createdDateTime");

				com.mir.ems.profile.openadr.recent.RegisterReport rt = new com.mir.ems.profile.openadr.recent.RegisterReport();
				rt.setSrcEMA(global.CHILD_ID);
				rt.setReport(report.getReportParams());
				rt.setRequestID("requestID");
				rt.setService("oadrUpdateReport");

				String uri = openADRpathSet + "EiReport";
				CoapClient client = new CoapClient();

				client.setURI(uri);
				CoapResponse resp = client.put(rt.toString(), MediaTypeRegistry.APPLICATION_JSON);

				try {

					json = new JSONObject(resp.getResponseText().toString());
					String responseDescription = json.getString("service");

					if (responseDescription.matches("oadrUpdatedReport")) {

					}

				} catch (Exception e) {
					e.printStackTrace();
					// UpdateReport();
				}

			}

		}
	}

}
