package com.mir.ems.udp;

import java.net.InetAddress;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mir.ems.deviceProfile.VTN_tab;
import com.mir.ems.hashMap.VTN_values;
import com.mir.ems.udp.udpServer.sendUDPThread;

public class UdpMessageHandler {

	public static HashMap<String, VTN_values> vtnHash= new HashMap<String, VTN_values>();

	public String EMSID = "MIR_EMS";
	public String VERSION = "MIR_EMS_Version_1.0";
	
	String message;
	String vtnIP;

	@SuppressWarnings("unchecked")
	public UdpMessageHandler(String message, InetAddress vtnIP, int vtnPort) {

		JSONParser jp = new JSONParser();
		String msgType;

		payloadSet payloadset = new payloadSet();
		String jsonMsg = payloadset.payloadMsgSet(message);

		/*
		 * IPADDR Parse
		 */
		String strVTNIP = vtnIP.toString();
		String[] ipaddrSplit = strVTNIP.split("/");
		String parsedVTNIP = ipaddrSplit[1];

		try {
			JSONObject msg_json = (JSONObject) jp.parse(jsonMsg);
			msgType = (String) msg_json.get("msgType");

			JSONObject drmsg = new JSONObject();

			switch (msgType) {

			case "CreatedVTNPartyRegistration":

				String vtnID = (String) msg_json.get("vtnID");
				String profileName = (String) msg_json.get("ProfileName");
				String transportName = (String) msg_json.get("TransportName");
				String registrationID = (String) msg_json.get("registrationID");
//				int registrationID = Integer.parseInt(msg_json.get("registrationID").toString());
				int venNum = Integer.parseInt(msg_json.get("venNum").toString());

				VTN_values vtn_values = new VTN_values(vtnIP, vtnPort, vtnID, venNum, profileName, transportName, 0.0);
				vtnHash.put(parsedVTNIP, vtn_values);
				
				drmsg.put("msgType", "CreatedVTNPartyRegistrationACK");
				drmsg.put("VTNID", vtnID);
				drmsg.put("EMSID", EMSID);
				drmsg.put("TransportName", "UDP/JSON");
				drmsg.put("Version", VERSION);
				drmsg.put("Response", 200);
				drmsg.put("ResponseType", "OK");
				drmsg.put("RegistrationID", registrationID);
				drmsg.put("Duration", 2000);

				String setREGACK = drmsg.toJSONString();

				Runnable p = new sendUDPThread(vtnIP, vtnPort, setREGACK);
				Thread p4 = new Thread(p);
				p4.start();
				VTN_tab.modify_EMA_table();
				break;
			case "vtnReport":

				String vtnID1 = (String) msg_json.get("vtnID");
				int venNum1 = Integer.parseInt(msg_json.get("venNum").toString());
				String profileName1 = (String) msg_json.get("ProfileName");
				String transportName1 = (String) msg_json.get("TransportName");
				double currentValue = Double.parseDouble(msg_json.get("currentValue").toString());

				
				VTN_values vtn_values1 = new VTN_values(vtnIP, vtnPort, vtnID1, venNum1, profileName1, transportName1, currentValue);
				vtnHash.put(parsedVTNIP, vtn_values1);
				
				drmsg.put("msgType", "vtnReportACK");

				drmsg.put("EMSID", EMSID);
				drmsg.put("VTNID", vtnID1);
				drmsg.put("Response", 200);
				String setREPORTACK = drmsg.toJSONString();

				Runnable p2 = new sendUDPThread(vtnIP, vtnPort, setREPORTACK);
				Thread p5 = new Thread(p2);
				p5.start();
				VTN_tab.modify_EMA_table();

				break;
				
			case "DestroyVTNPartyRegistration":
				String vtnID2 = (String) msg_json.get("vtnID");
				
				drmsg.put("msgType", "DestroyVTNPartyRegistrationACK");
				drmsg.put("EMSID", EMSID);
				drmsg.put("VTNID", vtnID2);
				drmsg.put("Response", 200);
				String setDESTROYACK = drmsg.toJSONString();

				Runnable p3 = new sendUDPThread(vtnIP, vtnPort, setDESTROYACK);
				Thread p6 = new Thread(p3);
				p6.start();
				
				vtnHash.remove(parsedVTNIP);
				VTN_tab.modify_EMA_table();

				break;
			default:
				System.out.println("Not defined Type");
			}

		} catch (ParseException e) {
			e.printStackTrace();

		}

	}
}
