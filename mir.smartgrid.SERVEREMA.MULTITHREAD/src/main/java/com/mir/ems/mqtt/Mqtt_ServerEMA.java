package com.mir.ems.mqtt;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.mir.ems.globalVar.global;
import com.mir.ems.main.Connection;
import com.mir.ems.profile.emap.v2.PowerAttributes;
import com.mir.ems.profile.emap.v2.ReportDescription;
import com.mir.smartgrid.simulator.profile.emap.CreatePartyRegistration;
import com.mir.smartgrid.simulator.profile.emap.DRInformation;
import com.mir.smartgrid.simulator.profile.emap.MgnInformation;
import com.mir.smartgrid.simulator.profile.emap.RegisterReport;
import com.mir.smartgrid.simulator.profile.emap.Topology;
import com.mir.smartgrid.simulator.profile.emap.UpdateReport;

public class Mqtt_ServerEMA extends Thread implements MqttCallback {

	enum Services {
		SessionSetup, Poll, Report, Opt, Event
	}

	enum SessionSetup {
		ConnectedRegistration, CreatedPartyRegistration, RegisteredReport, RegisterReport, Response, DistributeEvent, CanceledRegistration
	}

	enum Poll {
		Response, DistributeEvent
	}

	enum Report {
		UpdatedReport
	}

	enum Opt {
		CreatedOpt, CanceledOpt
	}

	// Private instance variables
	MqttClient client;
	public String brokerUrl;
	private boolean quietMode;
	public MqttConnectOptions conOpt;
	private boolean clean;
	private String password;
	private String userName;
	public int state;
	public String clientId;
	private Connection connection;

	private String msgPayload;

	public NewHandleMqttMessage newHandleMqttMessage;

	public Mqtt_ServerEMA(String brokerUrl, String clientId, boolean cleanSession, boolean quietMode, String userName,
			String password, Connection connection) throws MqttException {
		this.brokerUrl = brokerUrl;
		this.quietMode = quietMode;
		this.clean = cleanSession;
		this.password = password;
		this.userName = userName;
		this.state = 0;
		this.clientId = clientId;
		this.connection = connection;

		String tmpDir = System.getProperty("java.io.tmpdir");
		MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

		try {

			conOpt = new MqttConnectOptions();
			conOpt.setCleanSession(clean);
			if (password != null) {
				conOpt.setPassword(this.password.toCharArray());
			}
			if (userName != null) {
				conOpt.setUserName(this.userName);
			}

			client = new MqttClient(this.brokerUrl, clientId, dataStore);
			client.setCallback(this);

		} catch (MqttException e) {
			e.printStackTrace();
			log("Unable to set up client: " + e.toString());
			System.exit(1);
		}
	}

	public Mqtt_ServerEMA(String brokerUrl, String clientId, boolean cleanSession, boolean quietMode, String userName,
			String password) throws MqttException {
		this.brokerUrl = brokerUrl;
		this.quietMode = quietMode;
		this.clean = cleanSession;
		this.password = password;
		this.userName = userName;
		this.state = 0;
		this.clientId = clientId;

		String tmpDir = System.getProperty("java.io.tmpdir");
		MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

		try {

			conOpt = new MqttConnectOptions();
			conOpt.setCleanSession(clean);
			if (password != null) {
				conOpt.setPassword(this.password.toCharArray());
			}
			if (userName != null) {
				conOpt.setUserName(this.userName);
			}

			client = new MqttClient(this.brokerUrl, clientId, dataStore);
			client.setCallback(this);

		} catch (MqttException e) {
			e.printStackTrace();
			log("Unable to set up client: " + e.toString());
			System.exit(1);
		}
	}

	public void subscribe(String topicName, int qos) throws MqttException {
		client.connect(conOpt);

		client.subscribe(topicName, qos);

	}

	private void log(String message) {
		if (!quietMode) {
			System.out.println(message);
		}
	}

	public void connectionLost(Throwable cause) {

		log("Connection to " + brokerUrl + " lost! " + cause);
		System.exit(1);
	}

	public void deliveryComplete(IMqttDeliveryToken token) {

	}

