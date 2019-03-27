package com.mir.ems.mqtt;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mir.ems.GUI.MainFrame;
import com.mir.ems.database.GatewayDatabase;
import com.mir.ems.database.item.DeviceClass;
import com.mir.ems.database.item.EMAClass;
import com.mir.ems.database.item.EMAP_CoAP_EMA_DR;
import com.mir.ems.database.item.GeneratorClass;
import com.mir.ems.database.item.RecloserClass;
import com.mir.ems.database.item.ResourceClass;
import com.mir.ems.database.item.ServerEmaProfile;
import com.mir.ems.database.item.SmartMeterClass;
import com.mir.ems.database.item.StorageClass;
import com.mir.ems.deviceProfile.EMA_tab;
import com.mir.ems.deviceProfile.Ess_tab;
import com.mir.ems.deviceProfile.GatewaySettingPanel;
import com.mir.ems.deviceProfile.Led_tab;
import com.mir.ems.deviceProfile.Pv_tab;
import com.mir.ems.deviceProfile.Recloser_tab;
import com.mir.ems.deviceProfile.Resource_tab;
import com.mir.ems.deviceProfile.SmartMeter_tab;
import com.mir.ems.globalVar.global;
import com.mir.ems.hashMap.ESS_values;
import com.mir.ems.hashMap.PV_values;
import com.mir.ems.hashMap.Recloser_values;
import com.mir.ems.hashMap.Resource_values;
import com.mir.ems.main.Connection;
//import com.mir.ems.mqtt.Mqtt.publishThread;
import com.mir.ems.topTab.DRScheduling;

public class HandleMqttMessage extends Thread {

	public static HashMap<String, ESS_values> essHashMap;
	public static HashMap<String, PV_values> pvHashMap;
	public static boolean essValueCheck = false;
	public static boolean pvValueCheck = false;

	public static HashMap<String, Recloser_values> recloserHashMap;
	public static HashMap<String, Resource_values> resourceHashMap;
	public static boolean recloserValueCheck = false;
	public static boolean resourceValueCheck = false;

	public static GatewayDatabase gateway_db;
	public static ProcessStatus processStatus;
	public ESS_values ess_Value;
	public PV_values pv_Value;
	public Recloser_values recloser_value;
	public Resource_values resource_value;

	InetAddress Broker = null;

	static int client_cnt;
	String topic_process;

	String process_value;

	String venID;
	int version;
	String gw;
	int gwNum;
	int requestID;

	MqttClient client;
	// public HandleMqttMessage() {
	//
	// // TODO Auto-generated constructor stub
	// }

	/*
	 * Device(Rasp, ESS, PV) Registration Monitoring Device Status
	 */

