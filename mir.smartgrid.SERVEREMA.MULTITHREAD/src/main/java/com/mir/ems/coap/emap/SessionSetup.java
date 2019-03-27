package com.mir.ems.coap.emap;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.GUI.MainFrame;
import com.mir.ems.algorithm.Greedy;
import com.mir.ems.coap.CoAPObserverSubPath;
import com.mir.ems.database.item.EMAP_CoAP_EMA_DR;
import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.EventResponder;
import com.mir.ems.mqtt.Publishing;
import com.mir.ems.profile.emap.v2.Event;
import com.mir.ems.profile.emap.v2.EventResponse;
import com.mir.ems.profile.emap.v2.EventSignals;
import com.mir.ems.profile.emap.v2.Intervals;
import com.mir.ems.profile.emap.v2.Profile;
import com.mir.ems.profile.emap.v2.Transports;
import com.mir.update.database.cema_database;
import com.mir.update.database.device_total_database;

public class SessionSetup extends CoapResource {

	enum Type {
		CONNECTREGISTRATION, CREATEPARTYREGISTRATION, REGISTERREPORT, POLL, REGISTEREDREPORT, REQUESTEVENT, CANCELPARTYREGISTRATION, UPDATEREPORT
	}

	public SessionSetup(String name) {
		super(name);
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
	}

	@Override
	public void handlePOST(CoapExchange exchange) {
		exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
	}

	@Override
	public void handleDELETE(CoapExchange exchange) {
		exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
	}