	public void messageArrived(String topic, MqttMessage message)
			throws MqttException, InterruptedException, ParseException {
//
//		String time = new Timestamp(System.currentTimeMillis()).toString();
//		System.out.println("Time:\t" + time + " Topic:\t" + topic + "Message:\t" + new String(message.getPayload())
//				+ " QoS:\t" + message.getQos());

		msgPayload = new String(message.getPayload());

		String filter, idFilter;

		if (msgPayload.startsWith("{") && msgPayload.endsWith("}")) {
			try {
				JSONObject msg_json = new JSONObject(msgPayload);

				String[] topicParse = topic.split("/");

				String serviceType = null;
				if (topicParse.length > 2) {
					serviceType = topicParse[2];
				}

				filter = topicParse[0];
				idFilter = topicParse[1];

				HandleEnergyReport handleEnergyReport;

				if (topic.contains(global.SYSTEM_ID) && topic.contains("RDRrequest")) {

					String service = topic.split("/")[1];
					String emaID = msg_json.getString("SrcEMA");
					String devID = msg_json.getString("devnumber");

					DeviceControl hm = new DeviceControl(client, service, emaID, devID, msgPayload);
					hm.start();

				}

				
				else if (filter.equals(global.getSYSTEM_TYPE())) {

					/*----------------------------------------------------------------------
					 * Edited : 2018.01.24
					 * newHandleMqttMessage = new NewHandleMqttMessage(topicParse[3], msg_json);
					 * topicParse[3] => sort of Info, e.g. CreatePartyRegistration,RegisterReport,Poll and etc.
					 * msg_json => set payload message as json format
					 * ---------------------------------------------------------------------
					 */

					if (idFilter.equals(global.getSYSTEM_ID())) {
						if (serviceType.equals("SessionSetup")) {
							new com.mir.ems.mqtt.emap.SessionSetup(client, topicParse[2], topicParse[3], msg_json)
									.start();
						} else if (serviceType.equals("Opt")) {
							new com.mir.ems.mqtt.emap.Opt(client, topicParse[2], topicParse[3], msg_json).start();

						} else if (serviceType.equals("Report")) {
							new com.mir.ems.mqtt.emap.Report(client, topicParse[2], topicParse[3], msg_json).start();

						} else if (serviceType.equals("Poll")) {
							new com.mir.ems.mqtt.emap.DemandResponseEvent(client, topicParse[2], topicParse[3],
									msg_json).start();

						}

						if (topicParse[2].equals("Request")) {

							/*----------------------------------------------------------------------
							 * Edited : 2018.01.15
							 * handleEnergyReport = new HandleEnergyReport(topicParse[3], msg_json);
							 * topicParse[3] => sort of Info, e.g. Abstract, Explicit, Connect
							 * msg_json => set payload message as json format
							 * ---------------------------------------------------------------------
							 */
							handleEnergyReport = new HandleEnergyReport(client, topicParse[3], msg_json);
							handleEnergyReport.start();

						} else if (topicParse[2].equals("Periodical")) {

							handleEnergyReport = new HandleEnergyReport(client, topicParse[3], msg_json);
							handleEnergyReport.start();

						} else if (topicParse[2].equals("Response")) {

							handleEnergyReport = new HandleEnergyReport(client, topicParse[3], msg_json);
							handleEnergyReport.start();

						} else if (topicParse[2].equals("emergency")) {
							handleEnergyReport = new HandleEnergyReport(client, topicParse[3], msg_json);
							handleEnergyReport.start();
						}

					}
				}

				// Server EMA Side
				else if (topicParse[1].equals("EMAP") && topicParse[2].equals(global.SYSTEM_ID)) {

					String profileVersion = "EMAP1.0b";

					if (msg_json.getString("DestEMA").equals(global.getSYSTEM_ID())) {

						String service = msg_json.getString("service");
						if (topicParse[4].equals("SessionSetup")) {

							new com.mir.ems.mqtt.emap.SessionSetup(client, service, msg_json, profileVersion).start();

						}

						// Report
						else if (topicParse[4].equals("Report")) {
							new com.mir.ems.mqtt.emap.Report(client, service, msg_json, profileVersion).start();
						}
						// Event
						else if (topicParse[4].matches("Event|Poll")) {
							new com.mir.ems.mqtt.emap.DemandResponseEvent(client, service, msg_json, profileVersion)
									.start();
						}
						// Opt
						else if (topicParse[4].matches("Opt")) {
							new com.mir.ems.mqtt.emap.Opt(client, service, msg_json, profileVersion).start();
						}

					}

				}
				
				else if (topicParse[1].equals("OpenADR") && topicParse[2].equals(global.SYSTEM_ID)) {

					String profileVersion = "OpenADR2.0b";

					String service = msg_json.getString("service");
					service = service.replaceAll("oadr", "");

					if (service.matches("Poll")) {
						String venID_val = msg_json.getString("venID");

						if (global.venRegisterFlag.get(venID_val).intValue() == 0) {
							topicParse[4] = "SessionSetup";
						} else {
							topicParse[4] = "Poll";
						}
					}

					if (service.matches(
							"QueryRegistration|CreatePartyRegistration|CancelPartyRegistration|RegisterReport|RegisteredReport|RequestEvent")) {

						topicParse[4] = "SessionSetup";

					}

					if (service.matches("UpdateReport")) {
						topicParse[4] = "Report";

					}

					if (service.matches("CreatedEvent")) {
						topicParse[4] = "Event";

					}

					if (service.matches("CreateOpt|CancelOpt")) {
						topicParse[4] = "Opt";

					}
					// ===============================================================
					// Session Setup
					if (topicParse[4].equals("SessionSetup")) {

						if (service.matches("QueryRegistration|oadrQueryRegistration")){
							
							service = "CONNECTREGISTRATION";
						}else{
//							System.out.println(service);
						}
						new com.mir.ems.mqtt.emap.SessionSetup(client, service, msg_json, profileVersion).start();
					}

					// Report
					else if (topicParse[4].equals("Report")) {
						new com.mir.ems.mqtt.emap.Report(client, service, msg_json, profileVersion).start();
					}
					// Event
					else if (topicParse[4].matches("Event|Poll")) {
						new com.mir.ems.mqtt.emap.DemandResponseEvent(client, service, msg_json, profileVersion).start();
					}
					// Opt
					else if (topicParse[4].matches("Opt")) {
						new com.mir.ems.mqtt.emap.Opt(client, service, msg_json, profileVersion).start();
					}


				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			if (topic.contains("Event")) {
			} else {

				if (topic.contains(global.SYSTEM_ID) && topic.contains("info")) {

					String service = topic.split("/")[1];
					String emaID = msgPayload.split("/")[1];
					String devID = msgPayload.split("/")[3];

					DeviceControl hm = new DeviceControl(client, service, emaID, devID, msgPayload);
					hm.start();

				}

				if (topic.contains("DeviceConnect") || topic.contains("DeviceDisconnect")) {

					String service = topic.split("/")[1];
					String emaID = msgPayload.split("/")[1];
					String devID = msgPayload.split("/")[3];

					DeviceControl hm = new DeviceControl(client, service, emaID, devID, msgPayload);
					hm.start();

				}

			}
		}

	}

	public void sessionSetup(String procedure, String profileVersion) throws JSONException, InterruptedException {
		String setPayload = "";
		SessionSetup sessionSetup = SessionSetup.valueOf(procedure);
		JSONObject jsonParse = new JSONObject(msgPayload);
		double generate = 0, storage = 0, power = 0;

		switch (sessionSetup) {
		case ConnectedRegistration:

			if (profileVersion.equals("EMAP1.0b")) {

				com.mir.ems.profile.emap.v2.CreatePartyRegistration cp = new com.mir.ems.profile.emap.v2.CreatePartyRegistration();

				cp.setDestEMA(global.getParentnNodeID());

				cp.setHttpPullModel(this.connection.isPullModel());

				cp.setProfileName("EMAP1.0b");
				cp.setReportOnly(false);
				cp.setRequestID("requestID");
				cp.setService("CreatePartyRegistration");
				cp.setSrcEMA(this.connection.getEmaID());
				cp.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));
				cp.setTransportName("MQTT");
				cp.setXmlSignature(true);

				String topic = "/EMAP/" + global.getParentnNodeID() + "/1.0b/SessionSetup";
				setPayload = cp.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

			}

			else if (profileVersion.equals("OpenADR2.0b_new")) {

				com.mir.ems.profile.openadr.recent.CreatePartyRegistration cp = new com.mir.ems.profile.openadr.recent.CreatePartyRegistration();

				cp.setHttpPullModel(this.connection.isPullModel());

				cp.setProfileName("OpenADR2.0b");
				cp.setReportOnly(false);
				cp.setRequestID("requestID");
				cp.setService("oadrCreatePartyRegistration");
				cp.setSrcEMA(this.connection.getEmaID());
				// cp.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));
				cp.setTransportName("MQTT");
				cp.setXmlSignature(true);

				String topic = "/OpenADR/" + global.getParentnNodeID() + "/2.0b/EiRegisterParty";
				setPayload = cp.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

			}

			else {
				try {

					InetAddress local = InetAddress.getLocalHost();

					setPayload = new CreatePartyRegistration(this.connection.getEmaID(), global.getParentnNodeID(), 1,
							1, "MQTT", local.getHostAddress().toString(), 0, 0, "EMAP", 0,
							jsonParse.getString("registrationID"), "CreatePartyRegistration",
							this.connection.getCurrentTime(System.currentTimeMillis())).toString();

					new Publishing().publishThread(this.client,
							"SEMA/" + global.getParentnNodeID() + "/SessionSetup/CreatePartyRegistration", 0,
							setPayload.getBytes());

					// Store Report Type recv from Server EMA
					this.connection.setReportType(jsonParse.getString("type"));
					this.connection.setRegistrationID(jsonParse.getString("registrationID"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;

		case CreatedPartyRegistration:

			int emaCnt = 0;

			Iterator<String> keys = global.emaProtocolCoAP_Device.keySet().iterator();

			if (profileVersion.equals("EMAP1.0b")) {

				PowerAttributes pa = new PowerAttributes();
				pa.addPowerAttributesParams(200.1, 300.1, 400.1);

				ReportDescription rd = new ReportDescription();

				// Explicit 일 경우
				if (global.reportType.equals("Explicit")) {

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

					// (1) 가장 상위 노드 정보를 전달한다.
					rd.addReportDescriptionParams(this.connection.getEmaID(), this.connection.getEmaID(), "EMA",
							global.reportType, "itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod",
							false, "itemDescription", pa.getPowerAttributesParams(), "state", global.qosType,
							this.connection.getCurrentPower(), -1, 0, this.connection.getGenerate(),
							this.connection.getStorage(), this.connection.getMaxValue(), this.connection.getMinValue(),
							this.connection.getAvgValue(), new Date(System.currentTimeMillis()).toString(),
							this.connection.getMaxTime(), this.connection.getMinTime(), 9);

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

						rd.addReportDescriptionParams(key, key, "CEMA", global.reportType, "itemUnits", "siScaleCode",
								"marketContext", "minPeriod", "maxPeriod", false, "itemDescription",
								pa.getPowerAttributesParams(), "state", global.emaProtocolCoAP.get(key).getqOs(),
								global.emaProtocolCoAP.get(key).getPower(),
								global.emaProtocolCoAP.get(key).getDimming(), 0,
								global.emaProtocolCoAP.get(key).getGenerate(),
								global.emaProtocolCoAP.get(key).getStorage(),
								global.emaProtocolCoAP.get(key).getMaxValue(),
								global.emaProtocolCoAP.get(key).getMinValue(),
								global.emaProtocolCoAP.get(key).getAvgValue(),
								new Date(System.currentTimeMillis()).toString(),
								global.emaProtocolCoAP.get(key).getMaxTime(),
								global.emaProtocolCoAP.get(key).getMinTime(), 9);

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

					rd.addReportDescriptionParams(this.connection.getEmaID(), this.connection.getEmaID(), "EMA",
							global.reportType, "itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod",
							false, "itemDescription", pa.getPowerAttributesParams(), "state", global.qosType,
							this.connection.getCurrentPower(), -1, 0, this.connection.getGenerate(),
							this.connection.getStorage(), this.connection.getMaxValue(), this.connection.getMinValue(),
							this.connection.getAvgValue(), new Date(System.currentTimeMillis()).toString(),
							this.connection.getMaxTime(), this.connection.getMinTime(), 9);

				}

				com.mir.ems.profile.emap.v2.Report report = new com.mir.ems.profile.emap.v2.Report();
				report.addReportParams("duration", rd.getReportDescriptionParams(), "reportRequestID",
						"reportSpecifierID", "reportName", "createdDateTime");

				com.mir.ems.profile.emap.v2.RegisterReport rt = new com.mir.ems.profile.emap.v2.RegisterReport();
				rt.setDestEMA(global.getParentnNodeID());
				rt.setReport(report.getReportParams());
				rt.setRequestID("requestID");
				rt.setService("RegisterReport");
				rt.setSrcEMA(this.connection.getEmaID());
				rt.setType(global.reportType);
				rt.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));

				String topic = "/EMAP/" + global.getParentnNodeID() + "/1.0b/SessionSetup";
				setPayload = rt.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

			}

			else if (profileVersion.equals("OpenADR2.0b_new")) {

				com.mir.ems.profile.openadr.recent.PowerAttributes pa = new com.mir.ems.profile.openadr.recent.PowerAttributes();
				pa.addPowerAttributesParams(200.1, 300.1, 400.1);

				com.mir.ems.profile.openadr.recent.ReportDescription rd = new com.mir.ems.profile.openadr.recent.ReportDescription();

				// Explicit 일 경우
				if (global.reportType.equals("Explicit")) {

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

					// (1) 가장 상위 노드 정보를 전달한다.
					rd.addReportDescriptionParams(this.connection.getEmaID(), this.connection.getEmaID(),
							global.reportType, "itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod",
							false, "itemDescription", pa.getPowerAttributesParams());

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

					rd.addReportDescriptionParams(this.connection.getEmaID(), this.connection.getEmaID(),
							global.reportType, "itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod",
							false, "itemDescription", pa.getPowerAttributesParams());

				}

				com.mir.ems.profile.openadr.recent.Report report = new com.mir.ems.profile.openadr.recent.Report();
				report.addReportParams("duration", rd.getReportDescriptionParams(), "reportRequestID",
						"reportSpecifierID", "reportName", "createdDateTime");

				com.mir.ems.profile.openadr.recent.RegisterReport rt = new com.mir.ems.profile.openadr.recent.RegisterReport();
				rt.setSrcEMA(global.CHILD_ID);
				rt.setReport(report.getReportParams());
				rt.setRequestID("requestID");
				rt.setService("oadrRegisterReport");

				String topic = "/OpenADR/" + global.getParentnNodeID() + "/2.0b/EiReport";
				setPayload = rt.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

			}

			if (global.profile.equals("EMAP")) {
				Topology topology = new Topology();

				while (keys.hasNext()) {
					String key = keys.next();
					if (key.startsWith(this.connection.getEmaID())) {
						emaCnt++;

						if (key.contains("RECLOSER")) {

						} else if (key.contains("SOLAR")) {
							generate += global.emaProtocolCoAP_Device.get(key).getPower();
						} else if (key.contains("BATTERY")) {
							storage += global.emaProtocolCoAP_Device.get(key).getChargedEnergy();
						} else if (key.contains("RESOURCE")) {
							power += global.emaProtocolCoAP_Device.get(key).getPower();
						}

						topology.addParams(global.emaProtocolCoAP_Device.get(key).getDeviceEMAID(),
								global.emaProtocolCoAP_Device.get(key).getDeviceType(),
								global.emaProtocolCoAP_Device.get(key).getPower(),
								global.emaProtocolCoAP_Device.get(key).getState(),
								global.emaProtocolCoAP_Device.get(key).getMode(),
								global.emaProtocolCoAP_Device.get(key).getDimming(),
								global.emaProtocolCoAP_Device.get(key).getCapacity(),
								global.emaProtocolCoAP_Device.get(key).getSoc(),
								global.emaProtocolCoAP_Device.get(key).getVolt(),
								global.emaProtocolCoAP_Device.get(key).getHz(),
								global.emaProtocolCoAP_Device.get(key).getChargedEnergy(),
								global.emaProtocolCoAP_Device.get(key).getPriority());
					}

				}

				// 이 부분은 XML 파싱 하는 부분에 들어가야함
				this.connection.setMinValue(power);
				this.connection.setMaxValue(power);
				this.connection.setCurrentPower(power);
				this.connection.setAvgValue(power);
				/////////////////////////////////

				if (this.connection.getReportType().equals("Explicit")) {

					MgnInformation mgnInformation = new MgnInformation(emaCnt, topology.getParams(), null, 0, power, 0,
							generate, storage, this.connection.getMaxValue(), this.connection.getMinValue(),
							this.connection.getAvgValue(), this.connection.getMinTime(), this.connection.getMinTime());
					DRInformation drInformation = new DRInformation("microgrid", "mir", "2018-07-23", 0, 0, 0,
							"RegisterReport", 0, 0, 0, 0, 0, 0, "ReigsterReport", "Korea", 0, 0, 0);

					setPayload = new RegisterReport(this.connection.getEmaID(), global.getParentnNodeID(), 1,
							"Registration", this.connection.getReportType(), drInformation, mgnInformation,
							"RegisterReport", this.connection.getCurrentTime(System.currentTimeMillis())).toString();

				} else if (this.connection.getReportType().equals("Implicit")) {

					MgnInformation mgnInformation = new MgnInformation(emaCnt, null, 0, power, 0, generate, storage,
							this.connection.getMaxValue(), this.connection.getMinValue(), this.connection.getAvgValue(),
							this.connection.getMinTime(), this.connection.getMinTime());
					DRInformation drInformation = new DRInformation("microgrid", "mir", "2018-07-23", 0, 0, 0,
							"RegisterReport", 0, 0, 0, 0, 0, 0, "ReigsterReport", "Korea", 0, 0, 0);

					setPayload = new RegisterReport(this.connection.getEmaID(), global.getParentnNodeID(), 1,
							"Registration", this.connection.getReportType(), drInformation, mgnInformation,
							"RegisterReport", this.connection.getCurrentTime(System.currentTimeMillis())).toString();

				}
				new Publishing().publishThread(this.client,
						"SEMA/" + global.getParentnNodeID() + "/SessionSetup/RegisterReport", 0, setPayload.getBytes());
			}

			break;
		case RegisteredReport:

			if (profileVersion.equals("EMAP1.0b")) {

				com.mir.ems.profile.emap.v2.Poll poll = new com.mir.ems.profile.emap.v2.Poll();
				poll.setDestEMA(global.getParentnNodeID());
				poll.setService("Poll");
				poll.setSrcEMA(this.connection.getEmaID());
				poll.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));

				String topic = "/EMAP/" + global.getParentnNodeID() + "/1.0b/SessionSetup";
				setPayload = poll.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

			}

			else if (profileVersion.equals("OpenADR2.0b_new")) {

				com.mir.ems.profile.openadr.recent.Poll poll = new com.mir.ems.profile.openadr.recent.Poll();
				poll.setService("oadrPoll");
				poll.setVenID(this.connection.getEmaID());

				String topic = "/OpenADR/" + global.getParentnNodeID() + "/2.0b/OadrPoll";
				setPayload = poll.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

//				global.venRegisterFlag = 1;

			}


			break;
		case RegisterReport:

			if (profileVersion.equals("EMAP1.0b")) {

				com.mir.ems.profile.emap.v2.RegisteredReport rdt = new com.mir.ems.profile.emap.v2.RegisteredReport();
				rdt.setDestEMA(global.getParentnNodeID());
				rdt.setRequestID("requestID");
				rdt.setResponseCode(200);
				rdt.setResponseDescription("OK");
				rdt.setService("RegisteredReport");
				rdt.setSrcEMA(this.connection.getEmaID());
				rdt.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));

				String topic = "/EMAP/" + global.getParentnNodeID() + "/1.0b/SessionSetup";
				setPayload = rdt.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

			}

