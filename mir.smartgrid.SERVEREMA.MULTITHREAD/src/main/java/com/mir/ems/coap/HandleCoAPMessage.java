package com.mir.ems.coap;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mir.ems.GUI.MainFrame;
import com.mir.ems.database.item.DeviceClass;
import com.mir.ems.database.item.EMAClass;
import com.mir.ems.database.item.GatewayClass;
import com.mir.ems.database.item.GeneratorClass;
import com.mir.ems.database.item.RecloserClass;
import com.mir.ems.database.item.ResourceClass;
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

public class HandleCoAPMessage extends Thread {
	public static HashMap<String, ESS_values> essHashMap;
	public static HashMap<String, PV_values> pvHashMap;
	public static boolean essValueCheck = false;
	public static boolean pvValueCheck = false;

	public static HashMap<String, Recloser_values> recloserHashMap;
	public static HashMap<String, Resource_values> resourceHashMap;
	public static boolean recloserValueCheck = false;
	public static boolean resourceValueCheck = false;

	public static InetAddress client_ip;
	public ESS_values ess_Value;
	public PV_values pv_Value;
	public Recloser_values recloser_value;
	public Resource_values resource_value;
	public static String Path;
	public static String Text;

	public static double capacity;

	public HandleCoAPMessage(String path, CoapExchange exchange) {
		Path = path;
		Text = exchange.getRequestText();
		String[] p_token = Path.split("/");

		client_ip = exchange.getSourceAddress();
		if (p_token[0].equals("info") || p_token[0].equals("powermargin")) {
			try {
				System.out.println("info");
				InfoThread info;
				info = new InfoThread();
				info.start();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} else if (p_token[0].equals("PVinit") || p_token[0].equals("PV")) {
			try {
				System.out.println("PV Generator");
				GeneratorThread gen;
				gen = new GeneratorThread();
				gen.start();
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if (p_token[0].equals("ESSinit") || p_token[0].equals("ESS")) {
			try {
				System.out.println("ESS Storage");
				StorageThread storage = new StorageThread();
				storage.start();
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else if (p_token[0].equals("negotiation")) {
			try {
				System.out.println("Negotiation Request!");
				Negotiation nego;
				nego = new Negotiation();
				nego.start();
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if (p_token[0].equals("DevDisconnected")) {
			try {
				System.out.println("Detect Device Disconnection!");
				Disconnect discon = new Disconnect();
				discon.start();
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else if (p_token[0].equals("connect")) {
			Connect con = new Connect();
			con.start();
		} else if (p_token[0].equals("load_profile")) {

		} else if (p_token[0].equals("active_energy")) {
			ActiveEnergy activeEnergy = new ActiveEnergy();
			activeEnergy.start();
		}
	}

	class ActiveEnergy extends Thread {
		public void run() {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonMsg;
			try {
				jsonMsg = (JSONObject) jsonParser.parse(Text);
				String ema = (String) jsonMsg.get("GW");

				String[] emaID = ema.split("/");
				int emaNum = Integer.parseInt(emaID[1]);

				String devID = (String) jsonMsg.get("device_id");
				int energyValue = Integer.parseInt(jsonMsg.get("active_energy").toString());

				SmartMeterClass new_node = new SmartMeterClass(emaNum, devID, energyValue);

				if (nodeIsExist(new_node, energyValue) == false) {
					new_node.value_list.set(new_node.value_list.size() - 1, (double) 0.0);
					SmartMeter_tab.smartMeter_db.smartMeter_list.add(new_node);
					SmartMeter_tab.modify_meter_table();
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public boolean nodeIsExist(SmartMeterClass new_node, double energyValue) {
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
	}

	class Connect extends Thread {
		public void run() {
			int gwNum = Integer.parseInt(Text);
			String EMAname = "EMA" + Text;

			Thread thread = new Thread();
			try {

				thread.sleep(6);
				double resource = 0.0, threshold = 0.0;
				EMAClass new_node = new EMAClass(EMAname, "CoAP", resource, threshold);

				if (nodeIsExist_EMA(new_node, resource) == false) {
					new_node.value_list.set(new_node.value_list.size() - 1, (double) 0.0);
					EMA_tab.gateway_db.ema_list.add(new_node);
					EMA_tab.modify_EMA_table();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public boolean nodeIsExist_EMA(EMAClass new_node, double resource) {
			int emaListSize;
			int index = -1;

			emaListSize = EMA_tab.gateway_db.ema_list.size();

			for (int i = 0; i < emaListSize; i++) {
				if (EMA_tab.gateway_db.ema_list.get(i).emaName == new_node.emaName) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				System.out.println("###########################");
				// EMA_tab.ema_table_model.setValueAt(new_node.emaName, index,
				// 1);
				//// EMA_tab.ema_table_model.setValueAt(new_node.protocol,
				// index,
				// 3);
				EMA_tab.ema_table_model.setValueAt(new_node.resource, index, 3);
				EMA_tab.ema_table_model.setValueAt(new_node.threshold, index, 4);

				EMA_tab.gateway_db.ema_list.get(index).value_list.remove(0);
				EMA_tab.gateway_db.ema_list.get(index).value_list.add((double) resource);
				// Led_tab.device_db.led_list.get(index).value_list.add((double)
				// value);

				return true;
			}

			return false;
		}
	}

	class InfoThread extends Thread {
		String[] t_token = Path.split("/");
		String[] m_token = Text.split("/");

		public void run() {
			System.out.println("t_token length: " + t_token.length);
			if (t_token[0].equals("powermargin")) {
				System.out.println("Information Arrived.");

				System.out.println("get Gateway Information");
				int gateway_id = Integer.parseInt(m_token[1]);

				System.out.println("get Gateway Information about Consumption & Threshold");

				String EMAname = "EMA" + gateway_id;
				double currentPower = Double.parseDouble(m_token[2]);
				double threshold = Double.parseDouble(m_token[3]);

				EMAClass new_node = new EMAClass(EMAname, "CoAP", currentPower, threshold);

				if (nodeIsExist_EMA(new_node, threshold) == false) {
					new_node.value_list.set(new_node.value_list.size() - 1, (double) 0.0);
					EMA_tab.gateway_db.ema_list.add(new_node);
					EMA_tab.modify_EMA_table();

				}

				if (MainFrame.gateway_db.gateway_list.containsKey(gateway_id)) {
					MainFrame.gateway_db.gateway_list.get(gateway_id).threshold = (int) threshold;
					GatewaySettingPanel.modify_gateway_table();
				}

			} else if (t_token[0].equals("info")) {

				System.out.println("(info)get Gateway Information");
				// int gateway_id = Integer.parseInt(m_token[1]);

				String emaID = m_token[1];
				//
				// if (MainFrame.gateway_db.gateway_list.containsKey(emaID)) {
				// if
				// (!MainFrame.gateway_db.gateway_list.get(emaID).ip_addr.equals(client_ip))
				// MainFrame.gateway_db.gateway_list.get(emaID).ip_addr =
				// client_ip;
				// if (MainFrame.gateway_db.gateway_list.get(emaID).port_num !=
				// 5683)
				// MainFrame.gateway_db.gateway_list.get(emaID).port_num = 5683;
				// } else {
				// MainFrame.gateway_db.addValue(emaID, 2, client_ip, 5683,
				// "CoAP gateway" + emaID, 1, 101,
				// "class room", 0);
				// // port, building name, floor, room num, room name,
				// // threshold
				//
				// // add new gateway to the list
				// GatewaySettingPanel.modify_gateway_table();
				// }
				// // checking the device existence and managing the device
				// // com.mir.ems.database
				// if (m_token[2].equals("dev")) {
				System.out.println("get device information");
				String device_id = m_token[3];
				int state = Integer.parseInt(m_token[4]);
				int dimming = Integer.parseInt(m_token[5]);
				int value = Integer.parseInt(m_token[6]);
				int priority = Integer.parseInt(m_token[7]);
				String deviceName = "CoAP Device" + device_id;
				/*
				 * DeviceClass new_node = new DeviceClass(device_id, gateway_id,
				 * type, deviceName, mode, dimming, priority);
				 */

				System.err.println("CLIENT_IP");
				System.err.println(client_ip);
				System.err.println("--");

				global.devManger.put(device_id, new DeviceClass(1, device_id, emaID, 1, deviceName, state, dimming,
						priority, client_ip.toString(), null, value));

//				DeviceClass new_node = new DeviceClass(1, device_id, emaID, 1, deviceName, state, dimming, priority,
//						client_ip.toString(), null);
//				if (nodeIsExist(new_node, value) == false) {// value媛� �엫�떆
//					System.out.println("add new note");
//					new_node.value_list.set(new_node.value_list.size() - 1, (double) value);// value媛�
//																							// �엫�떆
//					// if (1 == 1) {
//					Led_tab.device_db.led_list.add(new_node);
//					Led_tab.modify_LED_table();
//				}
				// }
			}
		}

		public boolean nodeIsExist_EMA(EMAClass new_node, double resource) {
			int emaListSize;
			int index = -1;

			emaListSize = EMA_tab.gateway_db.ema_list.size();
			System.out.println("bbbb");
			System.out.println(emaListSize);
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

		public boolean nodeIsExist(DeviceClass new_node, int value) {
			int deviceListSize;
			int index = -1;

			if (new_node.type == 1) { // LED
				deviceListSize = Led_tab.device_db.other_list.size();
				System.out.println(deviceListSize);
				for (int i = 0; i < deviceListSize; i++) { // check the
															// existence of
															// the node in other
															// device table

					if (Led_tab.device_db.other_list.get(i).node_id == new_node.node_id
							&& Led_tab.device_db.other_list.get(i).room_id == new_node.room_id) {
						Led_tab.device_db.other_list.remove(i);
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
						System.out.println("2-1");
						break;
					}
				}
				System.out.println(index);
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
			} else { // other device
				deviceListSize = Led_tab.device_db.led_list.size();
				for (int i = 0; i < deviceListSize; i++) { // check the
															// existence of
															// the node in LED
															// table
					if (Led_tab.device_db.led_list.get(i).node_id == new_node.node_id
							&& Led_tab.device_db.led_list.get(i).room_id == new_node.room_id) {
						Led_tab.device_db.led_list.remove(i);
						Led_tab.led_table_model.removeRow(i);
						break;
					}
				}
				deviceListSize = Led_tab.device_db.other_list.size();
				for (int i = 0; i < deviceListSize; i++) { // get the index of
															// the
															// node in the list
					if (Led_tab.device_db.other_list.get(i).node_id == new_node.node_id
							&& Led_tab.device_db.other_list.get(i).room_id == new_node.room_id) {
						index = i;
						break;
					}
				}
			}
			return false;

		}
	}

	/*
	 * ESS
	 */
	class StorageThread extends Thread {

		public String name;

		public double chargedenergy;
		public double power;

		public String ackMsg;

		// public String getAckMsg() {
		// return ackMsg;
		// }
		//
		// public void setAckMsg(String topic_value, int gwNum, int storage_id,
		// double power, int mode,
		// int state, double changedEnergy, int capacity, double soc, double
		// volt, double hz, int priority) {
		// ackMsg = topic_value;
		// this.ackMsg = ackMsg;
		// }

		@Override
		public void run() {
			JSONParser jp = new JSONParser();
			JSONObject msg_json;
			try {
				msg_json = (JSONObject) jp.parse(Text);
				double power = Double.parseDouble((msg_json.get("Power").toString()));
				int mode = Integer.parseInt(msg_json.get("Mode").toString());
				int state = Integer.parseInt(msg_json.get("State").toString());
				double changedEnergy = Double.parseDouble(msg_json.get("ChangedEnergy").toString());
				int capacity = Integer.parseInt(msg_json.get("Capacity").toString());
				double soc = Double.parseDouble((msg_json.get("Soc").toString()));
				double volt = Double.parseDouble((msg_json.get("Volt").toString()));
				double hz = Double.parseDouble((msg_json.get("Hz").toString()));
				int priority = Integer.parseInt(msg_json.get("priority").toString());
				int gwNum = Integer.parseInt((msg_json.get("GW").toString()));
				int storage_id = Integer.parseInt((msg_json.get("dev").toString()));
				// TODO Auto-generated method stub
				if (Path.equals("ESSConnect")) {
					System.out.println("ESS Connect Message");
					name = "ESS" + storage_id;
					StorageClass new_node = new StorageClass(2, gwNum, storage_id, name, power, mode, state,
							changedEnergy, capacity, soc, volt, hz, priority);

					if (nodeIsExist(new_node, power) == false) {
						new_node.value_list.set(new_node.value_list.size() - 1, (double) power);
						Ess_tab.storage_db.storage_list.add(new_node);
						Ess_tab.modify_storage_table();

					}

				} else if (Path.equals("ESS")) {
					name = "ESS" + storage_id;
					StorageClass new_node = new StorageClass(2, gwNum, storage_id, name, power, mode, state,
							changedEnergy, capacity, soc, volt, hz, priority);

					if (nodeIsExist(new_node, power) == false) {// value媛� �엫�떆

						new_node.value_list.set(new_node.value_list.size() - 1, (double) power);// value媛�
						// �엫�떆
						// if (1 == 1) {
						Ess_tab.storage_db.storage_list.add(new_node);
						Ess_tab.modify_storage_table();
					}
				} else if (Path.equals("ESSDisconnect")) {
					int essListSize = Ess_tab.storage_db.storage_list.size();

					for (int i = 0; i < essListSize; i++) {
						if (Ess_tab.storage_db.storage_list.get(i).storage_id == storage_id) {
							Ess_tab.ess_table_model.removeRow(i);
							Ess_tab.storage_db.storage_list.remove(i);
							essListSize--;
							Ess_tab.modify_storage_table();
						}
					}
				} else if (Path.equals("ESSinit")) {
					name = "ESS" + storage_id;
					StorageClass new_node = new StorageClass(2, gwNum, storage_id, name, power, mode, state,
							changedEnergy, capacity, soc, volt, hz, priority);

					if (nodeIsExist(new_node, power) == false) {
						new_node.value_list.set(new_node.value_list.size() - 1, (double) power);
						Ess_tab.storage_db.storage_list.add(new_node);
						Ess_tab.modify_storage_table();
					}
					essHashMap = new HashMap<String, ESS_values>();
					ess_Value = new ESS_values(2, gwNum, storage_id, name, power, mode, state, changedEnergy, capacity,
							soc, volt, hz);
					essHashMap.put(name, ess_Value);
					setEssHashMap(essHashMap);
					setEssValueCheck(true);
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public boolean nodeIsExist(StorageClass new_storage, double value) {
			int storageListSize;
			int index = -1;

			storageListSize = Ess_tab.storage_db.storage_list.size();

			// System.out.println("STORAGELISTSIZE::"+storageListSize);
			for (int i = 0; i < storageListSize; i++) {
				if (Ess_tab.storage_db.storage_list.get(i).storage_id == new_storage.storage_id
						&& Ess_tab.storage_db.storage_list.get(i).gwNum == new_storage.gwNum) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				String state = "Wait";

				if (new_storage.mode == 1)
					state = "Charging";

				else if (new_storage.mode == 2)
					state = "Discharging";

				// String soc = (new_node.changedEnergy/ new_node.capacity)
				// *
				// 100 + "%";
				Ess_tab.ess_table_model.setValueAt(new_storage.state, index, 3);
				Ess_tab.ess_table_model.setValueAt(new_storage.soc, index, 4);
				Ess_tab.ess_table_model.setValueAt(new_storage.power, index, 5);
				Ess_tab.ess_table_model.setValueAt(new_storage.volt, index, 6);
				Ess_tab.ess_table_model.setValueAt(new_storage.hz, index, 7);
				Ess_tab.ess_table_model.setValueAt(new_storage.priority, index, 8);

				Ess_tab.storage_db.storage_list.get(index).value_list.remove(0);
				Ess_tab.storage_db.storage_list.get(index).value_list.add((double) power);
				return true;
			}

			return false;
		}
	}

	/*
	 * PV
	 */
	class GeneratorThread extends Thread {

		public String name;

		@Override
		public void run() {
			JSONParser jp = new JSONParser();
			JSONObject msg_json;

			try {
				msg_json = (JSONObject) jp.parse(Text);
				int gwNum = Integer.parseInt((msg_json.get("GW").toString()));
				int devNum = Integer.parseInt((msg_json.get("dev").toString()));

				double power = Double.parseDouble((msg_json.get("Power").toString()));
				int mode = Integer.parseInt(msg_json.get("Mode").toString());
				int priority = Integer.parseInt(msg_json.get("priority").toString());
				if (Path.equals("PVConnect")) {

					name = "PV" + devNum;
					GeneratorClass new_gen = new GeneratorClass(2, gwNum, devNum, name, mode, power, priority);

					if (nodeIsExist(new_gen, power)) {
						new_gen.value_list.set(new_gen.value_list.size() - 1, (double) power);// value媛�
																								// �엫�떆
						// if (1 == 1) {
						Pv_tab.generator_db.generator_list.add(new_gen);

						Pv_tab.modify_generator_table();
					}

				} else if (Path.equals("PVinit")) {

					name = "PV" + devNum;
					GeneratorClass new_gen = new GeneratorClass(2, gwNum, devNum, name, mode, power, priority);

					if (nodeIsExist(new_gen, power) == false) {// value媛� �엫�떆
						System.out.println("add new generator");
						new_gen.value_list.set(new_gen.value_list.size() - 1, (double) power);// value媛�
																								// �엫�떆
						// if (1 == 1) {
						Pv_tab.generator_db.generator_list.add(new_gen);
						Pv_tab.modify_generator_table();
					}
				} else if (Path.equals("PV")) {

					name = "PV" + devNum;
					GeneratorClass new_gen = new GeneratorClass(2, gwNum, devNum, name, mode, power, priority);

					/*
					 * Database will be added
					 */

					if (nodeIsExist(new_gen, power) == false) {
						Pv_tab.generator_db.generator_list.add(new_gen);
						Pv_tab.modify_generator_table();
					}
					pvHashMap = new HashMap<String, PV_values>();
					pv_Value = new PV_values(2, gwNum, devNum, name, mode, power, priority);
					pvHashMap.put(name, pv_Value);
					setPvHashMap(pvHashMap);
					setPvValueCheck(true);
				} else if (Path.equals("PVDisconnect")) {

					System.out.println("PV DisCONNECT MSG");

					System.out.println("ESS DisConnect Message");

					int pvListSize = Pv_tab.generator_db.generator_list.size();

					for (int i = 0; i < pvListSize; i++) {
						if (Pv_tab.generator_db.generator_list.get(i).device_id == devNum) {
							Pv_tab.pv_table_model.removeRow(i);
							Pv_tab.generator_db.generator_list.remove(i);
							pvListSize--;
							Pv_tab.modify_generator_table();
						}
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated method stub

		}

		public boolean nodeIsExist(GeneratorClass new_gen, double value) {
			int genListSize;
			int index = -1;

			genListSize = Pv_tab.generator_db.generator_list.size();
			for (int i = 0; i < genListSize; i++) { // get the index of
													// the
													// node in the list
				if (Pv_tab.generator_db.generator_list.get(i).device_id == new_gen.device_id
						&& Pv_tab.generator_db.generator_list.get(i).gateway_id == new_gen.gateway_id) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				String state = "On";
				System.out.println("PV state Change");

				if (new_gen.mode == 0)
					state = "Off";

				Pv_tab.pv_table_model.setValueAt(new_gen.mode, index, 3);
				Pv_tab.pv_table_model.setValueAt(new_gen.power, index, 4);
				Pv_tab.pv_table_model.setValueAt(new_gen.priority, index, 5);

				// add the power consumption data to the power consumption
				// list
				Pv_tab.generator_db.generator_list.get(index).value_list.remove(0);
				Pv_tab.generator_db.generator_list.get(index).value_list.add((double) value);
				return true;
			}

			return false;
		}
	}

	class ResourceThread extends Thread {

		public String name;

		@Override
		public void run() {
			JSONParser jp = new JSONParser();
			JSONObject msg_json;

			try {
				msg_json = (JSONObject) jp.parse(Text);

				int gwNum = Integer.parseInt((msg_json.get("GW").toString()));
				int devNum = Integer.parseInt((msg_json.get("dev").toString()));
				double power = Double.parseDouble((msg_json.get("Power").toString()));
				int mode = Integer.parseInt(msg_json.get("Mode").toString());
				int priority = Integer.parseInt(msg_json.get("Mode").toString());

				if (Path.equals("RESOURCEConnect")) {

					name = "RESOURCE" + devNum;
					ResourceClass new_node = new ResourceClass(2, gwNum, devNum, name, power, mode, priority);

					if (nodeIsExist(new_node, power) == false) {
						new_node.value_list.set(new_node.value_list.size() - 1, (double) power);
						Resource_tab.resource_db.resource_list.add(new_node);
						Resource_tab.modify_resource_table();
					}

				} else if (Path.equals("RESOURCE")) {

					name = "RECLOSER" + devNum;

					ResourceClass new_node = new ResourceClass(2, gwNum, devNum, name, power, mode, priority);

					if (nodeIsExist(new_node, power) == false) {
						new_node.value_list.set(new_node.value_list.size() - 1, (double) power);
						Resource_tab.resource_db.resource_list.add(new_node);
						Resource_tab.modify_resource_table();
					}

					resourceHashMap = new HashMap<String, Resource_values>();
					resource_value = new Resource_values(2, gwNum, devNum, name, power, mode, priority);
					resourceHashMap.put(name, resource_value);
					setResourceHashMap(resourceHashMap);
					setResourceValueCheck(true);

				} else if (Path.equals("RESOURCEDisconnect")) {

					int resourceListSize = Resource_tab.resource_db.resource_list.size();

					for (int i = 0; i < resourceListSize; i++) {
						if (Resource_tab.resource_db.resource_list.get(i).resource_id == devNum) {
							Resource_tab.resource_table_model.removeRow(i);
							Resource_tab.resource_db.resource_list.remove(i);
							resourceListSize--;
							Resource_tab.modify_resource_table();
						}
					}
				} else if (Path.equals("RESOURCEinit")) {

					name = "RECLOSER" + devNum;

					ResourceClass new_node = new ResourceClass(2, gwNum, devNum, name, power, mode, priority);

					if (nodeIsExist(new_node, power) == false) {
						new_node.value_list.set(new_node.value_list.size() - 1, (double) power);
						Resource_tab.resource_db.resource_list.add(new_node);
						Resource_tab.modify_resource_table();
					}

					resourceHashMap = new HashMap<String, Resource_values>();
					resource_value = new Resource_values(2, gwNum, devNum, name, power, mode, priority);
					resourceHashMap.put(name, resource_value);
					setResourceHashMap(resourceHashMap);
					setResourceValueCheck(true);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated method stub

		}

		public boolean nodeIsExist(ResourceClass new_gen, double value) {
			int recloserListSize;
			int index = -1;

			recloserListSize = Resource_tab.resource_db.resource_list.size();
			// System.out.println("STORAGELISTSIZE::"+storageListSize);
			for (int i = 0; i < recloserListSize; i++) {
				if (Resource_tab.resource_db.resource_list.get(i).resource_id == new_gen.resource_id
						&& Resource_tab.resource_db.resource_list.get(i).gateway_id == new_gen.gateway_id) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				// String soc = (new_node.changedEnergy/ new_node.capacity) *
				// 100 + "%";
				Resource_tab.resource_table_model.setValueAt(new_gen.mode, index, 3);
				Resource_tab.resource_table_model.setValueAt(new_gen.power, index, 4);
				Resource_tab.resource_table_model.setValueAt(new_gen.priority, index, 5);

				Resource_tab.resource_db.resource_list.get(index).value_list.remove(0);
				Resource_tab.resource_db.resource_list.get(index).value_list.add((double) value);
				return true;
			}
			return false;
		}
	}

	class RecloserThread extends Thread {

		public String name;

		@Override
		public void run() {
			JSONParser jp = new JSONParser();
			JSONObject msg_json;

			try {
				msg_json = (JSONObject) jp.parse(Text);
				int gwNum = Integer.parseInt((msg_json.get("GW").toString()));
				int devNum = Integer.parseInt((msg_json.get("dev").toString()));
				double power = Double.parseDouble((msg_json.get("Power").toString()));
				int mode = Integer.parseInt(msg_json.get("Mode").toString());
				int priority = Integer.parseInt(msg_json.get("Mode").toString());

				if (Path.equals("RECLOSERConnect")) {

					name = "RECLOSER" + devNum;

					RecloserClass new_node = new RecloserClass(2, gwNum, devNum, name, power, mode, priority);

					if (nodeIsExist(new_node, power) == false) {
						new_node.value_list.set(new_node.value_list.size() - 1, (double) power);
						Recloser_tab.recloser_db.recloser_list.add(new_node);
						Recloser_tab.modify_recloser_table();
					}

				} else if (Path.equals("RECLOSERDisconnect")) {

					int recloserListSize = Recloser_tab.recloser_db.recloser_list.size();

					for (int i = 0; i < recloserListSize; i++) {
						if (Recloser_tab.recloser_db.recloser_list.get(i).recloser_id == devNum) {
							Recloser_tab.recloser_table_model.removeRow(i);
							Recloser_tab.recloser_db.recloser_list.remove(i);
							recloserListSize--;
							Recloser_tab.modify_recloser_table();
						}
					}
				} else if (Path.equals("RECLOSERinit")) {
					name = "RECLOSER" + devNum;

					RecloserClass new_node = new RecloserClass(2, gwNum, devNum, name, power, mode, priority);
					if (nodeIsExist(new_node, power) == false) {
						Recloser_tab.recloser_db.recloser_list.add(new_node);
						Recloser_tab.modify_recloser_table();
					}

					recloserHashMap = new HashMap<String, Recloser_values>();
					recloser_value = new Recloser_values(2, gwNum, devNum, name, power, mode, priority);
					recloserHashMap.put(name, recloser_value);
					setRecloserHashMap(recloserHashMap);
					setRecloserValueCheck(true);

				} else if (Path.equals("RECLOSER")) {

					name = "RECLOSER" + devNum;

					RecloserClass new_node = new RecloserClass(2, gwNum, devNum, name, power, mode, priority);
					if (nodeIsExist(new_node, power) == false) {
						Recloser_tab.recloser_db.recloser_list.add(new_node);
						Recloser_tab.modify_recloser_table();
					}

					recloserHashMap = new HashMap<String, Recloser_values>();
					recloser_value = new Recloser_values(2, gwNum, devNum, name, power, mode, priority);
					recloserHashMap.put(name, recloser_value);
					setRecloserHashMap(recloserHashMap);
					setRecloserValueCheck(true);

				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated method stub

		}

		public boolean nodeIsExist(RecloserClass new_gen, double value) {
			int recloserListSize;
			int index = -1;

			recloserListSize = Recloser_tab.recloser_db.recloser_list.size();
			for (int i = 0; i < recloserListSize; i++) {
				if (Recloser_tab.recloser_db.recloser_list.get(i).recloser_id == new_gen.recloser_id
						&& Recloser_tab.recloser_db.recloser_list.get(i).gateway_id == new_gen.gateway_id) {
					index = i;
					break;
				}
			}
			if (index != -1) {

				Recloser_tab.recloser_table_model.setValueAt(new_gen.mode, index, 3);
				Recloser_tab.recloser_table_model.setValueAt(new_gen.power, index, 4);
				Recloser_tab.recloser_table_model.setValueAt(new_gen.priority, index, 5);

				Recloser_tab.recloser_db.recloser_list.get(index).value_list.remove(0);
				Recloser_tab.recloser_db.recloser_list.get(index).value_list.add((double) value);
				return true;
			}
			return false;
		}
	}

	class Negotiation extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ArrayList<GatewayClass> gatewayList = new ArrayList<GatewayClass>(
					MainFrame.gateway_db.gateway_list.values());
			int gatewaySize = gatewayList.size();

			String[] m_token = Text.split("/");
			byte[] b;
			boolean negotiation = false;

			String gatewayID = m_token[1];
			int request_val = Integer.parseInt(m_token[3]);

			for (int i = 0; i < gatewaySize; i++) {
				if (!gatewayList.get(i).gateway_id.equals(gatewayID)) {
					if ((9 * gatewayList.get(i).threshold / 10) - gatewayList.get(i).getMaxValue() > request_val) {
						try {
							String addr = gatewayList.get(i).ip_addr.toString() + ":5683";
							CoAPClient client = new CoAPClient(addr, "/nego_result", "ACCEPT".getBytes());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						negotiation = true;
						break;
					}
				}
			}
			if (negotiation == false) {
				try {
					// String addr =
					// gatewayList.get(gatewayID).ip_addr.toString() + ":5683";
					// CoAPClient client = new CoAPClient(addr, "/nego_result",
					// "DENIED".getBytes());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			gatewayList.clear();
		}
	}

	class Disconnect extends Thread {
		@Override
		public void run() {
			int deviceListSize = Led_tab.device_db.led_list.size();
			System.out.println(deviceListSize);
			System.out.println(Integer.parseInt(Text));
			for (int i = 0; i < deviceListSize; i++) {
				System.out.println(deviceListSize);
				if (Led_tab.device_db.led_list.get(i).node_id == Integer.parseInt(Text)) {
					Led_tab.device_db.led_list.remove(i);
					System.out.println("Remove!");
					Led_tab.led_table_model.removeRow(i);
					Led_tab.modify_LED_table();
				}

			}
		}
	}

	public static HashMap<String, ESS_values> getEssHashMap() {
		return essHashMap;
	}

	public static void setEssHashMap(HashMap<String, ESS_values> essHashMap) {
		HandleCoAPMessage.essHashMap = essHashMap;
	}

	public static HashMap<String, PV_values> getPvHashMap() {
		return pvHashMap;
	}

	public static void setPvHashMap(HashMap<String, PV_values> pvHashMap) {
		HandleCoAPMessage.pvHashMap = pvHashMap;
	}

	public static boolean isEssValueCheck() {
		return essValueCheck;
	}

	public static void setEssValueCheck(boolean essValueCheck) {
		HandleCoAPMessage.essValueCheck = essValueCheck;
	}

	public static boolean isPvValueCheck() {
		return pvValueCheck;
	}

	public static void setPvValueCheck(boolean pvValueCheck) {
		HandleCoAPMessage.pvValueCheck = pvValueCheck;
	}

	public static HashMap<String, Recloser_values> getRecloserHashMap() {
		return recloserHashMap;
	}

	public static void setRecloserHashMap(HashMap<String, Recloser_values> recloserHashMap) {
		HandleCoAPMessage.recloserHashMap = recloserHashMap;
	}

	public static HashMap<String, Resource_values> getResourceHashMap() {
		return resourceHashMap;
	}

	public static void setResourceHashMap(HashMap<String, Resource_values> resourceHashMap) {
		HandleCoAPMessage.resourceHashMap = resourceHashMap;
	}

	public static boolean isRecloserValueCheck() {
		return recloserValueCheck;
	}

	public static void setRecloserValueCheck(boolean recloserValueCheck) {
		HandleCoAPMessage.recloserValueCheck = recloserValueCheck;
	}

	public static boolean isResourceValueCheck() {
		return resourceValueCheck;
	}

	public static void setResourceValueCheck(boolean resourceValueCheck) {
		HandleCoAPMessage.resourceValueCheck = resourceValueCheck;
	}

}
