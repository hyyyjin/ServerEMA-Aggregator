package com.mir.ems.coap;

import org.eclipse.californium.core.server.resources.CoapExchange;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CoAPDR extends Thread {
	public static String Text;
	String DR_MSG;

	static int ven_id;
	
	static int new_ven; //check new ven
	final String queryregistration = "QueryRegistration";
	final String createpartyregistration = "CreatePartyRegistrationRequest";
	final String registerreport = "RegisterReport";
	final String poll = "Poll";
	final String registeredreport = "RegisteredReport";
	final String requestevent = "RequestEvent";
	final String createdevent = "CreatedEvent";
	final String updateReport = "UpdateReport";

	
	
	public String gw;
	public String venID;
	public String vtnID = "MIR_VTN";
	public int requestID;
	public int version = 2;

	@SuppressWarnings("unchecked")
	public void run(CoapExchange exchange, int seq) {
		Text = exchange.getRequestText();

		System.out.println("aaaa"+seq+exchange);
		JSONParser jp = new JSONParser();
		try {
			JSONObject msg_json = (JSONObject) jp.parse(Text);
			String Service = (String) msg_json.get("Service");
			int state = 0;
			if (seq == 0) {
				if (Service.equals(queryregistration)) {
					state = 1;
				}
				if (Service.equals(createpartyregistration)) {
					state = 2;
				}
				if (Service.equals(registerreport)) {
					state = 3;
				}
				if (Service.equals(poll)) {
					state = 4;
				}
				if (Service.equals(registeredreport)) {
					state = 5;
					
				}
				if (Service.equals(requestevent)) {
					state = 6;
				}
				if (Service.equals(createdevent)) {
					state = 7;
				}
				if (Service.equals(updateReport)) {
					System.out.println("같다");
					state = 8;
				}
				
			} else if (seq == 1) {
				state = 7;
			}

			switch (state) {
			case 1:
				System.out.println("QueryRegistration");

				JSONObject drmsg = new JSONObject();

				venID = (String) msg_json.get("VENID");
				requestID = ((Long) msg_json.get("RequestID")).intValue();
				drmsg.put("Service", "CreatedPartyRegistration");
				drmsg.put("VENID", venID);
				drmsg.put("VTNID", vtnID);
				drmsg.put("TransportName", vtnID);
				drmsg.put("RequestID", requestID);
				drmsg.put("RegistrationID", requestID);
				drmsg.put("Duration", 2000);

				DR_MSG = drmsg.toJSONString();

				System.out.println("Wait Next Message");
				new_ven = 1;

				break;
				
			case 2:
				drmsg = new JSONObject();
				System.out.println("CreatePartyRegistrationRequest");

				venID = (String) msg_json.get("oadrVenName");
				requestID = ((Long) msg_json.get("RequestID")).intValue();

				version = ((Long) msg_json.get("Version")).intValue();

				drmsg.put("Service", "CreatedPartyRegistration2");

				drmsg.put("VENID", venID);

				drmsg.put("VTNID", vtnID);

				drmsg.put("TransportName", vtnID);

				drmsg.put("Version", version);

				drmsg.put("Response", 200);

				drmsg.put("RequestID", requestID);

				drmsg.put("Duration", 2000);

				DR_MSG = drmsg.toJSONString();

				break;
			case 3:
				drmsg = new JSONObject();
				System.out.println("RegisterReport");
//				venID = (String) msg_json.get("VENID");
				requestID = ((Long) msg_json.get("RequestID")).intValue();
				// version = 2;

				drmsg.put("Service", "RegisteredReport");
				drmsg.put("VENID", venID);
				drmsg.put("VTNID", vtnID);
				drmsg.put("TransportName", vtnID);
				drmsg.put("Version", version);
				drmsg.put("Response", 200);
				drmsg.put("RequestID", requestID);

				DR_MSG = drmsg.toJSONString();

				break;

			case 4:
				
				System.out.println("Poll Message");
				drmsg = new JSONObject();

				venID = (String) msg_json.get("VENID");
				String ven_id_t[] = venID.split("_MIR");
				ven_id = Integer.valueOf(ven_id_t[ven_id_t.length - 1]);
				
				
				
				System.out.println("venID: " + ven_id);
				
				
				requestID = ((Long) msg_json.get("RequestID")).intValue();
				version = ((Long) msg_json.get("Version")).intValue();

				drmsg.put("Service", "RegisterReport");
				drmsg.put("VENID", venID);
				drmsg.put("VTNID", vtnID);
				drmsg.put("TransportName", vtnID);
				drmsg.put("Version", version);
				drmsg.put("Response", "ACK_SUCCESS");
				drmsg.put("RequestID", requestID);
				drmsg.put("Duration", 2000);

				DR_MSG = drmsg.toJSONString();
				new_ven = 2;

				break;
			case 5:
				drmsg = new JSONObject();

				venID = (String) msg_json.get("VENID");
				requestID = ((Long) msg_json.get("RequestID")).intValue();
				version = ((Long) msg_json.get("Version")).intValue();

				drmsg.put("Service", "Response");
				drmsg.put("VENID", venID);
				drmsg.put("Response", 200);
				drmsg.put("Version", version);

				DR_MSG = drmsg.toJSONString();
				// p_topic = "/gw/" + gwID[1] + "/oadrinit" + "/Response";

				break;
			case 6:
				drmsg = new JSONObject();

				venID = (String) msg_json.get("VENID");
				requestID = ((Long) msg_json.get("RequestID")).intValue();
				// version = 2;

				drmsg.put("Service", "DistributeEvent");
				drmsg.put("VENID", venID);
				drmsg.put("VTNID", vtnID);
				drmsg.put("TransportName", vtnID);
				drmsg.put("Version", version);
				drmsg.put("Response", "ACK_SUCCESS");
				drmsg.put("RequestID", requestID);

				DR_MSG = drmsg.toJSONString();

				break;
			case 7:
				drmsg = new JSONObject();
				venID = (String) msg_json.get("VENID");
				requestID = ((Long) msg_json.get("RequestID")).intValue();
				
				drmsg.put("Service", "Response");
				drmsg.put("RequestID", requestID);
				drmsg.put("VENID", venID);
				drmsg.put("Response", 200);

				DR_MSG = drmsg.toJSONString();

				break;
			case 8:
				System.out.println("aaa");
				drmsg = new JSONObject();
				venID = (String) msg_json.get("VENID");
				requestID = ((Long) msg_json.get("RequestID")).intValue();
				
				drmsg.put("RequestID", requestID);
				drmsg.put("VENID", venID);
				drmsg.put("Response", 200);
				drmsg.put("Responsedescription", "MIR");

				DR_MSG = drmsg.toJSONString();

				break;
			default:
				break;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int GetVENID(){
		return ven_id;
	}
	public String DRMessage() {
		return DR_MSG;
	}
	public int newVEN(){
		return new_ven;
	}

}