			else if (profileVersion.equals("OpenADR2.0b_new")) {

				com.mir.ems.profile.openadr.recent.RegisteredReport rdt = new com.mir.ems.profile.openadr.recent.RegisteredReport();
				rdt.setRequestID("requestID");
				rdt.setResponseCode(200);
				rdt.setResponseDescription("OK");
				rdt.setService("oadrRegisteredReport");
				rdt.setVenID(global.CHILD_ID);

				String topic = "/OpenADR/" + global.getParentnNodeID() + "/2.0b/EiReport";
				setPayload = rdt.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

			}

			break;
		case Response:

			if (profileVersion.equals("EMAP1.0b")) {

				com.mir.ems.profile.emap.v2.RequestEvent requestEvent = new com.mir.ems.profile.emap.v2.RequestEvent();
				requestEvent.setDestEMA(global.getParentnNodeID());
				requestEvent.setRequestID("requestID");
				requestEvent.setService("RequestEvent");
				requestEvent.setSrcEMA(this.connection.getEmaID());
				requestEvent.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));

				String topic = "/EMAP/" + global.getParentnNodeID() + "/1.0b/SessionSetup";
				setPayload = requestEvent.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

			}

			else if (profileVersion.equals("OpenADR2.0b_new")) {

				com.mir.ems.profile.openadr.recent.RequestEvent requestEvent = new com.mir.ems.profile.openadr.recent.RequestEvent();
				requestEvent.setVenID(global.CHILD_ID);
				requestEvent.setRequestID("requestID");
				requestEvent.setService("oadrRequestEvent");

				String topic = "/OpenADR/" + global.getParentnNodeID() + "/2.0b/EiEvent";
				setPayload = requestEvent.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());
				global.venRegisterSeqNumber = 1;

			}

			break;
		case DistributeEvent:

			// Wait for 1sec
			Thread.sleep(1000);

			if (profileVersion.equals("EMAP1.0b")) {

				String topic = "/EMAP/" + global.getParentnNodeID() + "/1.0b/Poll";

				if (this.connection.isPullModel()) {
					com.mir.ems.profile.emap.v2.Poll poll = new com.mir.ems.profile.emap.v2.Poll();
					poll.setDestEMA(global.getParentnNodeID());
					poll.setService("Poll");
					poll.setSrcEMA(this.connection.getEmaID());
					poll.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));

					setPayload = poll.toString();
					new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

				}

				PowerAttributes pa = new PowerAttributes();
				pa.addPowerAttributesParams(200.1, 300.1, 400.1);

				ReportDescription rd = new ReportDescription();

				// Explicit 일 경우
				if (global.reportType.equals("Explicit")) {

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

					// (1) 가장 상위 노드 정보를 전달한다.
					rd.addReportDescriptionParams(this.connection.getEmaID(), this.connection.getEmaID(), "EMA",
							global.reportType, "itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod",
							false, "itemDescription", pa.getPowerAttributesParams(), "state", global.qosType,
							this.connection.getCurrentPower(), -1, 0, this.connection.getGenerate(),
							this.connection.getStorage(), this.connection.getMaxValue(), this.connection.getMinValue(),
							this.connection.getAvgValue(), new Date(System.currentTimeMillis()).toString(),
							this.connection.getMaxTime(), this.connection.getMinTime(), 9);

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

						rd.addReportDescriptionParams(key, key, "CEMA", global.reportType, "itemUnits", "siScaleCode",
								"marketContext", "minPeriod", "maxPeriod", false, "itemDescription",
								pa.getPowerAttributesParams(), "state", global.emaProtocolCoAP.get(key).getqOs(),
								global.emaProtocolCoAP.get(key).getPower(),
								global.emaProtocolCoAP.get(key).getDimming(), 0,
								global.emaProtocolCoAP.get(key).getGenerate(),
								global.emaProtocolCoAP.get(key).getStorage(),
								global.emaProtocolCoAP.get(key).getMaxValue(),
								global.emaProtocolCoAP.get(key).getMinValue(),
								global.emaProtocolCoAP.get(key).getAvgValue(),
								new Date(System.currentTimeMillis()).toString(),
								global.emaProtocolCoAP.get(key).getMaxTime(),
								global.emaProtocolCoAP.get(key).getMinTime(), 9);

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

					rd.addReportDescriptionParams(this.connection.getEmaID(), this.connection.getEmaID(), "EMA",
							global.reportType, "itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod",
							false, "itemDescription", pa.getPowerAttributesParams(), "state", global.qosType,
							this.connection.getCurrentPower(), -1, 0, this.connection.getGenerate(),
							this.connection.getStorage(), this.connection.getMaxValue(), this.connection.getMinValue(),
							this.connection.getAvgValue(), new Date(System.currentTimeMillis()).toString(),
							this.connection.getMaxTime(), this.connection.getMinTime(), 9);

				}

				com.mir.ems.profile.emap.v2.Report report = new com.mir.ems.profile.emap.v2.Report();
				report.addReportParams("duration", rd.getReportDescriptionParams(), "reportRequestID",
						"reportSpecifierID", "reportName", "createdDateTime");

				com.mir.ems.profile.emap.v2.RegisterReport rt = new com.mir.ems.profile.emap.v2.RegisterReport();
				rt.setDestEMA(global.getParentnNodeID());
				rt.setReport(report.getReportParams());
				rt.setRequestID("requestID");
				rt.setService("UpdateReport");
				rt.setSrcEMA(this.connection.getEmaID());
				rt.setType(global.reportType);
				rt.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));

				topic = "/EMAP/" + global.getParentnNodeID() + "/1.0b/Report";
				setPayload = rt.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

			}

			else if (profileVersion.equals("OpenADR2.0b_new")) {

				String topic = "";
				if (this.connection.isPullModel()) {

					com.mir.ems.profile.openadr.recent.Poll poll = new com.mir.ems.profile.openadr.recent.Poll();
					poll.setService("oadrPoll");
					poll.setVenID(this.connection.getEmaID());

					topic = "/OpenADR/" + global.getParentnNodeID() + "/2.0b/OadrPoll";
					setPayload = poll.toString();
					new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());
				}
				com.mir.ems.profile.openadr.recent.PowerAttributes pa = new com.mir.ems.profile.openadr.recent.PowerAttributes();
				pa.addPowerAttributesParams(200.1, 300.1, 400.1);

				com.mir.ems.profile.openadr.recent.ReportDescription rd = new com.mir.ems.profile.openadr.recent.ReportDescription();

				// Explicit 일 경우
				if (global.reportType.equals("Explicit")) {

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

					// (1) 가장 상위 노드 정보를 전달한다.
					rd.addReportDescriptionParams(this.connection.getEmaID(), this.connection.getEmaID(),
							global.reportType, "itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod",
							false, "itemDescription", pa.getPowerAttributesParams());

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

					rd.addReportDescriptionParams(this.connection.getEmaID(), this.connection.getEmaID(),
							global.reportType, "itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod",
							false, "itemDescription", pa.getPowerAttributesParams());

				}

				com.mir.ems.profile.openadr.recent.Report report = new com.mir.ems.profile.openadr.recent.Report();
				report.addReportParams("duration", rd.getReportDescriptionParams(), "reportRequestID",
						"reportSpecifierID", "reportName", "createdDateTime");

				com.mir.ems.profile.openadr.recent.RegisterReport rt = new com.mir.ems.profile.openadr.recent.RegisterReport();
				rt.setSrcEMA(global.CHILD_ID);
				rt.setReport(report.getReportParams());
				rt.setRequestID("requestID");
				rt.setService("oadrUpdateReport");

				topic = "/OpenADR/" + global.getParentnNodeID() + "/2.0b/EiReport";
				setPayload = rt.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

			}

			if (global.profile.equals("EMAP")) {

				setPayload = new com.mir.smartgrid.simulator.profile.emap.Poll(this.connection.getEmaID(),
						global.getParentnNodeID(), 1, 1, "Periodical", "Poll",
						this.connection.getCurrentTime(System.currentTimeMillis())).toString();

				String setUpdatePayload = null;

				keys = global.emaProtocolCoAP_Device.keySet().iterator();
				Topology topology = new Topology();

				emaCnt = 0;
				generate = 0;
				storage = 0;
				power = 0;
				while (keys.hasNext()) {
					String key = keys.next();
					if (key.startsWith(this.connection.getEmaID())) {
						emaCnt++;

						if (key.contains("RECLOSER")) {
							// 채워서 직접 시뮬레이터를 건드려야 하는것이 맞지만 !, 그러면 시뮬레이터가 여러대가되야됨
							// 여기서 Recloser역할함
						} else if (key.contains("SOLAR")) {
							generate += global.emaProtocolCoAP_Device.get(key).getPower();
						} else if (key.contains("BATTERY")) {
							storage += global.emaProtocolCoAP_Device.get(key).getChargedEnergy();
						} else if (key.contains("RESOURCE")) {
							power += global.emaProtocolCoAP_Device.get(key).getPower();
						}

						topology.addParams(global.emaProtocolCoAP_Device.get(key).getDeviceEMAID(),
								global.emaProtocolCoAP_Device.get(key).getDeviceType(),
								global.emaProtocolCoAP_Device.get(key).getPower(),
								global.emaProtocolCoAP_Device.get(key).getState(),
								global.emaProtocolCoAP_Device.get(key).getMode(),
								global.emaProtocolCoAP_Device.get(key).getDimming(),
								global.emaProtocolCoAP_Device.get(key).getCapacity(),
								global.emaProtocolCoAP_Device.get(key).getSoc(),
								global.emaProtocolCoAP_Device.get(key).getVolt(),
								global.emaProtocolCoAP_Device.get(key).getHz(),
								global.emaProtocolCoAP_Device.get(key).getChargedEnergy(),
								global.emaProtocolCoAP_Device.get(key).getPriority());

					}

				}

				if (this.connection.getReportType().equals("Explicit")) {

					MgnInformation mgnInformation = new MgnInformation(emaCnt, topology.getParams(), null, 0, power, 0,
							generate, storage, this.connection.getMaxValue(), this.connection.getMinValue(),
							this.connection.getAvgValue(), this.connection.getMinTime(), this.connection.getMinTime());
					DRInformation drInformation = new DRInformation("microgrid", "mir", "2018-07-23", 0, 0, 0,
							"UpdateReport", 0, 0, 0, 0, 0, 0, "UpdateReport", "Korea", 0, 0, 0);

					setUpdatePayload = new UpdateReport(this.connection.getEmaID(), global.getParentnNodeID(), 1,
							"Periodic", this.connection.getReportType(), drInformation, mgnInformation,
							"PeriodicReport", this.connection.getCurrentTime(System.currentTimeMillis())).toString();

				} else if (this.connection.getReportType().equals("Implicit")) {

					MgnInformation mgnInformation = new MgnInformation(emaCnt, null, 0, power, 0, generate, storage,
							this.connection.getMaxValue(), this.connection.getMinValue(), this.connection.getAvgValue(),
							this.connection.getMinTime(), this.connection.getMinTime());
					DRInformation drInformation = new DRInformation("microgrid", "mir", "2018-07-23", 0, 0, 0,
							"UpdateReport", 0, 0, 0, 0, 0, 0, "UpdateReport", "Korea", 0, 0, 0);

					setUpdatePayload = new UpdateReport(this.connection.getEmaID(), global.getParentnNodeID(), 1,
							"Periodic", this.connection.getReportType(), drInformation, mgnInformation,
							"PeriodicReport", this.connection.getCurrentTime(System.currentTimeMillis())).toString();

				}

				new Publishing().publishThread(this.client, "SEMA/" + global.getParentnNodeID() + "/Poll/Poll", 0,
						setPayload.getBytes());

				new Publishing().publishThread(this.client,
						"SEMA/" + global.getParentnNodeID() + "/Report/UpdateReport", 0, setUpdatePayload.getBytes());
			}
			break;
		case CanceledRegistration:
			// break;
		}
	}

	public void poll(String procedure, String profileVersion) throws JSONException, InterruptedException {

		JSONObject jsonParse = new JSONObject(msgPayload);
		String setPayload = null;
		Poll poll = Poll.valueOf(procedure);
		switch (poll) {
		case Response:

			if (profileVersion.equals("EMAP1.0b")) {

				if (this.connection.isPullModel()) {

					Thread.sleep(global.interval);

					com.mir.ems.profile.emap.v2.Poll pollMsg = new com.mir.ems.profile.emap.v2.Poll();
					pollMsg.setDestEMA(global.getParentnNodeID());
					pollMsg.setService("Poll");
					pollMsg.setSrcEMA(this.connection.getEmaID());
					pollMsg.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));

					String topic = "/EMAP/" + global.getParentnNodeID() + "/1.0b/Poll";

					setPayload = pollMsg.toString();
					new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());
				}
			}

			if (profileVersion.equals("OpenADR2.0b_new")) {

				if (this.connection.isPullModel()) {

					Thread.sleep(global.interval);

					com.mir.ems.profile.openadr.recent.Poll pollMsg = new com.mir.ems.profile.openadr.recent.Poll();
					pollMsg.setService("oadrPoll");
					pollMsg.setVenID(this.connection.getEmaID());

					String topic = "/OpenADR/" + global.getParentnNodeID() + "/2.0b/OadrPoll";
					setPayload = pollMsg.toString();
					new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

				}
			}

			break;
		case DistributeEvent:

			if (profileVersion.equals("EMAP1.0b")) {

				double threshold = 0;

				JSONArray jsonArr = new JSONArray(jsonParse.getString("event"));

				for (int i = 0; i < jsonArr.length(); i++) {

					JSONObject subJson = new JSONObject(jsonArr.get(i).toString());

					JSONArray subJsonArr = new JSONArray(subJson.getString("eventSignals"));

					for (int j = 0; j < subJsonArr.length(); j++) {

						JSONObject subJson2 = new JSONObject(subJsonArr.get(i).toString());

						threshold = subJson2.getDouble("threshold");
					}

				}

				
				System.out.println("EMS에게 이벤트를 받음");
				System.out.println("Threshold"+ threshold);
				this.connection.setThreshold(threshold);
				doEmergencyControl(threshold);
				
				
				if(!this.connection.isPullModel()){
					com.mir.ems.profile.emap.v2.Response res = new com.mir.ems.profile.emap.v2.Response();
					
					res.setDestEMA(global.getParentnNodeID());
					res.setRequestID("requestID");
					res.setResponseCode(200);
					res.setResponseDescription("OK");
					res.setService("Response");
					res.setSrcEMA(this.connection.getEmaID());
					res.setTime(new Date(System.currentTimeMillis()).toString());
					
					String topic = "/EMAP/" + global.getParentnNodeID() + "/1.0b/Event";
					setPayload = res.toString();
					new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());
					
				}
				
				com.mir.ems.profile.emap.v2.CreatedEvent cde = new com.mir.ems.profile.emap.v2.CreatedEvent();

				cde.setDestEMA(global.getParentnNodeID());
				cde.setEventID("eventID");
				cde.setModificationNumber(1);
				cde.setOptType("optIn");
				cde.setRequestID("requestID");
				cde.setResponseCode(200);
				cde.setResponseDescription("OK");
				cde.setService("CreatedEvent");
				cde.setSrcEMA(this.connection.getEmaID());
				cde.setTime(new Date(System.currentTimeMillis()).toString());

				String topic = "/EMAP/" + global.getParentnNodeID() + "/1.0b/Event";
				setPayload = cde.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());
			}

			else if (profileVersion.equals("OpenADR2.0b_new")) {

				double threshold = 0;

				JSONArray jsonArr = new JSONArray(jsonParse.getString("event"));

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
				System.out.println("EMS에게 이벤트를 받음");
				System.out.println("Threshold"+ threshold);
				doEmergencyControl(threshold);
				
				if(!this.connection.isPullModel()){
					com.mir.ems.profile.openadr.recent.Response res = new com.mir.ems.profile.openadr.recent.Response();
					
					res.setDestEMA(this.connection.getEmaID());
					res.setRequestID("requestID");
					res.setResponseCode(200);
					res.setResponseDescription("OK");
					res.setService("oadrResponse");
					
					String topic = "/OpenADR/" + global.getParentnNodeID() + "/2.0b/EiEvent";
					setPayload = res.toString();
					new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());
				}
				
				com.mir.ems.profile.openadr.recent.CreatedEvent cde = new com.mir.ems.profile.openadr.recent.CreatedEvent();

				cde.setVtnID(global.getParentnNodeID());
				cde.setEventID("eventID");
				cde.setModificationNumber(1);
				cde.setOptType("optIn");
				cde.setRequestID("requestID");
				cde.setResponseCode(200);
				cde.setResponseDescription("OK");
				cde.setService("oadrCreatedEvent");
				cde.setVenID(this.connection.getEmaID());

				String topic = "/OpenADR/" + global.getParentnNodeID() + "/2.0b/EiEvent";
				setPayload = cde.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());
			}

			break;
		}

	}

	public void report(String procedure, String profileVersion) throws InterruptedException, JSONException {

		JSONObject jsonParse = new JSONObject(msgPayload);
		String setPayload = null;
		Report report = Report.valueOf(procedure);
		double generate = 0, storage = 0, power = 0;

		switch (report) {
		case UpdatedReport:
			if (profileVersion.equals("EMAP1.0b")) {

				Thread.sleep(global.interval);

				global.reportType = jsonParse.getString("type");

				PowerAttributes pa = new PowerAttributes();
				pa.addPowerAttributesParams(200.1, 300.1, 400.1);

				ReportDescription rd = new ReportDescription();

				// Explicit 일 경우
				if (global.reportType.equals("Explicit")) {

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

					// (1) 가장 상위 노드 정보를 전달한다.
					rd.addReportDescriptionParams(this.connection.getEmaID(), this.connection.getEmaID(), "EMA",
							global.reportType, "itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod",
							false, "itemDescription", pa.getPowerAttributesParams(), "state", global.qosType,
							this.connection.getCurrentPower(), -1, 0, this.connection.getGenerate(),
							this.connection.getStorage(), this.connection.getMaxValue(), this.connection.getMinValue(),
							this.connection.getAvgValue(), new Date(System.currentTimeMillis()).toString(),
							this.connection.getMaxTime(), this.connection.getMinTime(), 9);

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

						rd.addReportDescriptionParams(key, key, "CEMA", global.reportType, "itemUnits", "siScaleCode",
								"marketContext", "minPeriod", "maxPeriod", false, "itemDescription",
								pa.getPowerAttributesParams(), "state", global.emaProtocolCoAP.get(key).getqOs(),
								global.emaProtocolCoAP.get(key).getPower(),
								global.emaProtocolCoAP.get(key).getDimming(), 0,
								global.emaProtocolCoAP.get(key).getGenerate(),
								global.emaProtocolCoAP.get(key).getStorage(),
								global.emaProtocolCoAP.get(key).getMaxValue(),
								global.emaProtocolCoAP.get(key).getMinValue(),
								global.emaProtocolCoAP.get(key).getAvgValue(),
								new Date(System.currentTimeMillis()).toString(),
								global.emaProtocolCoAP.get(key).getMaxTime(),
								global.emaProtocolCoAP.get(key).getMinTime(), 9);

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

					rd.addReportDescriptionParams(this.connection.getEmaID(), this.connection.getEmaID(), "EMA",
							global.reportType, "itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod",
							false, "itemDescription", pa.getPowerAttributesParams(), "state", global.qosType,
							this.connection.getCurrentPower(), -1, 0, this.connection.getGenerate(),
							this.connection.getStorage(), this.connection.getMaxValue(), this.connection.getMinValue(),
							this.connection.getAvgValue(), new Date(System.currentTimeMillis()).toString(),
							this.connection.getMaxTime(), this.connection.getMinTime(), 9);

				}

				com.mir.ems.profile.emap.v2.Report report1 = new com.mir.ems.profile.emap.v2.Report();
				report1.addReportParams("duration", rd.getReportDescriptionParams(), "reportRequestID",
						"reportSpecifierID", "reportName", "createdDateTime");

				com.mir.ems.profile.emap.v2.RegisterReport rt = new com.mir.ems.profile.emap.v2.RegisterReport();
				rt.setDestEMA(global.getParentnNodeID());
				rt.setReport(report1.getReportParams());
				rt.setRequestID("requestID");
				rt.setService("UpdateReport");
				rt.setSrcEMA(this.connection.getEmaID());
				rt.setType(global.reportType);
				rt.setTime(this.connection.getCurrentTime(System.currentTimeMillis()));

				String topic = "/EMAP/" + global.getParentnNodeID() + "/1.0b/Report";
				setPayload = rt.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

			}

			else if (profileVersion.equals("OpenADR2.0b_new")) {

				Thread.sleep(global.interval);

				
				com.mir.ems.profile.openadr.recent.PowerAttributes pa = new com.mir.ems.profile.openadr.recent.PowerAttributes();
				pa.addPowerAttributesParams(200.1, 300.1, 400.1);

				com.mir.ems.profile.openadr.recent.ReportDescription rd = new com.mir.ems.profile.openadr.recent.ReportDescription();

				// Explicit 일 경우
				if (global.reportType.equals("Explicit")) {

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

					// (1) 가장 상위 노드 정보를 전달한다.
					rd.addReportDescriptionParams(this.connection.getEmaID(), this.connection.getEmaID(),
							global.reportType, "itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod",
							false, "itemDescription", pa.getPowerAttributesParams());

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

					rd.addReportDescriptionParams(this.connection.getEmaID(), this.connection.getEmaID(),
							global.reportType, "itemUnits", "siScaleCode", "marketContext", "minPeriod", "maxPeriod",
							false, "itemDescription", pa.getPowerAttributesParams());

				}

				com.mir.ems.profile.openadr.recent.Report report_b = new com.mir.ems.profile.openadr.recent.Report();
				report_b.addReportParams("duration", rd.getReportDescriptionParams(), "reportRequestID",
						"reportSpecifierID", "reportName", "createdDateTime");

				com.mir.ems.profile.openadr.recent.RegisterReport rt = new com.mir.ems.profile.openadr.recent.RegisterReport();
				rt.setSrcEMA(global.CHILD_ID);
				rt.setReport(report_b.getReportParams());
				rt.setRequestID("requestID");
				rt.setService("oadrUpdateReport");

				String topic = "/OpenADR/" + global.getParentnNodeID() + "/2.0b/EiReport";
				setPayload = rt.toString();
				new Publishing().publishThread(this.client, topic, 0, setPayload.getBytes());

			}
			
			break;
		}
	}

	public void opt(String procedure, String profileVersion) {
		Opt opt = Opt.valueOf(procedure);
		switch (opt) {
		case CreatedOpt:
			break;

		case CanceledOpt:
			break;
		}
	}

	public MqttClient getMqttClient() {
		return client;
	}
	

	public void doEmergencyControl(double recvThreshold) {

		Iterator<String> keys = global.emaProtocolCoAP.keySet().iterator();

		while (keys.hasNext()) {
			String key = keys.next();

			double threshold = recvThreshold / global.EXPERIMENTNUM;

			Calendar now = Calendar.getInstance();

			int sYear = now.get(Calendar.YEAR);
			int sMonth = now.get(Calendar.MONTH) + 1;
			int sDate = now.get(Calendar.DATE);
			String strYMD = sYear + "" + sMonth + "" + sDate;
			int sHour = now.get(Calendar.HOUR_OF_DAY);
			int sMin = now.get(Calendar.MINUTE);

			String sTime = sHour + "" + sMin + "" + "11";
			String eTime = (sHour + 1) + "" + sMin + "" + "11";

			// PULL MODEL
			if (global.emaProtocolCoAP.get(key).isPullModel()) {
				global.emaProtocolCoAP_EventFlag.get(key).setEventFlag(true).setStartYMD(Integer.parseInt(strYMD))
						.setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(strYMD))
						.setEndTime(Integer.parseInt(eTime)).setThreshold(threshold);
			}

			// PUSH MODEL
			else if (!global.emaProtocolCoAP.get(key).isPullModel()) {

				if (global.emaProtocolCoAP.get(key).getProtocol().equals("MQTT")) {
					global.initiater.eventOccur(key, 1, Integer.parseInt(strYMD), Integer.parseInt(sTime),
							Integer.parseInt(strYMD), Integer.parseInt(eTime), threshold);
				} else {
					
					global.obs_emaProtocolCoAP_EventFlag.get(key).setEventFlag(true)
							.setStartYMD(Integer.parseInt(strYMD)).setStartTime(Integer.parseInt(sTime + "11"))
							.setEndYMD(Integer.parseInt(strYMD)).setEndTime(Integer.parseInt(eTime + "11"))
							.setThreshold(threshold);

				}
			}

		}

	}
	

}