	@Override
	public void handlePUT(CoapExchange exchange) {

		// System.out.println(exchange.getRequestText().toString());
		// System.out.println(getPath());

		if (getPath().contains(global.version)) {

			try {

				String version = "EMAP" + getPath().split("/")[3];

//				System.out.println(exchange.getRequestText().toString());

				JSONObject json = new JSONObject(exchange.getRequestText().toString());
				String service = json.getString("service");

				new SessionType(getName(), service, exchange, version).start();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (getPath().contains(global.openADRVersion)) {

			try {

				String version = "OpenADR" + getPath().split("/")[3];

				JSONObject json = new JSONObject(exchange.getRequestText().toString());
				String service = json.getString("service");
//				System.out.println(service);
				service = service.replaceAll("Oadr", "");
				service = service.replaceAll("oadr", "");

				if (service.equals("QueryRegistration")) {
					service = "CONNECTREGISTRATION";
				}

				new SessionType(getName(), service, exchange, version).start();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else {
			new SessionType(getName(), exchange).start();

		}

	}

	class SessionType extends Thread {
		CoapExchange exchange;
		String incomingType, requestText, setPayload;

		Emap_Cema_Profile emaProfile;
		Emap_Device_Profile deviceProfile;
		private JSONObject jsonObj;
		private JSONObject sub1JsonObj;
		private JSONObject sub2JsonObj;
		private String version, service;

		SessionType(String incomingType, String service, CoapExchange exchange, String version) {
			this.exchange = exchange;
			this.incomingType = incomingType;
			this.requestText = exchange.getRequestText();
			this.version = version;
			this.service = service;
		}

		SessionType(String incomingType, CoapExchange exchange) {
			this.exchange = exchange;
			this.incomingType = incomingType;
			this.requestText = exchange.getRequestText();
		}

		@Override
		public void run() {

			Type type;

			if (version.equals("EMAP1.0b")) {
				type = Type.valueOf(service.toUpperCase());

			}

			else if (version.equals("OpenADR2.0b")) {
				type = Type.valueOf(service.toUpperCase());

			}

			else {
				type = Type.valueOf(incomingType.toUpperCase());

			}

			switch (type) {
			case CONNECTREGISTRATION:
				this.setPayload = acknowledgeCONNECTREGISTRATION(requestText);
				break;
			case CREATEPARTYREGISTRATION:
				this.setPayload = acknowledgeCREATEPARTYREGISTRATION(requestText);
				break;
			case REGISTERREPORT:
				this.setPayload = acknowledgeREGISTERREPORT(requestText);
				break;
			case POLL:
				this.setPayload = acknowledgePOLL(requestText);
				break;
			case REGISTEREDREPORT:
				this.setPayload = acknowledgeREGISTEREDREPORT(requestText);
				break;
			case REQUESTEVENT:
				this.setPayload = acknowledgeREQUESTEVENT(requestText);
				break;
			case CANCELPARTYREGISTRATION:
				this.setPayload = acknowledgeCANCELPARTYREGISTRATION(requestText);
				break;

			case UPDATEREPORT:
				this.setPayload = acknowledgeUPDATEREPORT(requestText);
				break;

			default:
				this.setPayload = "TYPE WRONG";
				break;
			}

			if (this.setPayload.equals("TYPE WRONG"))
				this.exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
			else if (this.setPayload.toUpperCase().equals("NORESPONSE")) {

			} else
				this.exchange.respond(ResponseCode.CONTENT, this.setPayload, MediaTypeRegistry.APPLICATION_JSON);

		}

		public String acknowledgeCONNECTREGISTRATION(String requestText) {
			JSONObject drmsg = new JSONObject();
			String srcEMA = null;

			if (version.equals("EMAP1.0b")) {
				try {
					jsonObj = new JSONObject(requestText.toUpperCase());

					srcEMA = jsonObj.getString("SRCEMA");

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"));

					Transports tr = new Transports().addTransportNameParams("MQTT").addTransportNameParams("CoAP");

					Profile p = new Profile().addProfileParams("EMAP1.0B", tr.getTransportNameParams())
							.addProfileParams("EMAP1.0A", tr.getTransportNameParams());

					com.mir.ems.profile.emap.v2.ConnectedPartyRegistration cr = new com.mir.ems.profile.emap.v2.ConnectedPartyRegistration();

					if (global.emaRegister.keySet().contains(srcEMA)) {

						cr.setResponseCode(200);
						cr.setResponseDescription("OK");
					} else {
						cr.setResponseCode(400);
						cr.setResponseDescription("Bad Request");
					}

					cr.setDestEMA(emaProfile.getEmaID());
					cr.setDuration(global.duration);
					cr.setProfile(p.getProfileParams());
					cr.setRequestID(emaProfile.getRequestID());
					// cr.setResponseCode(200);
					// cr.setResponseDescription("OK");
					cr.setService("ConnectedRegistration");
					cr.setSrcEMA(global.SYSTEM_ID);
					cr.setTime(new Date(System.currentTimeMillis()).toString());
					cr.setVersion(version);

					String payload = cr.toString();
					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else if (version.equals("OpenADR2.0b")) {

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());

					srcEMA = jsonObj.getString("VENID");

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("VENID")).setRequestID(jsonObj.getString("REQUESTID"));

					com.mir.ems.profile.openadr.recent.Transports tr = new com.mir.ems.profile.openadr.recent.Transports()
							.addTransportNameParams("MQTT").addTransportNameParams("CoAP");

					com.mir.ems.profile.openadr.recent.Profile p = new com.mir.ems.profile.openadr.recent.Profile()
							.addProfileParams("OpenADR1.0b", tr.getTransportNameParams())
							.addProfileParams("OpenADR2.0b", tr.getTransportNameParams());

					com.mir.ems.profile.openadr.recent.ConnectedPartyRegistration cr = new com.mir.ems.profile.openadr.recent.ConnectedPartyRegistration();

					global.venRegisterFlag.put(srcEMA, 0);

					if (global.emaRegister.keySet().contains(srcEMA)) {

						cr.setResponseCode(200);
						cr.setResponseDescription("OK");
					} else {
						cr.setResponseCode(400);
						cr.setResponseDescription("Bad Request");
					}
					cr.setDestEMA(emaProfile.getEmaID());
					cr.setDuration(global.duration);
					cr.setProfile(p.getProfileParams());
					cr.setRequestID(emaProfile.getRequestID());
					cr.setService("oadrCreatedPartyRegistration");
					cr.setSrcEMA(global.SYSTEM_ID);

					String payload = cr.toString();
					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else {
				try {

					emaProfile = new Emap_Cema_Profile();

					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"))
							.setType(jsonObj.getString("TYPE")).setqOs(jsonObj.getString("QOS"))
							.setVersion(jsonObj.getString("VERSION"))
							.setCustomerPriority(jsonObj.getInt("CUSTOMERPRIORITY"));

					// Security(Hash Algorithm - MD5 Apache Commons)
					String registrationID = DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis()));

					drmsg.put("SrcEMA", global.SYSTEM_ID);
					drmsg.put("DestEMA", emaProfile.getEmaID());
					drmsg.put("responseCode", 200);
					drmsg.put("responseDescription", "OK");
					drmsg.put("requestID", emaProfile.getRequestID());
					drmsg.put("registrationID", registrationID);
					drmsg.put("transportName", "CoAP");
					drmsg.put("type", emaProfile.getType());
					drmsg.put("QoS", emaProfile.getqOs());
					drmsg.put("version", emaProfile.getVersion());
					drmsg.put("duration", 1000);
					drmsg.put("customerPriority", emaProfile.getCustomerPriority());
					drmsg.put("time", new Date(System.currentTimeMillis()));
					drmsg.put("profileName", global.profileName);
					drmsg.put("service", "ConnectedRegistration");

					String qos = jsonObj.getString("QOS");

					emaProfile = new Emap_Cema_Profile(jsonObj.getString("SRCEMA"), qos, registrationID, 0,
							jsonObj.getInt("CUSTOMERPRIORITY"), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, new JSONObject(),
							new JSONObject(), "CONNECT");

					global.putEmaProtocolCoAP(jsonObj.getString("SRCEMA"), emaProfile);

					global.emaProtocolCoAP_EventFlag.put(jsonObj.getString("SRCEMA"), new EMAP_CoAP_EMA_DR());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return drmsg.toString();
		}

		public String acknowledgeCREATEPARTYREGISTRATION(String requestText) {
			JSONObject drmsg = new JSONObject();
			String srcEMA = null;

			if (version.equals("EMAP1.0b")) {

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());

					srcEMA = jsonObj.getString("SRCEMA");

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"))
							.setVersion(jsonObj.getString("PROFILENAME"))
							.setTransportName(jsonObj.getString("TRANSPORTNAME"))
							.setPullModel(Boolean.parseBoolean(jsonObj.getString("HTTPPULLMODEL")));

					com.mir.ems.profile.emap.v2.CreatedPartyRegistration cdp = new com.mir.ems.profile.emap.v2.CreatedPartyRegistration();

					String registrationID = DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis()));

					Transports tr = new Transports().addTransportNameParams("MQTT").addTransportNameParams("CoAP");

					Profile p = new Profile().addProfileParams("EMAP1.0B", tr.getTransportNameParams())
							.addProfileParams("EMAP1.0A", tr.getTransportNameParams());

					cdp.setDestEMA(emaProfile.getEmaID());
					cdp.setDuration(global.duration);
					cdp.setProfile(p.getProfileParams());
					cdp.setRequestID(emaProfile.getRequestID());
					cdp.setResponseCode(200);
					cdp.setResponseDescription("OK");
					cdp.setService("CreatedPartyRegistration");
					cdp.setSrcEMA(global.SYSTEM_ID);
					cdp.setTime(new Date(System.currentTimeMillis()).toString());
					cdp.setVersion(version);
					cdp.setRegistrationID(registrationID);

					String payload = cdp.toString();

					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					boolean realTimetableChanged = !emaProfile.isPullModel() && global.realTimeTableHasChanged ? true
							: false;

					Emap_Cema_Profile profile = new Emap_Cema_Profile("COAP", emaProfile.getEmaID(), registrationID,
							null, null, 0, 0, 0, 0, 0, 0, 0, 0, null, null, 0, emaProfile.isPullModel(),
							global.tableHasChanged, realTimetableChanged, "CONNECT");

					global.emaProtocolCoAP.put(emaProfile.getEmaID(), profile);
					global.emaProtocolCoAP_EventFlag.put(emaProfile.getEmaID(), new EMAP_CoAP_EMA_DR());

					// add(new CoAPObserverSubPath(emaProfile.getEmaID()));

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			else if (version.equals("OpenADR2.0b")) {

				try {

					jsonObj = new JSONObject(requestText.toUpperCase());

					srcEMA = jsonObj.getString("OADRVENNAME");

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("OADRVENNAME")).setRequestID(jsonObj.getString("REQUESTID"))
							.setPullModel(Boolean.parseBoolean(jsonObj.getString("OADRHTTPPULLMODEL")));

					com.mir.ems.profile.openadr.recent.CreatedPartyRegistration cdp = new com.mir.ems.profile.openadr.recent.CreatedPartyRegistration();

					String registrationID = DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis()));

					com.mir.ems.profile.openadr.recent.Transports tr = new com.mir.ems.profile.openadr.recent.Transports()
							.addTransportNameParams("MQTT").addTransportNameParams("CoAP");

					com.mir.ems.profile.openadr.recent.Profile p = new com.mir.ems.profile.openadr.recent.Profile()
							.addProfileParams("OpenADR1.0b", tr.getTransportNameParams())
							.addProfileParams("OpenADR2.0b", tr.getTransportNameParams());

					cdp.setDestEMA(emaProfile.getEmaID());
					cdp.setDuration(global.duration);
					cdp.setProfile(p.getProfileParams());
					cdp.setRequestID(emaProfile.getRequestID());
					cdp.setResponseCode(200);
					cdp.setResponseDescription("OK");
					cdp.setService("oadrCreatedPartyRegistration");
					cdp.setSrcEMA(global.SYSTEM_ID);
					cdp.setRegistrationID(registrationID);

					String topic = "/OpenADR/" + emaProfile.getEmaID() + "/2.0b/EiRegisterParty";

					String payload = cdp.toString();

					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					boolean realTimetableChanged = !emaProfile.isPullModel() && global.realTimeTableHasChanged ? true
							: false;

					Emap_Cema_Profile profile = new Emap_Cema_Profile("COAP", emaProfile.getEmaID(), registrationID,
							null, null, 0, 0, 0, 0, 0, 0, 0, 0, null, null, 0, emaProfile.isPullModel(),
							global.tableHasChanged, realTimetableChanged, "CONNECT");

					global.emaProtocolCoAP.put(emaProfile.getEmaID(), profile);
					global.emaProtocolCoAP_EventFlag.put(emaProfile.getEmaID(), new EMAP_CoAP_EMA_DR());

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else {

				try {

					emaProfile = new Emap_Cema_Profile();
					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"))
							.setVersion(jsonObj.getString("VERSION")).setProfileName(jsonObj.getString("PROFILENAME"))
							.setRegistrationID(jsonObj.getString("REGISTRATIONID"));

					drmsg.put("SrcEMA", global.SYSTEM_ID);
					drmsg.put("DestEMA", emaProfile.getEmaID());
					drmsg.put("responseCode", 200);
					drmsg.put("responseDescription", "OK");
					drmsg.put("requestID", emaProfile.getRequestID());
					drmsg.put("registrationID", emaProfile.getRegistrationID());
					drmsg.put("transportName", "CoAP");
					drmsg.put("version", emaProfile.getVersion());
					drmsg.put("duration", 1000);
					drmsg.put("profileName", emaProfile.getProfileName());
					drmsg.put("time", new Date(System.currentTimeMillis()));
					drmsg.put("service", "CreatedPartyRegistration");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return drmsg.toString();
		}

		public String acknowledgeREGISTERREPORT(String requestText) {
			JSONObject drmsg = new JSONObject();
			String srcEMA = null;

			if (version.equals("EMAP1.0b")) {

				try {
					jsonObj = new JSONObject(requestText);

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("SrcEMA")).setRequestID(jsonObj.getString("requestID"));

					JSONArray reportArr = new JSONArray(jsonObj.getString("report"));

					for (int i = 0; i < reportArr.length(); i++) {

						JSONArray decrArr = new JSONArray(
								new JSONObject(reportArr.get(i).toString()).getString("reportDescription"));

						if (jsonObj.getString("type").equals("Implicit") || 1 == decrArr.length()) {

							// System.err.println("IMPLCIT");

							JSONObject decr = new JSONObject(decrArr.get(0).toString());

							String qos, registrationID, state, minTime, maxTime;
							double margin, minValue, maxValue, avgValue, power, generate, storage;
							int priority, dimming;
							boolean pullModel;
							qos = decr.getString("qos");
							registrationID = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getRegistrationID();
							margin = decr.getDouble("margin");
							minValue = decr.getDouble("minValue");
							maxValue = decr.getDouble("maxValue");
							avgValue = decr.getDouble("avgValue");
							minTime = decr.getString("minTime");
							maxTime = decr.getString("maxTime");
							power = decr.getDouble("power");
							generate = decr.getDouble("generate");
							storage = decr.getDouble("storage");
							state = decr.getString("state");
							priority = decr.getInt("priority");
							dimming = decr.getInt("dimming");
							pullModel = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isPullModel();

							boolean realTimetable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA"))
									.isRealTimetableChanged();
							boolean timeTable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isTableChanged();

							Emap_Cema_Profile profile = new Emap_Cema_Profile("COAP", jsonObj.getString("SrcEMA"),
									registrationID, qos, state, power, dimming, margin, generate, storage, maxValue,
									minValue, avgValue, maxTime, minTime, priority, pullModel, timeTable, realTimetable,
									"CONNECT");

							global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), profile);
							global.emaThresholdManage.put(jsonObj.getString("SrcEMA"), avgValue);

						}

						else if (jsonObj.getString("type").equals("Explicit") || 1 < decrArr.length()) {

							// System.err.println("Explicit");

							for (int j = 0; j < decrArr.length(); j++) {

								JSONObject decr = new JSONObject(decrArr.get(j).toString());

								String qos, registrationID, state, minTime, maxTime, rID;
								double margin, minValue, maxValue, avgValue, power, generate, storage;
								int priority, dimming;
								boolean pullModel;

								if (decr.getString("deviceType").equals("EMA")) {
									qos = decr.getString("qos");
									registrationID = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA"))
											.getRegistrationID();
									margin = decr.getDouble("margin");
									minValue = decr.getDouble("minValue");
									maxValue = decr.getDouble("maxValue");
									avgValue = decr.getDouble("avgValue");
									minTime = decr.getString("minTime");
									maxTime = decr.getString("maxTime");
									power = decr.getDouble("power");
									generate = decr.getDouble("generate");
									storage = decr.getDouble("storage");
									state = decr.getString("state");
									priority = decr.getInt("priority");
									dimming = decr.getInt("dimming");
									pullModel = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isPullModel();

									boolean realTimetable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA"))
											.isRealTimetableChanged();
									boolean timeTable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA"))
											.isTableChanged();

									Emap_Cema_Profile profile = new Emap_Cema_Profile("COAP",
											jsonObj.getString("SrcEMA"), registrationID, qos, state, power, dimming,
											margin, generate, storage, maxValue, minValue, avgValue, maxTime, minTime,
											priority, pullModel, timeTable, realTimetable, "CONNECT");

									// System.err.println("Session POWER" +
									// power);

									global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), profile);
									global.emaThresholdManage.put(jsonObj.getString("SrcEMA"), avgValue);

								}

								else {

									rID = decr.getString("rID");
									qos = decr.getString("qos");
									margin = decr.getDouble("margin");
									minValue = decr.getDouble("minValue");
									maxValue = decr.getDouble("maxValue");
									avgValue = decr.getDouble("avgValue");
									minTime = decr.getString("minTime");
									maxTime = decr.getString("maxTime");
									power = decr.getDouble("power");
									generate = decr.getDouble("generate");
									storage = decr.getDouble("storage");
									state = decr.getString("state");
									priority = decr.getInt("priority");
									dimming = decr.getInt("dimming");

									deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"), rID,
											decr.getString("deviceType"), qos, state, power, dimming, margin, generate,
											storage, maxValue, minValue, avgValue, maxTime, minTime, priority);

									global.emaProtocolCoAP_Device.put(rID, deviceProfile);

								}

								JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));

								for (int k = 0; k < powerAtts.length(); k++) {
									JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());
									// System.out.println(powerAtt.getString("hertz"));
								}

							}

						}

					}

					com.mir.ems.profile.emap.v2.RegisteredReport rdt = new com.mir.ems.profile.emap.v2.RegisteredReport();
					rdt.setDestEMA(emaProfile.getEmaID());
					rdt.setRequestID(emaProfile.getRequestID());
					rdt.setResponseCode(200);
					rdt.setResponseDescription("OK");
					rdt.setService("RegisteredReport");
					rdt.setSrcEMA(global.SYSTEM_ID);
					rdt.setTime(new Date(System.currentTimeMillis()).toString());

					String payload = rdt.toString();

					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (version.equals("OpenADR2.0b")) {

				try {
					jsonObj = new JSONObject(requestText);

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("venID")).setRequestID(jsonObj.getString("requestID"));

					JSONArray reportArr = new JSONArray(jsonObj.getString("oadrReport"));

					for (int i = 0; i < reportArr.length(); i++) {

						JSONArray decrArr = new JSONArray(
								new JSONObject(reportArr.get(i).toString()).getString("oadrReportDescription"));

					}

					com.mir.ems.profile.openadr.recent.RegisteredReport rdt = new com.mir.ems.profile.openadr.recent.RegisteredReport();
					rdt.setVenID(emaProfile.getEmaID());
					rdt.setRequestID(emaProfile.getRequestID());
					rdt.setResponseCode(200);
					rdt.setResponseDescription("OK");
					rdt.setService("oadrRegisteredReport");
					//

					String payload = rdt.toString();
					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {

					emaProfile = new Emap_Cema_Profile();
					jsonObj = new JSONObject(requestText.toUpperCase());
					sub2JsonObj = new JSONObject(jsonObj.getString("EMAREGISTEREDMGNINFORMATION").toUpperCase());

					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"))
							.setVersion(jsonObj.getString("VERSION"));

					Emap_Cema_Profile originVal = global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA"));

					emaProfile = new Emap_Cema_Profile(jsonObj.getString("SRCEMA"), originVal.getqOs(),
							originVal.getRegistrationID(), sub2JsonObj.getInt("EMACNT"),
							originVal.getCustomerPriority(), sub2JsonObj.getDouble("MARGIN"),
							sub2JsonObj.getDouble("MINVALUE"), sub2JsonObj.getDouble("MAXVALUE"),
							sub2JsonObj.getDouble("AVGVALUE"), sub2JsonObj.getDouble("POWER"),
							sub2JsonObj.getDouble("GENERATE"), sub2JsonObj.getDouble("STORAGE"),
							jsonObj.getJSONObject("EMAREGISTEREDDRINFORMATION"),
							jsonObj.getJSONObject("EMAREGISTEREDMGNINFORMATION"), "CONNECT");

					JSONArray topologyInfo = new JSONArray(sub2JsonObj.getString("TOPOLOGY").toUpperCase());
					for (int i = 0; i < topologyInfo.length(); i++) {

						sub1JsonObj = new JSONObject(topologyInfo.get(i).toString().toUpperCase());

						if (sub1JsonObj.getString("DEVICETYPE").equals("LED")) {
							deviceProfile = new Emap_Device_Profile(jsonObj.getString("SRCEMA"),
									sub1JsonObj.getString("DEVICEEMAID"), sub1JsonObj.getString("DEVICETYPE"),
									sub1JsonObj.getString("STATE"), sub1JsonObj.getInt("DIMMING"), 0,
									sub1JsonObj.getInt("PRIORITY"), sub1JsonObj.getDouble("POWER"), 0.0, 0.0, 0.0, 0.0,
									0.0);
						} else if (sub1JsonObj.getString("DEVICETYPE").equals("PV")) {
							deviceProfile = new Emap_Device_Profile(jsonObj.getString("SRCEMA"),
									sub1JsonObj.getString("DEVICEEMAID"), sub1JsonObj.getString("DEVICETYPE"),
									sub1JsonObj.getString("STATE"), 0, 0, sub1JsonObj.getInt("PRIORITY"),
									sub1JsonObj.getDouble("POWER"), 0.0, 0.0, 0.0, 0.0, 0.0);
						} else if (sub1JsonObj.getString("DEVICETYPE").equals("ESS")) {
							deviceProfile = new Emap_Device_Profile(jsonObj.getString("SRCEMA"),
									sub1JsonObj.getString("DEVICEEMAID"), sub1JsonObj.getString("DEVICETYPE"),
									sub1JsonObj.getString("STATE"), 0, sub1JsonObj.getInt("MODE"),
									sub1JsonObj.getInt("PRIORITY"), sub1JsonObj.getDouble("POWER"),
									sub1JsonObj.getDouble("CAPACITY"), sub1JsonObj.getDouble("VOLT"),
									sub1JsonObj.getDouble("HZ"), sub1JsonObj.getDouble("CHARGEDENERGY"),
									sub1JsonObj.getDouble("SOC"));
						} else if (sub1JsonObj.getString("DEVICETYPE").equals("RECLOSER")) {
							deviceProfile = new Emap_Device_Profile(jsonObj.getString("SRCEMA"),
									sub1JsonObj.getString("DEVICEEMAID"), sub1JsonObj.getString("DEVICETYPE"),
									sub1JsonObj.getString("STATE"), 0, 0, sub1JsonObj.getInt("PRIORITY"),
									sub1JsonObj.getDouble("POWER"), 0.0, sub1JsonObj.getDouble("VOLT"),
									sub1JsonObj.getDouble("HZ"), 0.0, 0.0);
						} else if (sub1JsonObj.getString("DEVICETYPE").equals("RESOURCE")) {
							deviceProfile = new Emap_Device_Profile(jsonObj.getString("SRCEMA"),
									sub1JsonObj.getString("DEVICEEMAID"), sub1JsonObj.getString("DEVICETYPE"),
									sub1JsonObj.getString("STATE"), 0, 0, sub1JsonObj.getInt("PRIORITY"),
									sub1JsonObj.getDouble("POWER"), 0.0, 0.0, 0.0, 0.0, 0.0);
						}
						global.emaProtocolCoAP_Device.put(sub1JsonObj.getString("DEVICEEMAID"), deviceProfile);

					}

					global.emaProtocolCoAP.replace(jsonObj.getString("SRCEMA"), emaProfile);

					drmsg.put("SrcEMA", global.SYSTEM_ID);
					drmsg.put("DestEMA", emaProfile.getEmaID());
					drmsg.put("responseCode", 200);
					drmsg.put("responseDescription", "OK");
					drmsg.put("requestID", emaProfile.getRequestID());
					drmsg.put("transportName", "CoAP");
					drmsg.put("version", emaProfile.getVersion());
					drmsg.put("threshold", sub2JsonObj.getDouble("MARGIN"));
					drmsg.put("time", new Date(System.currentTimeMillis()));
					drmsg.put("service", "RegisteredReport");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return drmsg.toString();
		}

		public String acknowledgePOLL(String requestText) {
			JSONObject drmsg = new JSONObject();
			String srcEMA = null;
			if (version.equals("EMAP1.0b")) {

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("SRCEMA"));

					com.mir.ems.profile.emap.v2.RegisterReport rt2 = new com.mir.ems.profile.emap.v2.RegisterReport();
					rt2.setSrcEMA(global.SYSTEM_ID);
					rt2.setDestEMA(emaProfile.getEmaID());
					rt2.setRequestID(emaProfile.getRequestID());
					rt2.setService("RegisterReport");
					rt2.setTime(new Date(System.currentTimeMillis()).toString());

					String payload = rt2.toString();

					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "NORESPONSE";

			}

			else if (version.equals("OpenADR2.0b")) {

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());

					srcEMA = jsonObj.getString("VENID");
					global.venRegisterFlag.replace(srcEMA, 1);

					emaProfile = new Emap_Cema_Profile();
					emaProfile.setEmaID(jsonObj.getString("VENID"));

					com.mir.ems.profile.openadr.recent.RegisterReport rt2 = new com.mir.ems.profile.openadr.recent.RegisterReport();
					rt2.setDestEMA(emaProfile.getEmaID());
					rt2.setRequestID("REQUESTID");
					rt2.setService("oadrRegisterReport");

					String payload = rt2.toString();

					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "NORESPONSE";

			}

			else {
				try {

					emaProfile = new Emap_Cema_Profile();
					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setType(jsonObj.getString("TYPE"));
					// System.out.println("레지레지");

					if (emaProfile.getType().toUpperCase().equals("REGISTRATION")) {
						// System.out.println("여기다");
						drmsg.put("SrcEMA", global.SYSTEM_ID);
						drmsg.put("DestEMA", emaProfile.getEmaID());
						drmsg.put("version", emaProfile.getVersion());
						drmsg.put("requestID", emaProfile.getRequestID());
						drmsg.put("reportName", "InitReport");
						drmsg.put("reportType", "Registration");
						drmsg.put("EMAregisteredinformation",
								global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getEMARegisteredInfo());
						drmsg.put("EMAregisteredMgninformation",
								global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getEMARegisteredMgnInfo());

						drmsg.put("time", new Date(System.currentTimeMillis()));
						drmsg.put("service", "RegisterReport");

						return drmsg.toString();

					} else {
						return "NORESPONSE";
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "NORESPONSE";

				}
			}
			// return drmsg.toString();
		}

		public String acknowledgeREGISTEREDREPORT(String requestText) {
			JSONObject drmsg = new JSONObject();
			String srcEMA = null;

			if (version.equals("EMAP1.0b")) {

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());
					emaProfile = new Emap_Cema_Profile();
					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"));

					com.mir.ems.profile.emap.v2.Response response = new com.mir.ems.profile.emap.v2.Response();
					response.setDestEMA(emaProfile.getEmaID());
					response.setRequestID(emaProfile.getRequestID());
					response.setResponseCode(200);
					response.setResponseDescription("OK");
					response.setService("Response");
					response.setSrcEMA(global.SYSTEM_ID);
					response.setTime(new Date(System.currentTimeMillis()).toString());

					String payload = response.toString();

					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "NORESPONSE";

			}

			else if (version.equals("OpenADR2.0b")) {

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("VENID")).setRequestID(jsonObj.getString("REQUESTID"));

					com.mir.ems.profile.openadr.recent.Response response = new com.mir.ems.profile.openadr.recent.Response();
					response.setDestEMA(emaProfile.getEmaID());
					response.setRequestID(emaProfile.getRequestID());
					response.setResponseCode(200);
					response.setResponseDescription("OK");
					response.setService("oadrResponse");

					String payload = response.toString();

					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "NORESPONSE";

			}

			else {
				try {

					emaProfile = new Emap_Cema_Profile();

					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"))
							.setVersion(jsonObj.getString("VERSION"));

					drmsg.put("SrcEMA", global.SYSTEM_ID);
					drmsg.put("DestEMA", emaProfile.getEmaID());
					drmsg.put("responseCode", 200);
					drmsg.put("responseDescription", "OK");
					drmsg.put("requestID", emaProfile.getRequestID());
					drmsg.put("type", "REPORT");
					drmsg.put("version", emaProfile.getVersion());
					drmsg.put("time", new Date(System.currentTimeMillis()));
					drmsg.put("service", "Response");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return drmsg.toString();
		}

		@SuppressWarnings("deprecation")
		public String acknowledgeREQUESTEVENT(String requestText) {
			JSONObject drmsg = new JSONObject();
			String srcEMA = null;

			if (version.equals("EMAP1.0b")) {

				// Threshold 분배 알고리즘 시작, Thread 아님, 반드시 이게 끝나고 분배해줘야 하기 때문
				new Greedy().start();

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"));

					Intervals interval = new Intervals();

					interval.addIntervalsParams(global.duration, "uid", 0.0);

					EventSignals es = new EventSignals();
					Event event = new Event();

					// Threshold 정보 전달

					event.addEventParams("eventID",
							new EventSignals().addEventSignalsParams(interval.getIntervalsParams(), "signalName",
									"Control Event", "signalID", 0,
									global.emaThresholdManage.get(jsonObj.getString("SRCEMA")).doubleValue(), 0, 0,
									"KW/WON").getEventSignalsParams(),
							1, "modificationReason", -1, "mirLab", new Date(System.currentTimeMillis()).toString(),
							"eventStatus", false, "SessionSetup", "properties", "components", emaProfile.getEmaID(),
							new Date(System.currentTimeMillis()).toString(), "PT1H", "tolerance", "notification",
							"rampUp", "recovery");

					// Passive 기본 가격정보 전송

					if (global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).isTableChanged()) {

						for (int i = 0; i < global.priceTable.size(); i++) {

							if (global.priceTable.get(i).getType() != null) {
								if (global.priceTable.get(i).getType().equals("Industrial1")) {

									for (int j = 0; j < 3; j++) {

										String vtnComment = "";
										double price = 0;

										if (j == 0) {
											vtnComment = "Summer";
											price = global.priceTable.get(i).getSummer();
										} else if (j == 1) {
											vtnComment = "Spring/Fall";
											price = global.priceTable.get(i).getSpringFall();
										} else if (j == 2) {
											vtnComment = "Winter";
											price = global.priceTable.get(i).getWinter();
										}

										event.addEventParams("eventID", new EventSignals()
												.addEventSignalsParams(interval.getIntervalsParams(), "signalName",
														"Price Event", "signalID", 0, 0, 0, price, "KW/WON")
												.getEventSignalsParams(), 1, "modificationReason", -1,
												"http://cyber.kepco.co.kr/ckepco/front/jsp/CY/E/E/CYEEHP00103.jsp",
												global.createTableDate, "eventStatus", true, vtnComment, "properties",
												"components", emaProfile.getEmaID(), "dtStart", "duration", "tolerance",
												"notification", "rampUp", "recovery");

									}
								}

								else if (global.priceTable.get(i).getType().equals("Industrial2")) {
									// 추후 가격정보에 대한 더 상세한 자료가 필요할 경우 추가, 가격표는
									// ElectricityPrice.txt 정보 참조
								} else if (global.priceTable.get(i).getType().equals("Industrial3")) {
									// 추후 가격정보에 대한 더 상세한 자료가 필요할 경우 추가, 가격표는
									// ElectricityPrice.txt 정보 참조
								}
							}
						}

						global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).setTableChanged(false);
					}

					// Active Mode 일때 실시간 가격정보 전달
					if (MainFrame.rdbtnmntmNewRadioItem_1.isSelected()) {

						if (global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).isRealTimetableChanged()) {

							Calendar now = Calendar.getInstance();
							int cHour = now.get(Calendar.HOUR_OF_DAY);
							int year = now.get(Calendar.YEAR);
							int month = now.get(Calendar.MONTH) + 1;
							int date = now.get(Calendar.DATE);

							for (int i = 0; i < global.realTimePriceTable.size(); i++) {

								int time = Integer
										.parseInt(global.realTimePriceTable.get(i).getStrTime().split(":")[0]);

								if (time >= cHour) {

									double price = global.realTimePriceTable.get(i).getPrice();

									event.addEventParams("eventID",
											new EventSignals()
													.addEventSignalsParams(interval.getIntervalsParams(), "signalName",
															"Price Event", "signalID", 0, 0, 0, price, "KW/WON")
													.getEventSignalsParams(),
											1, "modificationReason", -1,
											"https://hourlypricing.comed.com/live-prices/?date=20180826",
											new Date(System.currentTimeMillis()).toString(), "eventStatus", true,
											"RealTimePricing", "properties", "components", emaProfile.getEmaID(),
											new Date(year, month, date, time, 0).toString(), "PT1H", "tolerance",
											"notification", "rampUp", "recovery");
								}
							}

							global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).setRealTimetableChanged(false);

						}
					}

					/*-----------------여기까지가 Active Mode --------------*/

					EventResponse er = new EventResponse();
					er.setRequestID(emaProfile.getRequestID());
					er.setResponseCode(200);
					er.setResponseDescription("OK");

					com.mir.ems.profile.emap.v2.DistributeEvent drE = new com.mir.ems.profile.emap.v2.DistributeEvent();

					drE.setDestEMA(emaProfile.getEmaID());
					drE.setEvent(event.getEventParams());
					drE.setRequestID(emaProfile.getRequestID());
					drE.setResponse(er.toString());
					drE.setService("DistributeEvent");
					drE.setSrcEMA(global.SYSTEM_ID);
					drE.setTime(new Date(System.currentTimeMillis()).toString());
					drE.setResponseRequired("Always");

					String payload = drE.toString();

					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "NORESPONSE";

			}

			else if (version.equals("OpenADR2.0b")) {

				// Threshold 분배 알고리즘 시작, Thread 아님, 반드시 이게 끝나고 분배해줘야 하기 때문
				new Greedy().start();

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("VENID")).setRequestID(jsonObj.getString("REQUESTID"));

					Intervals interval = new Intervals();
					interval.addIntervalsParams(global.duration, "uid", 1234.0);

					com.mir.ems.profile.openadr.recent.Event event = new com.mir.ems.profile.openadr.recent.Event();
					// Threshold 정보 전달

					event.addEventParams("eventID",
							new com.mir.ems.profile.openadr.recent.EventSignals()
									.addEventSignalsParams(interval.getIntervalsParams(), "signalName", "Control Event",
											"signalID", 0)
									.getEventSignalsParams(),
							1, "modificationReason", -1, "mirLab", new Date(System.currentTimeMillis()).toString(),
							"eventStatus", false, "SessionSetup", "properties", "components", emaProfile.getEmaID(),
							new Date(System.currentTimeMillis()).toString(), "PT1H", "tolerance", "notification",
							"rampUp", "recovery");

					com.mir.ems.profile.openadr.recent.EventResponse er = new com.mir.ems.profile.openadr.recent.EventResponse();
					er.setRequestID(emaProfile.getRequestID());
					er.setResponseCode(200);
					er.setResponseDescription("OK");

					com.mir.ems.profile.openadr.recent.DistributeEvent drE = new com.mir.ems.profile.openadr.recent.DistributeEvent();

					drE.setSrcEMA(global.SYSTEM_ID);
					drE.setEvent(event.getEventParams());
					drE.setRequestID(emaProfile.getRequestID());
					drE.setResponse(er.toString());
					drE.setService("oadrDistributeEvent");
					drE.setResponseRequired("Always");

					String payload = drE.toString();
					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "NORESPONSE";

			}

			else {
				try {

					emaProfile = new Emap_Cema_Profile();
					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"));

					drmsg.put("SrcEMA", global.SYSTEM_ID);
					drmsg.put("DestEMA", emaProfile.getEmaID());
					drmsg.put("requestID", emaProfile.getRequestID());
					drmsg.put("responseCode", 200);
					drmsg.put("responseDescription", "OK");
					drmsg.put("time", new Date(System.currentTimeMillis()));

					Calendar calendar = Calendar.getInstance();
					String startYMD = "" + calendar.get(Calendar.YEAR) + "" + (calendar.get(Calendar.MONTH) + 1) + ""
							+ calendar.get(Calendar.DATE);
					String startTime = "" + calendar.get(Calendar.HOUR_OF_DAY) + "" + calendar.get(Calendar.MINUTE) + ""
							+ calendar.get(Calendar.SECOND);

					JSONArray eventInfo = new JSONArray();
					JSONObject childFormat = new JSONObject();
					childFormat.put("testEvent", false);
					childFormat.put("marketContext", 1);
					childFormat.put("eventStatus", "Registration");
					childFormat.put("priority", 0);
					childFormat.put("eventID", 0);
					childFormat.put("startYMD", Integer.parseInt(startYMD));
					childFormat.put("startTime", Integer.parseInt(startTime));
					childFormat.put("endYMD", Integer.parseInt(startYMD));
					childFormat.put("endTime", Integer.parseInt(startTime));
					childFormat.put("duration", 1000);
					childFormat.put("uid", 0);
					double currentValue = global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getPower();
					double threshold = global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getMargin();
					childFormat.put("currentValue", currentValue);
					childFormat.put("signalName", "Initial");
					childFormat.put("signalType", "DistributeEvent");
					childFormat.put("signalID", 1);
					childFormat.put("threshold", threshold);
					childFormat.put("capacity", threshold - currentValue);
					eventInfo.put(childFormat);
					drmsg.put("EMADREventInformation", eventInfo);

					eventInfo = new JSONArray();
					childFormat = new JSONObject();
					childFormat.put("price", 1000);
					childFormat.put("unit", "KW");
					eventInfo.put(childFormat);
					drmsg.put("EMADRPriceInformation", eventInfo);

					drmsg.put("service", "DistributeEvent");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return drmsg.toString();
		}

		public String acknowledgeCANCELPARTYREGISTRATION(String requestText) {

			JSONObject drmsg = new JSONObject();

			try {
				emaProfile = new Emap_Cema_Profile();
				jsonObj = new JSONObject(requestText.toUpperCase());

				drmsg.put("SrcEMA", global.SYSTEM_ID);
				drmsg.put("DestEMA", jsonObj.getString("SRCEMA"));
				drmsg.put("requestID", jsonObj.getString("REQUESTID"));
				drmsg.put("registrationID", jsonObj.getString("REGISTRATIONID"));
				drmsg.put("service", "CanceledPartyRegistration");

				global.emaProtocolCoAP.remove(jsonObj.get("SRCEMA"));

			} catch (JSONException e) {

			}
			return "a";
		}

		public String acknowledgeUPDATEREPORT(String requestText) {
			JSONObject drmsg = new JSONObject();
			String srcEMA = null;

			if (version.equals("OpenADR2.0b")) {

				try {
					jsonObj = new JSONObject(requestText);

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("venID")).setRequestID(jsonObj.getString("requestID"));
					String registrationID = global.emaProtocolCoAP.get(jsonObj.get("venID")).getRegistrationID();
					boolean pullModel = global.emaProtocolCoAP.get(jsonObj.get("venID")).isPullModel();
					boolean realTimetable = global.emaProtocolCoAP.get(jsonObj.get("venID")).isRealTimetableChanged();
					boolean timeTable = global.emaProtocolCoAP.get(jsonObj.get("venID")).isTableChanged();

					JSONArray reportArr = new JSONArray(jsonObj.getString("oadrReport"));

					for (int i = 0; i < reportArr.length(); i++) {

						JSONArray decrArr = new JSONArray(
								new JSONObject(reportArr.get(i).toString()).getString("oadrReportDescription"));

						if (reportArr.length() == 1) {
							JSONObject decr = new JSONObject(decrArr.get(0).toString());

							String minTime = decr.getString("oadrMinPeriod");
							String maxTime = decr.getString("oadrMaxPeriod");

							JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));

							double power = 0;
							for (int k = 0; k < powerAtts.length(); k++) {
								JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());
//								System.out.println(powerAtt.getString("voltage"));

								power = powerAtt.getDouble("voltage");

								// 오늘 여기
								Emap_Cema_Profile profile = new Emap_Cema_Profile("MQTT", jsonObj.getString("venID"),
										registrationID, "qos", "state", power, -1, -1, -1, -1, -1, -1, -1, maxTime, minTime,
										-1, pullModel, timeTable, realTimetable, "CONNECT");

								global.emaProtocolCoAP.replace(jsonObj.getString("venID"), profile);

								global.emaProtocolCoAP.replace(jsonObj.getString("venID"), profile);
								// global.emaThresholdManage.put(jsonObj.getString("venID"),
								// power);

								cema_database cd = new cema_database();
								cd.buildup(jsonObj.getString("venID"), "qos", "state", power, -1, -1, -1, -1, -1, -1, -1,
										new Date(System.currentTimeMillis()).toString(),
										new Date(System.currentTimeMillis()).toString(), -1);

							}

							// 자동 DR
//							double margin = global.emaProtocolCoAP.get(jsonObj.getString("venID")).getMargin();
//							if (power > 10 && power >= margin * global.EXPERIMENTPERCENT) {
//								setEvent(jsonObj.getString("venID"), margin, power);
//							}

						}

						else if (reportArr.length() > 1) {
							JSONObject decr = new JSONObject(decrArr.get(i).toString());

							if (decr.getString("rID").contains("EMA")) {
								String minTime = decr.getString("oadrMinPeriod");
								String maxTime = decr.getString("oadrMaxPeriod");

								JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));

								for (int k = 0; k < powerAtts.length(); k++) {
									JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());
//									System.out.println(powerAtt.getString("voltage"));

									double power = powerAtt.getDouble("voltage");

									// 오늘 여기
									Emap_Cema_Profile profile = new Emap_Cema_Profile("MQTT", jsonObj.getString("venID"),
											registrationID, "qos", "state", power, -1, -1, -1, -1, -1, -1, -1, maxTime,
											minTime, -1, pullModel, timeTable, realTimetable, "CONNECT");

									global.emaProtocolCoAP.replace(jsonObj.getString("venID"), profile);

									global.emaProtocolCoAP.replace(jsonObj.getString("venID"), profile);
									// global.emaThresholdManage.put(jsonObj.getString("venID"),
									// power);

									cema_database cd = new cema_database();
									cd.buildup(jsonObj.getString("venID"), "qos", "state", power, -1, -1, -1, -1, -1, -1,
											-1, new Date(System.currentTimeMillis()).toString(),
											new Date(System.currentTimeMillis()).toString(), -1);

								}
							} else {
								String minTime = decr.getString("oadrMinPeriod");
								String maxTime = decr.getString("oadrMaxPeriod");

								JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));

								double totalPower = 0;

								for (int k = 0; k < powerAtts.length(); k++) {
									JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());