	public HandleMqttMessage(MqttClient client, String topic_process, String message, String gw, int gwNum, String dev,
			int devNum) {
		this.client = client;
		String DevIndex = "MQTT device" + devNum;

		if (topic_process.equals("DeviceConnect")) {

			Thread thread = new Thread();
			try {

				thread.sleep(3);
				int state = 0, dimming = 0, priority = 0, value = 0;

				// DeviceClass new_node = new DeviceClass(1, devNum + "", gwNum
				// + "", 1, DevIndex, state, dimming,
				// priority);

				// if (nodeIsExist(new_node, value) == false) {
				// new_node.value_list.set(new_node.value_list.size() - 1,
				// (double) value);
				// Led_tab.device_db.led_list.add(new_node);
				// Led_tab.modify_LED_table();
				// }

				JSONObject drmsg = new JSONObject();

				drmsg.put("gw", gwNum);
				drmsg.put("dev", devNum);
				drmsg.put("onoff", "null");
				drmsg.put("dimming", "null");
				drmsg.put("watt", "null");
				drmsg.put("priority", "null");

				String p_topic = "gw/" + gwNum + "/dev/" + devNum + "/DeviceConnectACK";
				String m_msg = drmsg.toJSONString();

				publishThread(p_topic, global.qos, m_msg.getBytes());

				// Runnable p = new publishThread(p_topic, global.qos,
				// m_msg.getBytes());
				// Thread p7 = new Thread(p);
				// p7.start();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else if (topic_process.equals("DeviceDisconnect")) {
			Thread thread = new Thread();
			try {
				thread.sleep(3);

				/*
				 * 추가하고 삭제하는 부분 문제있음 수정해야댐
				 */
				int deviceListSize = Led_tab.device_db.led_list.size();

				for (int i = 0; i < deviceListSize; i++) {
					if (Led_tab.device_db.led_list.get(i).node_id == devNum) {
						Led_tab.led_table_model.removeRow(i);
						Led_tab.device_db.led_list.remove(i);
						deviceListSize--;
						Led_tab.modify_LED_table();
					}
				}

				JSONObject drmsg = new JSONObject();

				drmsg.put("gw", gwNum);
				drmsg.put("dev", devNum);
				drmsg.put("onoff", "null");
				drmsg.put("dimming", "null");
				drmsg.put("watt", "null");
				drmsg.put("priority", "null");

				String p_topic = "gw/" + gwNum + "/dev/" + devNum + "/DeviceDisconnectACK";
				String m_msg = drmsg.toJSONString();

				publishThread(p_topic, global.qos, m_msg.getBytes());

				// Runnable p = new publishThread(p_topic, global.qos,
				// m_msg.getBytes());
				// Thread p7 = new Thread(p);
				// p7.start();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (topic_process.equals("RDRrequest") || topic_process.equals("info")) {
			// DeviceControl deviceControl = new DeviceControl(topic_process,
			// message, gw, gwNum, dev, devNum);
			// deviceControl.start();
		}
		/*
		 *
		 * Handling Recloser Message
		 *
		 */
		else if (topic_process.equals("RECLOSERConnect") || topic_process.equals("RECLOSERDisconnect")
				|| topic_process.equals("RECLOSERinit") || topic_process.equals("RECLOSER")) {

			JSONParser jp = new JSONParser();
			JSONObject msg_json;
			try {
				msg_json = (JSONObject) jp.parse(message);

				double power = Double.parseDouble((msg_json.get("Power").toString()));
				int mode = Integer.parseInt(msg_json.get("Mode").toString());
				int priority = Integer.parseInt(msg_json.get("Mode").toString());

				RecloserThread recloser = new RecloserThread(topic_process, gwNum, devNum, power, mode, priority);

				recloser.start();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/*
		 * 
		 * Handling Resource Message
		 * 
		 * 
		 */

		else if (topic_process.equals("RESOURCEConnect") || topic_process.equals("RESOURCEDisconnect")
				|| topic_process.equals("RESOURCEinit") || topic_process.equals("RESOURCE")) {

			JSONParser jp = new JSONParser();
			JSONObject msg_json;
			try {
				msg_json = (JSONObject) jp.parse(message);

				double power = Double.parseDouble((msg_json.get("Power").toString()));
				int mode = Integer.parseInt(msg_json.get("Mode").toString());
				int priority = Integer.parseInt(msg_json.get("Mode").toString());

				ResourceThread resource = new ResourceThread(topic_process, gwNum, devNum, power, mode, priority);
				resource.start();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (topic_process.equals("ESSConnect") || topic_process.equals("ESSDisconnect")
				|| topic_process.equals("ESSinit") || topic_process.equals("ESS")) {

			JSONParser jp = new JSONParser();
			JSONObject msg_json;
			try {
				msg_json = (JSONObject) jp.parse(message);

				double power = Double.parseDouble((msg_json.get("Power").toString()));
				int mode = Integer.parseInt(msg_json.get("Mode").toString());
				int state = Integer.parseInt(msg_json.get("State").toString());
				double changedEnergy = Double.parseDouble(msg_json.get("ChangedEnergy").toString());
				int capacity = Integer.parseInt(msg_json.get("Capacity").toString());
				double soc = Double.parseDouble((msg_json.get("Soc").toString()));
				double volt = Double.parseDouble((msg_json.get("Volt").toString()));
				double hz = Double.parseDouble((msg_json.get("Hz").toString()));
				int priority = Integer.parseInt(msg_json.get("priority").toString());
				EnergyStorageSystemThread ess = new EnergyStorageSystemThread(topic_process, gwNum, devNum, power, mode,
						state, changedEnergy, capacity, soc, volt, hz, priority);
				ess.start();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (topic_process.equals("PVConnect") || topic_process.equals("PVDisconnect")
				|| topic_process.equals("PVinit") || topic_process.equals("PV")) {

			JSONParser jp = new JSONParser();
			JSONObject msg_json;

			try {
				msg_json = (JSONObject) jp.parse(message);
				double power = Double.parseDouble((msg_json.get("Power").toString()));
				int mode = Integer.parseInt(msg_json.get("Mode").toString());
				int priority = Integer.parseInt(msg_json.get("priority").toString());
				PhotoVoltaicThread pv = new PhotoVoltaicThread(topic_process, gw, gwNum, dev, devNum, power, mode,
						priority);
				pv.start();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/*
	 * Gateway Registration //
	 */
	public HandleMqttMessage(MqttClient client, String topic_process, String message, int gwNum) throws MqttException {
		this.client = client;
		String EMAname = "EMA" + gwNum;
		gateway_db = new GatewayDatabase();

		// int gwNum = t
		try {
			Broker = InetAddress.getByName(Connection.mqttIP);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (topic_process.equals("connected")) {
			Thread thread = new Thread();
			try {

				thread.sleep(6);
				double resource = 0.0, threshold = 0.0;
				EMAClass new_node = new EMAClass(EMAname, "MQTT", resource, threshold);

				if (nodeIsExist_EMA(new_node, resource) == false) {
					new_node.value_list.set(new_node.value_list.size() - 1, (double) 0.0);
					EMA_tab.gateway_db.ema_list.add(new_node);
					EMA_tab.modify_EMA_table();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {

				if (MainFrame.gateway_db.gateway_list.containsKey(gwNum)) {
					MainFrame.gateway_db.gateway_list.get(gwNum).ip_addr = Broker;
					if (MainFrame.gateway_db.gateway_list.get(gwNum).port_num != 1883)
						MainFrame.gateway_db.gateway_list.get(gwNum).port_num = 1883;
					GatewaySettingPanel.modify_gateway_table();

				} else {

					MainFrame.gateway_db.addValue(gwNum + "", 1, Broker, 1883, "Mqtt Gateway" + gwNum, 1, 101,
							"class room", 0);

					GatewaySettingPanel.modify_gateway_table();
				}

				thread.sleep(3);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			//
			String p_topic = "gw/" + gwNum + "/connectACK";
			String payload = "gw/" + gwNum + "#";

			publishThread(p_topic, global.qos, payload.getBytes());

		} else if (topic_process.equals("disconnected"))

		{
			Thread thread = new Thread();
			try {
				thread.sleep(5);
				int emaListSize = EMA_tab.gateway_db.ema_list.size();

				for (int i = 0; i < emaListSize; i++) {

					if (EMA_tab.gateway_db.ema_list.get(i).emaName.equals(EMAname)) {
						EMA_tab.ema_table_model.removeRow(i);
						EMA_tab.gateway_db.ema_list.remove(i);
						emaListSize--;
						EMA_tab.modify_EMA_table();
					}

				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String p_topic = "gw/" + gwNum + "/disconnectACK";
			String payload = "gw/" + gwNum + "#";

			publishThread(p_topic, global.qos, payload.getBytes());

		} else if (topic_process.equals("PowerMargin")) {
			JSONParser jp = new JSONParser();
			try {
				JSONObject msg_json = (JSONObject) jp.parse(message);

				double currentPower = Double.parseDouble(msg_json.get("Power").toString());
				double threshold = Double.parseDouble(msg_json.get("Margin").toString());

				EMAClass new_node = new EMAClass(EMAname, "MQTT", currentPower, threshold);

				if (nodeIsExist_EMA(new_node, currentPower) == false) {
					new_node.value_list.set(new_node.value_list.size() - 1, (double) 0.0);
					EMA_tab.gateway_db.ema_list.add(new_node);
					EMA_tab.modify_EMA_table();
				}

				if (MainFrame.gateway_db.gateway_list.containsKey(gwNum)) {
					MainFrame.gateway_db.gateway_list.get(gwNum).threshold = (int) threshold;
					GatewaySettingPanel.modify_gateway_table();
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String p_topic = "gw/" + gwNum + "/PowerMarginACK";
			String payload = "gw/" + gwNum + "#";

			publishThread(p_topic, global.qos, payload.getBytes());

		}

	}

	public HandleMqttMessage(MqttClient client, String topic_process, String message, int gwNum, int dcuNum)
			throws MqttException {
		this.client = client;
		String msgPayload;
		String devID;
		String ema;
		int energyValue;

		if (topic_process.equals("load_profile")) {

		} else if (topic_process.equals("active_energy")) {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonMsg;
			try {
				jsonMsg = (JSONObject) jsonParser.parse(message);
				ema = (String) jsonMsg.get("GW");

				String[] emaID = ema.split("/");
				int emaNum = Integer.parseInt(emaID[1]);

				devID = (String) jsonMsg.get("device_id");
				energyValue = Integer.parseInt(jsonMsg.get("active_energy").toString());

				SmartMeterClass new_node = new SmartMeterClass(emaNum, devID, energyValue);

				if (nodeIsExist_DCU(new_node, energyValue) == false) {
					new_node.value_list.set(new_node.value_list.size() - 1, (double) 0.0);
					SmartMeter_tab.smartMeter_db.smartMeter_list.add(new_node);
					SmartMeter_tab.modify_meter_table();
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public HandleMqttMessage(MqttClient client, String topic_process, String process_value, String message,
			String venID, String gw, int gwNum) throws MqttException {
		this.client = client;

		processStatus = new ProcessStatus();
		processStatus.setProcess_value(process_value);

		if (topic_process.equals("oadrinit")) {

			SendingThread sendingThread = new SendingThread(process_value, message, venID, gwNum);
			sendingThread.start();

		} else if (topic_process.equals("oadrPoll")) {

			OadrPollSendingThread oadrPollSendingThread = new OadrPollSendingThread(process_value, message, venID,
					gwNum);
			oadrPollSendingThread.start();
		} else if (topic_process.equals("UpdateReport")) {
			OadrPollSendingThread oadrPollSendingThread = new OadrPollSendingThread(process_value, message, venID,
					gwNum);
			oadrPollSendingThread.start();
		}
	}

	/*
	 * Thread for Handling Poll Message
	 */
	class OadrPollSendingThread extends Thread {

		final String odarpoll = "Poll";
		final String createdEvent = "CreatedEvent";
		final String updateReport = "UpdateReport";

		protected String process_value;
		protected String message;
		protected String venID;
		protected int gwNum;

		DRScheduling dRScheduling = new DRScheduling();

		public OadrPollSendingThread(String process_value, String message, String venID, int gwNum) {
			this.process_value = process_value;
			this.message = message;
			this.venID = venID;
			this.gwNum = gwNum;
		}

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
		}

		@Override
		public void run() {
			if (process_value.equals(odarpoll)) {
				DRoadrPoll();
			} else if (process_value.equals(createdEvent)) {
				DRoadrCreatedEvent();
			} else if (process_value.equals(updateReport)) {
				DRoadrUpdateReport();
			}
		}

		public void DRoadrPoll() {

			boolean flag = false;
			if (global.emaProtocolCoAP_EventFlag.containsKey(global.CHILD_ID + gwNum)) {

				flag = global.emaProtocolCoAP_EventFlag.get(global.CHILD_ID + gwNum).isEventFlag();

			}
			if (!flag) {

				JSONObject drmsg = new JSONObject();

				drmsg.put("VENID", venID);
				drmsg.put("Response", 200);

				String m_msg = drmsg.toJSONString();
				String p_topic = "gw/" + gwNum + "/oadrPoll" + "/Response";

				publishThread(p_topic, global.qos, m_msg.getBytes());

			} else {

				global.emaProtocolCoAP_EventFlag.get(global.CHILD_ID + gwNum).setEventFlag(false);

				JSONObject drmsg = new JSONObject();

				drmsg.put("Service", "oadrDistributeEvent");
				drmsg.put("StartYMD", global.emaProtocolCoAP_EventFlag.get(global.CHILD_ID + gwNum).getStartYMD());
				drmsg.put("StartTime", global.emaProtocolCoAP_EventFlag.get(global.CHILD_ID + gwNum).getStartTime());
				drmsg.put("EndYMD", global.emaProtocolCoAP_EventFlag.get(global.CHILD_ID + gwNum).getEndYMD());
				drmsg.put("EndTime", global.emaProtocolCoAP_EventFlag.get(global.CHILD_ID + gwNum).getEndTime());
				drmsg.put("Value", global.emaProtocolCoAP_EventFlag.get(global.CHILD_ID + gwNum).getThreshold());
				drmsg.put("Response", 2);
				drmsg.put("RequestID", 1);
				drmsg.put("EventID", 1);
				drmsg.put("ModificationNumber", 0);
				drmsg.put("TargetVEN", "MIR_VEN" + gwNum);
				drmsg.put("Responsedescription", "MIR");
				drmsg.put("OptType", "optIn");

				String p_topic = "gw/" + gwNum + "/drevent" + "/oadrDistributeEvent";
				String m_msg = drmsg.toJSONString();

				publishThread(p_topic, global.qos, m_msg.getBytes());

			}

		}

		public void DRoadrCreatedEvent() {

			JSONObject drmsg = new JSONObject();

			drmsg.put("VENID", venID);
			drmsg.put("Response", 200);

			String m_msg = drmsg.toJSONString();
			String p_topic = "gw/" + gwNum + "/drevent" + "/Response";

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void DRoadrUpdateReport() {

			JSONObject drmsg = new JSONObject();
			drmsg.put("RequestID", 1);
			drmsg.put("VENID", venID);
			drmsg.put("Response", 200);
			drmsg.put("Responsedescription", "OK");

			/*
			 * updatereport를 통해 받은 값을들을 자료구조에 저장해주어야함 그 값을 활용하지 못하고 있어서 수정해야할
			 * 필요가있다.
			 */
			String m_msg = drmsg.toJSONString();
			String p_topic = "gw/" + gwNum + "/oadrUpdatedReport";

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}
	}

	/*
	 * Thread for Handling oadrInit Message
	 */
	class SendingThread extends Thread {

		final String queryregistration = "QueryRegistration";
		final String createpartyregistration = "CreatePartyRegistration";
		final String registereport = "RegisterReport";
		final String poll = "Poll";
		final String registeredreport = "RegisteredReport";
		final String requestevent = "RequestEvent";

		protected String process_value;
		protected String message;
		protected String venID;
		protected int gwNum;

		public SendingThread(String process_value, String message, String venID, int gwNum) {
			this.process_value = process_value;
			this.message = message;
			this.venID = venID;
			this.gwNum = gwNum;
		}

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
		}

		@Override
		public void run() {
			if (process_value.equals(queryregistration)) {
				DRqueryRegistrationThread();
			} else if (process_value.equals(createpartyregistration)) {
				DRcreatePartyRegistration();
			} else if (process_value.equals(registereport)) {
				DRregisterReport();
			} else if (process_value.equals(poll)) {
				DRpoll();
			} else if (process_value.equals(registeredreport)) {
				DRregisteredReport();
			} else if (process_value.equals(requestevent)) {
				DRrequestEvent();
			}
		}

		public void DRqueryRegistrationThread() {

			JSONObject drmsg = new JSONObject();

			drmsg.put("VENID", venID);
			drmsg.put("VTNID", "MIR_VTN");
			drmsg.put("TransportName", "MIR_VTN");
			drmsg.put("Duration", 2000);

			String m_msg = drmsg.toJSONString();

			String p_topic = "gw/" + gwNum + "/oadrinit" + "/CreatedPartyRegistration";

			publishThread(p_topic, global.qos, m_msg.getBytes());

			global.emaProtocolCoAP_EventFlag.put(global.CHILD_ID + gwNum, new EMAP_CoAP_EMA_DR());

			global.openADRHashMap.put(global.CHILD_ID + gwNum,
					new ServerEmaProfile(global.CHILD_ID + gwNum, "MQTT", null, null, null, 0, "CONNECT", 0, 0, 0, 0, 0,
							0, 0, 0, 0, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
							new Date(System.currentTimeMillis())));

		}

		public void DRcreatePartyRegistration() {

			JSONObject drmsg = new JSONObject();

			drmsg.put("VENID", venID);
			drmsg.put("VTNID", "MIR_VTN");
			drmsg.put("TransportName", "MIR_VTN");
			drmsg.put("Response", 200);
			drmsg.put("Duration", 2000);

			String m_msg = drmsg.toJSONString();
			String p_topic = "gw/" + gwNum + "/oadrinit" + "/CreatedPartyRegistration";

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void DRregisterReport() {

			JSONObject drmsg = new JSONObject();

			drmsg.put("VENID", venID);
			drmsg.put("VTNID", "MIR_VTN");
			drmsg.put("TransportName", "MIR_VTN");
			drmsg.put("Response", 200);

			String m_msg = drmsg.toJSONString();

			String p_topic = "gw/" + gwNum + "/oadrinit" + "/RegisteredReport";

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void DRpoll() {
			JSONObject drmsg = new JSONObject();

			drmsg.put("VENID", venID);
			drmsg.put("VTNID", "MIR_VTN");
			drmsg.put("TransportName", "MIR_VTN");
			drmsg.put("Response", "ACK_SUCCESS");
			drmsg.put("Duration", 2000);

			String m_msg = drmsg.toJSONString();
			String p_topic = "gw/" + gwNum + "/oadrinit" + "/RegisterReport";

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void DRregisteredReport() {

			JSONObject drmsg = new JSONObject();

			drmsg.put("VENID", venID);
			drmsg.put("Response", 200);

			String m_msg = drmsg.toJSONString();
			String p_topic = "gw/" + gwNum + "/oadrinit" + "/Response";

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void DRrequestEvent() {

			JSONObject drmsg = new JSONObject();

			drmsg.put("VENID", venID);
			drmsg.put("Response", "ACK_SUCCESS");

			String m_msg = drmsg.toJSONString();
			String p_topic = "gw/" + gwNum + "/oadrinit" + "/DistributeEvent";

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

	}

	public boolean nodeIsExist_DCU(SmartMeterClass new_node, double energyValue) {
		int smartMeterListSize;
		int index = -1;

		smartMeterListSize = SmartMeter_tab.smartMeter_db.smartMeter_list.size();

		for (int i = 0; i < smartMeterListSize; i++) {

			if (SmartMeter_tab.smartMeter_db.smartMeter_list.get(i).devID.equals(new_node.devID)) {

				index = i;
				break;
			}
		}
		if (index != -1) {

			SmartMeter_tab.smartMeter_table_model.setValueAt(new_node.energyValue, index, 3);
			SmartMeter_tab.smartMeter_db.smartMeter_list.get(index).value_list.remove(0);
			SmartMeter_tab.smartMeter_db.smartMeter_list.get(index).value_list.add((double) energyValue);

			return true;
		}
		return false;
	}

	//
	public boolean nodeIsExist_EMA(EMAClass new_node, double resource) {
		int emaListSize;
		int index = -1;

		emaListSize = EMA_tab.gateway_db.ema_list.size();

		for (int i = 0; i < emaListSize; i++) {
			if (EMA_tab.gateway_db.ema_list.get(i).emaName.equals(new_node.emaName)) {
				index = i;

				break;
			}
		}
		if (index != -1) {
			EMA_tab.ema_table_model.setValueAt(new_node.resource, index, 3);
			EMA_tab.ema_table_model.setValueAt(new_node.threshold, index, 4);

			EMA_tab.gateway_db.ema_list.get(index).value_list.remove(0);
			EMA_tab.gateway_db.ema_list.get(index).value_list.add((double) resource);

			return true;
		}

		return false;
	}

	public static boolean nodeIsExist(DeviceClass new_node, int value) {
		int deviceListSize;
		int index = -1;

		if (new_node.type == 1) { // LED
			deviceListSize = Led_tab.device_db.other_list.size();
			for (int i = 0; i < deviceListSize; i++) { // check the
														// existence of
														// the node in other
														// device table
				if (Led_tab.device_db.other_list.get(i).node_id == new_node.node_id
						&& Led_tab.device_db.other_list.get(i).room_id == new_node.room_id) {
					Led_tab.device_db.other_list.remove(i);
					// GeneratorTablePanel.generator_table_model.removeRow(i);
					break;
				}
			}
			deviceListSize = Led_tab.device_db.led_list.size();
			for (int i = 0; i < deviceListSize; i++) { // get the index of
														// the
														// node in the list
				if (Led_tab.device_db.led_list.get(i).node_id == new_node.node_id
						&& Led_tab.device_db.led_list.get(i).room_id == new_node.room_id) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				String onOff = "on";
				// change the item in the led table
				if (new_node.mode == 0)
					onOff = "off";
				Led_tab.led_table_model.setValueAt(onOff, index, 3);
				Led_tab.led_table_model.setValueAt(new_node.dimming, index, 4);
				Led_tab.led_table_model.setValueAt(new_node.priority, index, 5);
				// add the power consumption data to the power consumption
				// list
				Led_tab.device_db.led_list.get(index).value_list.remove(0);
				Led_tab.device_db.led_list.get(index).value_list.add((double) value);
				return true;
			}
		}
		return false;
	}

	/*
	 * Thread for ESS
	 */
	class EnergyStorageSystemThread extends Thread {

		final String essConnect = "ESSConnect";
		final String essDisconnect = "ESSDisconnect";
		final String essInit = "ESSinit";
		final String ess = "ESS";

		private String topic_value, name;
		private int gwNum;

		double soc, power, changedEnergy, volt, hz;
		int storage_id, mode, state, capacity, priority;

		public EnergyStorageSystemThread(String topic_value, int gwNum, int storage_id, double power, int mode,
				int state, double changedEnergy, int capacity, double soc, double volt, double hz, int priority) {

			this.topic_value = topic_value;
			this.gwNum = gwNum;
			this.storage_id = storage_id;
			this.power = power;
			this.mode = mode;
			this.state = state;
			this.changedEnergy = changedEnergy;
			this.capacity = capacity;
			this.soc = soc;
			this.volt = volt;
			this.hz = hz;
			this.priority = priority;
		}

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
		}

		@Override
		public void run() {
			if (topic_value.equals(essConnect)) {
				ESSConnect();
			} else if (topic_value.equals(essDisconnect)) {
				ESSDisconnect();
			} else if (topic_value.equals(essInit)) {
				ESSInitialize();
			} else if (topic_value.equals(ess)) {
				ESSstatusUpdate();
			}
		}

		public void ESSConnect() {
			System.out.println("ESS Connect Message");
			name = "ESS" + storage_id;
			StorageClass new_node = new StorageClass(2, gwNum, storage_id, name, power, mode, state, changedEnergy,
					capacity, soc, volt, hz, priority);

			if (nodeIsExist_ESS(new_node, power) == false) {
				new_node.value_list.set(new_node.value_list.size() - 1, (double) power);
				Ess_tab.storage_db.storage_list.add(new_node);
				Ess_tab.modify_storage_table();

			}
			int essListSize = Ess_tab.storage_db.storage_list.size();

			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("ESSID", storage_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("State", state);
			drmsg.put("ChangedEnergy", changedEnergy);
			drmsg.put("Capacity", capacity);
			drmsg.put("Soc", soc);
			drmsg.put("Volt", volt);
			drmsg.put("Hz", hz);

			String p_topic = "gw/" + gwNum + "/ESSConnectACK";
			String m_msg = drmsg.toJSONString();

			// Runnable p = new publishThread(p_topic, global.qos,
			// m_msg.getBytes());
			// Thread p7 = new Thread(p);
			// p7.start();
		}

		public void ESSDisconnect() {

			int essListSize = Ess_tab.storage_db.storage_list.size();

			for (int i = 0; i < essListSize; i++) {
				if (Ess_tab.storage_db.storage_list.get(i).storage_id == storage_id) {
					Ess_tab.ess_table_model.removeRow(i);
					Ess_tab.storage_db.storage_list.remove(i);
					essListSize--;
					Ess_tab.modify_storage_table();
				}
			}

			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("ESSID", storage_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("State", state);
			drmsg.put("ChangedEnergy", changedEnergy);
			drmsg.put("Capacity", capacity);
			drmsg.put("Soc", soc);
			drmsg.put("Volt", volt);
			drmsg.put("Hz", hz);

			String p_topic = "gw/" + gwNum + "/ESSDisconnectACK";
			String m_msg = drmsg.toJSONString();

		}

		public void ESSInitialize() {

			name = "ESS" + storage_id;
			StorageClass new_node = new StorageClass(2, gwNum, storage_id, name, power, mode, state, changedEnergy,
					capacity, soc, volt, hz, priority);

			if (nodeIsExist_ESS(new_node, power) == false) {
				new_node.value_list.set(new_node.value_list.size() - 1, (double) power);
				Ess_tab.storage_db.storage_list.add(new_node);
				Ess_tab.modify_storage_table();
			}
			essHashMap = new HashMap<String, ESS_values>();
			ess_Value = new ESS_values(2, gwNum, storage_id, name, power, mode, state, changedEnergy, capacity, soc,
					volt, hz);
			essHashMap.put(name, ess_Value);
			setEssHashMap(essHashMap);
			setEssValueCheck(true);
			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("ESSID", storage_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("State", state);
			drmsg.put("ChangedEnergy", changedEnergy);
			drmsg.put("Capacity", capacity);
			drmsg.put("Soc", soc);
			drmsg.put("Volt", volt);
			drmsg.put("Hz", hz);

			String p_topic = "gw/" + gwNum + "/ESSACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void ESSstatusUpdate() {

			name = "ESS" + storage_id;
			StorageClass new_node = new StorageClass(2, gwNum, storage_id, name, power, mode, state, changedEnergy,
					capacity, soc, volt, hz, priority);

			if (nodeIsExist_ESS(new_node, power) == false) {
				Ess_tab.storage_db.storage_list.add(new_node);
				Ess_tab.modify_storage_table();
			}
			essHashMap = new HashMap<String, ESS_values>();
			ess_Value = new ESS_values(2, gwNum, storage_id, name, power, mode, state, changedEnergy, capacity, soc,
					volt, hz);
			essHashMap.put(name, ess_Value);
			setEssHashMap(essHashMap);
			setEssValueCheck(true);

			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("ESSID", storage_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("State", state);
			drmsg.put("ChangedEnergy", changedEnergy);
			drmsg.put("Capacity", capacity);
			drmsg.put("Soc", soc);
			drmsg.put("Volt", volt);
			drmsg.put("Hz", hz);

			String p_topic = "gw/" + gwNum + "/ESSACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public boolean nodeIsExist_ESS(StorageClass new_node, double power) {
			int storageListSize;
			int index = -1;

			storageListSize = Ess_tab.storage_db.storage_list.size();
			for (int i = 0; i < storageListSize; i++) {
				if (Ess_tab.storage_db.storage_list.get(i).storage_id == new_node.storage_id
						&& Ess_tab.storage_db.storage_list.get(i).gwNum == new_node.gwNum) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				String state = "Wait";

				if (new_node.mode == 1)
					state = "Charging";

				else if (new_node.mode == 2)
					state = "Discharging";

				Ess_tab.ess_table_model.setValueAt(new_node.state, index, 3);
				Ess_tab.ess_table_model.setValueAt(new_node.soc, index, 4);
				Ess_tab.ess_table_model.setValueAt(new_node.power, index, 5);
				Ess_tab.ess_table_model.setValueAt(new_node.volt, index, 6);
				Ess_tab.ess_table_model.setValueAt(new_node.hz, index, 7);
				Ess_tab.ess_table_model.setValueAt(new_node.priority, index, 8);

				Ess_tab.storage_db.storage_list.get(index).value_list.remove(0);
				Ess_tab.storage_db.storage_list.get(index).value_list.add((double) power);
				return true;
			}

			return false;
		}
	}

	/*
	 * Thread for PV
	 */
	class PhotoVoltaicThread extends Thread {
		final String pvInit = "PVinit";
		final String pvStatus = "PV";
		final String pvConnect = "PVConnect";
		final String pvDisconnect = "PVDisconnect";
		private String topic_value, name;
		private int gwNum;

		double power;
		int generator_id, mode, priority;

		public PhotoVoltaicThread(String topic_value, String gw, int gwNum, String dev, int generator_id, double power,
				int mode, int priority) {

			this.topic_value = topic_value;
			this.gwNum = gwNum;
			this.generator_id = generator_id;
			this.power = power;
			this.mode = mode;
			this.priority = priority;
		}

		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
		}

		@Override
		public void run() {
			if (topic_value.equals(pvConnect)) {
				PVConnect();
			} else if (topic_value.equals(pvDisconnect)) {
				PVDisconnect();
			} else if (topic_value.equals(pvInit)) {
				PVInitialize();
			} else if (topic_value.equals(pvStatus)) {
				PVstatusUpdate();
			}
		}

		public void PVConnect() {
			name = "PV" + generator_id;
			GeneratorClass new_gen = new GeneratorClass(2, gwNum, generator_id, name, mode, power, priority);

			if (nodeIsExist_PV(new_gen, power) == false) {
				new_gen.value_list.set(new_gen.value_list.size() - 1, (double) power);
				Pv_tab.generator_db.generator_list.add(new_gen);
				Pv_tab.modify_generator_table();
			}
			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("PVID", generator_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("Priority", priority);

			String p_topic = "gw/" + gwNum + "/PVConnectACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void PVDisconnect() {
			System.out.println("PV DisCONNECT MSG");

			System.out.println("ESS DisConnect Message");

			int pvListSize = Pv_tab.generator_db.generator_list.size();

			for (int i = 0; i < pvListSize; i++) {
				if (Pv_tab.generator_db.generator_list.get(i).device_id == generator_id) {
					Pv_tab.pv_table_model.removeRow(i);
					Pv_tab.generator_db.generator_list.remove(i);
					pvListSize--;
					Pv_tab.modify_generator_table();
				}
			}

			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("PVID", generator_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("Priority", priority);

			String p_topic = "gw/" + gwNum + "/PVDisconnectACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

			// Runnable p = new publishThread(p_topic, global.qos,
			// m_msg.getBytes());
			// Thread p7 = new Thread(p);
			// p7.start();

		}

		public void PVInitialize() {
			System.out.println("PV Initialize");

			System.out.println(gwNum + "::gwNum");
			System.out.println(power + "::power");
			System.out.println(mode + "::mode");
			name = "PV" + generator_id;
			System.out.println(name + "::name");
			GeneratorClass new_gen = new GeneratorClass(2, gwNum, generator_id, name, mode, power, priority);

			if (nodeIsExist_PV(new_gen, power) == false) {
				new_gen.value_list.set(new_gen.value_list.size() - 1, (double) power);
				Pv_tab.generator_db.generator_list.add(new_gen);
				System.out.println("PVLISTSIZE::::" + Pv_tab.generator_db.generator_list.size());
				System.out.println("ADD Generator DB");
				Pv_tab.modify_generator_table();
			}
			/*
			 * Database will be added
			 */
			pvHashMap = new HashMap<String, PV_values>();
			pv_Value = new PV_values(2, gwNum, generator_id, name, mode, power, priority);
			pvHashMap.put(name, pv_Value);
			setPvHashMap(pvHashMap);
			setPvValueCheck(true);

			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("PVID", generator_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("Priority", priority);

			String p_topic = "gw/" + gwNum + "/PVACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void PVstatusUpdate() {
			System.out.println("PV Status Update");

			//
			name = "PV" + generator_id;
			GeneratorClass new_gen = new GeneratorClass(2, gwNum, generator_id, name, mode, power, priority);

			if (nodeIsExist_PV(new_gen, power) == false) {
				Pv_tab.generator_db.generator_list.add(new_gen);
				Pv_tab.modify_generator_table();
			}
			pvHashMap = new HashMap<String, PV_values>();
			pv_Value = new PV_values(2, gwNum, generator_id, name, mode, power, priority);
			pvHashMap.put(name, pv_Value);
			setPvHashMap(pvHashMap);
			setPvValueCheck(true);
			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("PVID", generator_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("Priority", priority);

			String p_topic = "gw/" + gwNum + "/PVACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public boolean nodeIsExist_PV(GeneratorClass new_node, double power) {
			int genListSize;
			int index = -1;

			genListSize = Pv_tab.generator_db.generator_list.size();
			for (int i = 0; i < genListSize; i++) { // get the index of
													// the
													// node in the list
				if (Pv_tab.generator_db.generator_list.get(i).device_id == new_node.device_id
						&& Pv_tab.generator_db.generator_list.get(i).gateway_id == new_node.gateway_id) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				String state = "On";
				System.out.println("PV state Change");

				if (new_node.mode == 0)
					state = "Off";

				Pv_tab.pv_table_model.setValueAt(new_node.mode, index, 3);
				Pv_tab.pv_table_model.setValueAt(new_node.power, index, 4);
				Pv_tab.pv_table_model.setValueAt(new_node.priority, index, 5);

				// add the power consumption data to the power consumption
				// list
				Pv_tab.generator_db.generator_list.get(index).value_list.remove(0);
				Pv_tab.generator_db.generator_list.get(index).value_list.add((double) power);
				return true;
			}

			return false;
		}

	}

	class ResourceThread extends Thread {

		final String resourceConnect = "RESOURCEConnect";
		final String resourceDisconnect = "RESOURCEDisconnect";
		final String resourceInit = "RESOURCEinit";
		final String resource = "RESOURCE";

		private String topic_value, name;
		private int gwNum;

		double power;
		int resource_id, mode, priority;

		public ResourceThread(String topic_value, int gwNum, int resource_id, double power, int mode, int priority) {

			this.topic_value = topic_value;
			this.gwNum = gwNum;
			this.resource_id = resource_id;
			this.power = power;
			this.priority = priority;
		}

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
		}

		@Override
		public void run() {
			if (topic_value.equals(resourceConnect)) {
				RESOURCEConnect();
			} else if (topic_value.equals(resourceDisconnect)) {
				RESOURCEDisconnect();
			} else if (topic_value.equals(resourceInit)) {
				RESOURCEinitialize();
			} else if (topic_value.equals(resource)) {
				RESOURCEstatusUpdate();
			}
		}

		public void RESOURCEConnect() {
			System.out.println("RECLOSER Connect Message");
			name = "RESOURCE" + resource_id;
			ResourceClass new_node = new ResourceClass(2, gwNum, resource_id, name, power, mode, priority);

			if (nodeIsExist_Resource(new_node, power) == false) {
				new_node.value_list.set(new_node.value_list.size() - 1, (double) power);
				Resource_tab.resource_db.resource_list.add(new_node);
				Resource_tab.modify_resource_table();
			}

			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("RESOURCEID", resource_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("priority", priority);

			String p_topic = "gw/" + gwNum + "/RESOURCEConnectACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void RESOURCEDisconnect() {

			int resourceListSize = Resource_tab.resource_db.resource_list.size();

			for (int i = 0; i < resourceListSize; i++) {
				if (Resource_tab.resource_db.resource_list.get(i).resource_id == resource_id) {
					Resource_tab.resource_table_model.removeRow(i);
					Resource_tab.resource_db.resource_list.remove(i);
					resourceListSize--;
					Resource_tab.modify_resource_table();
				}
			}
			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("RESOURCEID", resource_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("priority", priority);

			String p_topic = "gw/" + gwNum + "/RESOURCEDisconnectACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void RESOURCEinitialize() {
			System.out.println("RESOURCE Initializing");
			name = "RECLOSER" + resource_id;

			ResourceClass new_node = new ResourceClass(2, gwNum, resource_id, name, power, mode, priority);

			if (nodeIsExist_Resource(new_node, power) == false) {
				new_node.value_list.set(new_node.value_list.size() - 1, (double) power);
				Resource_tab.resource_db.resource_list.add(new_node);
				Resource_tab.modify_resource_table();
			}

			resourceHashMap = new HashMap<String, Resource_values>();
			resource_value = new Resource_values(2, gwNum, resource_id, name, power, mode, priority);
			resourceHashMap.put(name, resource_value);
			setResourceHashMap(resourceHashMap);
			setResourceValueCheck(true);

			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("RECLOSERID", resource_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("priority", priority);

			String p_topic = "gw/" + gwNum + "/RESOURCEACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void RESOURCEstatusUpdate() {
			System.out.println("RESOURCE Status Update");

			name = "RESOURCE" + resource_id;

			ResourceClass new_node = new ResourceClass(2, gwNum, resource_id, name, power, mode, priority);
			if (nodeIsExist_Resource(new_node, power) == false) {
				Resource_tab.resource_db.resource_list.add(new_node);
				Resource_tab.modify_resource_table();
			}

			resourceHashMap = new HashMap<String, Resource_values>();
			resource_value = new Resource_values(2, gwNum, resource_id, name, power, mode, priority);
			resourceHashMap.put(name, resource_value);
			setResourceHashMap(resourceHashMap);
			setResourceValueCheck(true);
			System.out.println(getResourceHashMap());

			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("RECLOSERID", resource_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("priority", priority);

			String p_topic = "gw/" + gwNum + "/RESOURCEACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public boolean nodeIsExist_Resource(ResourceClass new_node, double power) {
			int recloserListSize;
			int index = -1;

			recloserListSize = Resource_tab.resource_db.resource_list.size();

			for (int i = 0; i < recloserListSize; i++) {
				if (Resource_tab.resource_db.resource_list.get(i).resource_id == new_node.resource_id
						&& Resource_tab.resource_db.resource_list.get(i).gateway_id == new_node.gateway_id) {
					index = i;
					break;
				}
			}
			if (index != -1) {

				Resource_tab.resource_table_model.setValueAt(new_node.mode, index, 3);
				Resource_tab.resource_table_model.setValueAt(new_node.power, index, 4);
				Resource_tab.resource_table_model.setValueAt(new_node.priority, index, 5);

				Resource_tab.resource_db.resource_list.get(index).value_list.remove(0);
				Resource_tab.resource_db.resource_list.get(index).value_list.add((double) power);
				return true;
			}
			return false;
		}
	}

	class RecloserThread extends Thread {

		final String recloserConnect = "RECLOSERConnect";
		final String recloserDisconnect = "RECLOSERDisconnect";
		final String recloserInit = "RECLOSERinit";
		final String recloser = "RECLOSER";

		private String topic_value, name;
		private int gwNum;

		double power;
		int recloser_id, mode, priority;

		public RecloserThread(String topic_value, int gwNum, int recloser_id, double power, int mode, int priority) {

			this.topic_value = topic_value;
			this.gwNum = gwNum;
			this.recloser_id = recloser_id;
			this.power = power;
			this.priority = priority;
		}

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
		}

		@Override
		public void run() {
			if (topic_value.equals(recloserConnect)) {
				RECLOSERConnect();
			} else if (topic_value.equals(recloserDisconnect)) {
				RECLOSERDisconnect();
			} else if (topic_value.equals(recloserInit)) {
				RECLOSERinitialize();
			} else if (topic_value.equals(recloser)) {
				RECLOSERstatusUpdate();
			}
		}

		public void RECLOSERConnect() {
			System.out.println("RECLOSER Connect Message");
			name = "RECLOSER" + recloser_id;

			RecloserClass new_node = new RecloserClass(2, gwNum, recloser_id, name, power, mode, priority);

			if (nodeIsExist_Recloser(new_node, power) == false) {
				new_node.value_list.set(new_node.value_list.size() - 1, (double) power);
				Recloser_tab.recloser_db.recloser_list.add(new_node);
				Recloser_tab.modify_recloser_table();
			}

			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("RECLOSERID", recloser_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("priority", priority);

			String p_topic = "gw/" + gwNum + "/RECLOSERConnectACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void RECLOSERDisconnect() {
			System.out.println("RECLOSER DisConnect Message");

			int recloserListSize = Recloser_tab.recloser_db.recloser_list.size();

			for (int i = 0; i < recloserListSize; i++) {
				if (Recloser_tab.recloser_db.recloser_list.get(i).recloser_id == recloser_id) {
					Recloser_tab.recloser_table_model.removeRow(i);
					Recloser_tab.recloser_db.recloser_list.remove(i);
					recloserListSize--;
					Recloser_tab.modify_recloser_table();
				}
			}
			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("RECLOSERID", recloser_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("priority", priority);

			String p_topic = "gw/" + gwNum + "/RECLOSERDisconnectACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());
			;
		}

		public void RECLOSERinitialize() {
			System.out.println("RECLOSER Initializing");
			name = "RECLOSER" + recloser_id;

			RecloserClass new_node = new RecloserClass(2, gwNum, recloser_id, name, power, mode, priority);
			if (nodeIsExist_Recloser(new_node, power) == false) {
				Recloser_tab.recloser_db.recloser_list.add(new_node);
				Recloser_tab.modify_recloser_table();
			}

			recloserHashMap = new HashMap<String, Recloser_values>();
			recloser_value = new Recloser_values(2, gwNum, recloser_id, name, power, mode, priority);
			recloserHashMap.put(name, recloser_value);
			setRecloserHashMap(recloserHashMap);
			setRecloserValueCheck(true);
			System.out.println(getRecloserHashMap());

			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("RECLOSERID", recloser_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("priority", priority);

			String p_topic = "gw/" + gwNum + "/RECLOSERACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public void RECLOSERstatusUpdate() {
			System.out.println("RECLOSER Status Update");

			name = "RECLOSER" + recloser_id;

			RecloserClass new_node = new RecloserClass(2, gwNum, recloser_id, name, power, mode, priority);
			if (nodeIsExist_Recloser(new_node, power) == false) {
				Recloser_tab.recloser_db.recloser_list.add(new_node);
				Recloser_tab.modify_recloser_table();
			}

			recloserHashMap = new HashMap<String, Recloser_values>();
			recloser_value = new Recloser_values(2, gwNum, recloser_id, name, power, mode, priority);
			recloserHashMap.put(name, recloser_value);
			setRecloserHashMap(recloserHashMap);
			setRecloserValueCheck(true);

			JSONObject drmsg = new JSONObject();

			drmsg.put("gw", gwNum);
			drmsg.put("RECLOSERID", recloser_id);
			drmsg.put("Mode", mode);
			drmsg.put("Power", power);
			drmsg.put("priority", priority);

			String p_topic = "gw/" + gwNum + "/RECLOSERACK";
			String m_msg = drmsg.toJSONString();

			publishThread(p_topic, global.qos, m_msg.getBytes());

		}

		public boolean nodeIsExist_Recloser(RecloserClass new_node, double power) {
			int recloserListSize;
			int index = -1;

			recloserListSize = Recloser_tab.recloser_db.recloser_list.size();
			for (int i = 0; i < recloserListSize; i++) {
				if (Recloser_tab.recloser_db.recloser_list.get(i).recloser_id == new_node.recloser_id
						&& Recloser_tab.recloser_db.recloser_list.get(i).gateway_id == new_node.gateway_id) {
					index = i;
					break;
				}
			}
			if (index != -1) {

				Recloser_tab.recloser_table_model.setValueAt(new_node.mode, index, 3);
				Recloser_tab.recloser_table_model.setValueAt(new_node.power, index, 4);
				Recloser_tab.recloser_table_model.setValueAt(new_node.priority, index, 5);

				Recloser_tab.recloser_db.recloser_list.get(index).value_list.remove(0);
				Recloser_tab.recloser_db.recloser_list.get(index).value_list.add((double) power);
				return true;
			}
			return false;
		}
	}

	public static HashMap<String, Recloser_values> getRecloserHashMap() {
		return recloserHashMap;
	}

	public static void setRecloserHashMap(HashMap<String, Recloser_values> recloserHashMap) {
		HandleMqttMessage.recloserHashMap = recloserHashMap;
	}

	public static HashMap<String, Resource_values> getResourceHashMap() {
		return resourceHashMap;
	}

	public static void setResourceHashMap(HashMap<String, Resource_values> resourceHashMap) {
		HandleMqttMessage.resourceHashMap = resourceHashMap;
	}

	public static HashMap<String, ESS_values> getEssHashMap() {
		return essHashMap;
	}

	public void setEssHashMap(HashMap<String, ESS_values> essHashMap) {
		this.essHashMap = essHashMap;
	}

	public static HashMap<String, PV_values> getPvHashMap() {
		return pvHashMap;
	}

	public void setPvHashMap(HashMap<String, PV_values> pvHashMap) {
		this.pvHashMap = pvHashMap;
	}

	public static boolean isEssValueCheck() {
		return essValueCheck;
	}

	public static void setEssValueCheck(boolean essValueCheck) {
		HandleMqttMessage.essValueCheck = essValueCheck;
	}

	public static boolean isRecloserValueCheck() {
		return recloserValueCheck;
	}

	public static void setRecloserValueCheck(boolean recloserValueCheck) {
		HandleMqttMessage.recloserValueCheck = recloserValueCheck;
	}

	public static boolean isResourceValueCheck() {
		return resourceValueCheck;
	}

	public static void setResourceValueCheck(boolean resourceValueCheck) {
		HandleMqttMessage.resourceValueCheck = resourceValueCheck;
	}

	public static boolean isPvValueCheck() {
		return pvValueCheck;
	}

	public static void setPvValueCheck(boolean pvValueCheck) {
		HandleMqttMessage.pvValueCheck = pvValueCheck;
	}

	public void publishThread(String topicName, int qos, byte[] payload) {

		new Thread(new Runnable() {

			public void run() {

				MqttMessage message = new MqttMessage(payload);

				message.setQos(qos);

				try {

					client.publish(topicName, message);

				} catch (MqttException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}).start();
	}
}

class DeviceControl extends Thread {

	private String emaID, devID, payload, service;
	private MqttClient client;

	int state, dimming, value, priority;

	public DeviceControl(MqttClient client, String service, String emaID, String devID, String payload) {

		this.client = client;
		this.service = service;
		this.emaID = emaID;
		this.devID = devID;
		this.payload = payload;

	}

	public void run() {

		if (service.equals("DeviceConnect")) {
			global.devManger.put(devID, new DeviceClass(2, devID, emaID, 1, devID, state, dimming, priority,
				this.client.getServerURI().toString(), this.client, 0));
		}

		if (service.equals("DeviceDisconnect")) {
			global.devManger.remove(devID);
		}

		if (service.equals("RDRrequest")) {
			System.out.println("여기여기");
			rDRrequest();

		} else if (service.equals("info")) {

			state = Integer.parseInt(payload.split("/")[4]);
			dimming = Integer.parseInt(payload.split("/")[5]);
			value = Integer.parseInt(payload.split("/")[6]);
			priority = Integer.parseInt(payload.split("/")[7]);

			stateUpdate();
		}
	}

	public void rDRrequest() {
		
		System.out.println("여기여기");
		
		JSONObject drmsg = new JSONObject();

		drmsg.put("SrcEMA", global.SYSTEM_ID);
		drmsg.put("responseDescription", "OK");
		drmsg.put("responseCode", 200);
		drmsg.put("devnumber", devID);

		String topic = emaID + "/RDRresponse";
		String m_msg = drmsg.toJSONString();

		new Publishing().publishThread(client, topic, global.qos, m_msg.getBytes());

	}

	public void stateUpdate() {

		global.devManger.replace(devID, new DeviceClass(2, devID, emaID, 1, devID, state, dimming, priority,
				this.client.getServerURI().toString(), this.client, value));
		
//		DeviceClass new_node = new DeviceClass(2, devID, emaID, 1, devID, state, dimming, priority,
//				this.client.getServerURI());

//		if (nodeIsExist(new_node, value) == false) {
//			System.out.println("add new note");
//			Led_tab.device_db.led_list.add(new_node);
//			Led_tab.modify_LED_table();
//		}

	}

	public boolean nodeIsExist(DeviceClass new_node, int value) {
		int deviceListSize;
		int index = -1;

		if (new_node.type == 1) { // LED
			deviceListSize = Led_tab.device_db.other_list.size();
			for (int i = 0; i < deviceListSize; i++) { // check the
														// existence of
														// the node in other
														// device table
				if (Led_tab.device_db.other_list.get(i).node_id == new_node.node_id
						&& Led_tab.device_db.other_list.get(i).room_id == new_node.room_id) {
					Led_tab.device_db.other_list.remove(i);
					// GeneratorTablePanel.generator_table_model.removeRow(i);
					break;
				}
			}
			deviceListSize = Led_tab.device_db.led_list.size();
			for (int i = 0; i < deviceListSize; i++) { // get the index of
														// the
														// node in the list
				if (Led_tab.device_db.led_list.get(i).node_id == new_node.node_id
						&& Led_tab.device_db.led_list.get(i).room_id == new_node.room_id) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				String onOff = "on";
				// change the item in the led table
				if (new_node.mode == 0)
					onOff = "off";
				Led_tab.led_table_model.setValueAt(onOff, index, 3);
				Led_tab.led_table_model.setValueAt(new_node.dimming, index, 4);
				Led_tab.led_table_model.setValueAt(new_node.priority, index, 5);
				// add the power consumption data to the power consumption
				// list
				Led_tab.device_db.led_list.get(index).value_list.remove(0);
				Led_tab.device_db.led_list.get(index).value_list.add((double) value);
				return true;
			}
		}
		return false;
	}

}