//									System.out.println(powerAtt.getString("voltage"));

									double power = powerAtt.getDouble("voltage");
									totalPower += power;
									String rID = decr.getString("rID");
									deviceProfile = new Emap_Device_Profile(jsonObj.getString("venID"), rID,
											decr.getString("deviceType"), "qos", "state", power, -1, -1, -1, -1, -1, -1, -1,
											maxTime, minTime, -1);

									global.emaProtocolCoAP_Device.replace(rID, deviceProfile);

									device_total_database dtd = new device_total_database();
									dtd.buildUp(jsonObj.getString("venID"), rID, decr.getString("deviceType"), "state", -1,
											-1, power, new Date(System.currentTimeMillis()).toString());

								}

								// 자동 DR
//								double margin = global.emaThresholdManage.get(jsonObj.getString("venID")).doubleValue();
//								if (totalPower > 10 && totalPower >= margin * global.EXPERIMENTPERCENT) {
//									setEvent(jsonObj.getString("venID"), margin, totalPower);
//								}

							}
						}

					}

					com.mir.ems.profile.openadr.recent.UpdatedReport rdt = new com.mir.ems.profile.openadr.recent.UpdatedReport();
					rdt.setDestEMA(emaProfile.getEmaID());
					rdt.setRequestID(emaProfile.getRequestID());
					rdt.setResponseCode(200);
					rdt.setResponseDescription("OK");
					rdt.setService("oadrUpdatedReport");
					
					String payload = rdt.toString();
					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			return drmsg.toString();
		}

	}
	public void setEvent(String srcEMA, double margin, double power) {
		Calendar now = Calendar.getInstance();

		int sYear = now.get(Calendar.YEAR);
		int sMonth = now.get(Calendar.MONTH) + 1;
		int sDate = now.get(Calendar.DATE);
		String strYMD = sYear + "" + sMonth + "" + sDate;
		int sHour = now.get(Calendar.HOUR_OF_DAY);
		int sMin = now.get(Calendar.MINUTE);

		String sTime = sHour + "" + sMin + "" + "11";
		String eTime = (sHour + 1) + "" + sMin + "" + "11";

		double threshold = margin * (global.EXPERIMENTPERCENT);

		// PULL MODEL
		if (global.emaProtocolCoAP.get(srcEMA).isPullModel()) {
			global.emaProtocolCoAP_EventFlag.get(srcEMA).setEventFlag(true).setStartYMD(Integer.parseInt(strYMD))
					.setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(strYMD))
					.setEndTime(Integer.parseInt(eTime)).setThreshold(threshold);
		}

		// PUSH MODEL
		if (!global.emaProtocolCoAP.get(srcEMA).isPullModel()) {

			global.initiater.eventOccur(srcEMA, 1, Integer.parseInt(strYMD), Integer.parseInt(sTime + "11"),
					Integer.parseInt(strYMD), Integer.parseInt(eTime + "11"), threshold);

		}

	}

}
