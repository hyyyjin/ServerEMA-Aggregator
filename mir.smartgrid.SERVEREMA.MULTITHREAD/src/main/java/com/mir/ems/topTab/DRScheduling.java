package com.mir.ems.topTab;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.jfree.data.time.Minute;
import org.json.simple.JSONObject;

import com.mir.ems.GUI.MainFrame;
import com.mir.ems.Graph.DRSchedulingGraph;
import com.mir.ems.beep.BeepSound;
import com.mir.ems.coap.CoAPClient;
import com.mir.ems.coap.CoAPObserverSubPath;
import com.mir.ems.database.item.EMAP_CoAP_EMA_DR;
import com.mir.ems.globalVar.global;
import com.mir.ems.main.Connection;
import com.mir.ems.mqtt.HandleMqttMessage;
import com.mir.ems.mqtt.ProcessStatus;
//import com.mir.ems.mqtt.Mqtt.publishThread;
import java.awt.Color;

@SuppressWarnings("serial")
public class DRScheduling extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;

	Calendar calendar = Calendar.getInstance();

	public static String sYMD, eYMD, sTime, eTime;

	public static int indexOfEma;
	public String obsFlag;

	public static boolean obsEventFlag = false;

	int message_method;
	public static int value;
	public static int state = 0;

	public static String DR_MSG = null;
	public static int multiNum;

	public static int protocol;

	JSONObject drmsg = new JSONObject();

	public static LinkedList<Integer> gwNumQueue = new LinkedList<Integer>();

	public JComboBox<String> protocol_comboBox;

	public DRScheduling() {
		setBackground(Color.WHITE);
		// setBackgroundcO();
		setBounds(14, 60, 1491, 700);
		setLayout(null);

		JLabel lblNewLabel = new JLabel("EMA Scheduling");
		lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblNewLabel.setBounds(12, 545, 166, 21);
		add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("EMA ID");
		lblNewLabel_1.setBounds(477, 583, 55, 15);
		add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Start Time");
		lblNewLabel_2.setBounds(912, 586, 82, 15);
		add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("End Time");
		lblNewLabel_3.setBounds(912, 625, 57, 15);
		add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Value");
		lblNewLabel_4.setBounds(477, 625, 57, 15);
		add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("Protocol");
		lblNewLabel_5.setBounds(89, 580, 57, 15);
		add(lblNewLabel_5);

		JLabel lblNewLabel_6 = new JLabel("Type");
		lblNewLabel_6.setBounds(89, 619, 57, 15);
		add(lblNewLabel_6);

		JButton btnNewButton = new JButton("Set");
		btnNewButton.setBounds(1351, 621, 97, 23);
		add(btnNewButton);

		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(217, 616, 149, 21);

		comboBox.addItem("BroadCast");
		comboBox.addItem("obsDREvent");
		comboBox.addItem("obsDREvent : 3");
		comboBox.addItem("obsDREvent : 9");
		comboBox.addItem("obsDREvent : 20");

		// comboBox.addItem("Multicast");
		// comboBox.addItem("Unicast");

		comboBox.addItem("MultiCast : 3");
		comboBox.addItem("MultiCast : 9");
		comboBox.addItem("MultiCast : 20");
		comboBox.addItem("MultiCast : 3-pull");
		comboBox.addItem("MultiCast : 9-pull");
		comboBox.addItem("MultiCast : 20-pull");
		// comboBox.addItem("MultiCast : 1-pull");

		comboBox.addItem("Push");
		comboBox.addItem("Pull");
		add(comboBox);
		//
		// JComboBox<String> Addressing = new JComboBox<String>();
		// Addressing.setBounds(809, 94, 104, 21);
		// Addressing.addItem("BroadCast");
		// Addressing.addItem("MultiCast : 3");
		// Addressing.addItem("MultiCast : 9");
		// Addressing.addItem("MultiCast : 20");
		// Addressing.addItem("Push");
		// Addressing.addItem("Pull");

		String date;

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);

		if (month < 10 && day < 10) {
			date = Integer.toString(year) + "0" + Integer.toString(month) + "0" + Integer.toString(day);
		} else if (month < 10 && !(day < 10)) {
			date = Integer.toString(year) + "0" + Integer.toString(month) + Integer.toString(day);
		} else if (!(month < 10) && day < 10) {
			date = Integer.toString(year) + Integer.toString(month) + "0" + Integer.toString(day);
		} else {
			date = Integer.toString(year) + Integer.toString(month) + Integer.toString(day);
		}

		String start_hour, start_minute;
		String end_hour, end_minute;

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int e_minute = calendar.get(Calendar.MINUTE) + 1;

		start_hour = Integer.toString(hour);
		start_minute = Integer.toString(minute);

		end_hour = Integer.toString(hour);
		end_minute = Integer.toString(e_minute);

		textField = new JTextField();
		textField.setBounds(631, 577, 149, 21);
		add(textField);
		textField.setColumns(10);
		textField.setEditable(false);

		textField_1 = new JTextField();
		textField_1.setBounds(1034, 583, 82, 21);
		add(textField_1);
		textField_1.setColumns(10);

		textField_1.setText(date);

		textField_2 = new JTextField();
		textField_2.setBounds(1128, 583, 55, 21);
		add(textField_2);
		textField_2.setColumns(10);
		textField_2.setText(start_hour);

		textField_3 = new JTextField();
		textField_3.setBounds(1195, 583, 64, 21);
		add(textField_3);
		textField_3.setColumns(10);
		textField_3.setText(start_minute);

		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(1034, 619, 82, 21);
		add(textField_4);
		textField_4.setText(date);

		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(1128, 619, 55, 21);
		add(textField_5);
		textField_5.setText(end_hour);

		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(1195, 619, 64, 21);
		add(textField_6);
		textField_6.setText(end_minute);

		textField_7 = new JTextField();
		textField_7.setText("100");
		textField_7.setColumns(10);
		textField_7.setBounds(631, 619, 149, 21);
		add(textField_7);

		textField_8 = new JTextField();
		textField_8.setText(global.getProtocol_type_global());
		textField_8.setFont(new Font("Arial", Font.BOLD, 12));

		textField_8.setColumns(10);
		textField_8.setEditable(false);
		textField_8.setBounds(217, 577, 149, 21);
		add(textField_8);

		DRSchedulingGraph panel = new DRSchedulingGraph();
		panel.setBounds(12, 0, 1436, 529);
		add(panel);

		protocol_comboBox = new JComboBox<String>();
		protocol_comboBox.setBounds(217, 546, 149, 21);
		protocol_comboBox.addItem("MQTT");
		protocol_comboBox.addItem("COAP");

		add(protocol_comboBox);
		//
		// JLabel lblNewLabel_7 = new JLabel("New label");
		// panel.add(lblNewLabel_7);

		comboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (comboBox.getSelectedItem().toString().equals("BroadCast")) {
					textField.setEditable(false);
					message_method = 0;
				} else if (comboBox.getSelectedItem().toString().equals("Pull")) {
					message_method = 1;
					textField.setEditable(true);
					obsFlag = "non-obs";
				} else if (comboBox.getSelectedItem().toString().equals("Push")) {
					message_method = 2;
					textField.setEditable(true);
					obsFlag = "non-obs";

				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 3")) {
					textField.setEditable(false);
					message_method = 3;
				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 9")) {
					textField.setEditable(false);
					message_method = 9;
				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 20")) {
					textField.setEditable(false);
					message_method = 20;
				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 3-pull")) {
					textField.setEditable(false);
					message_method = 4;
				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 9-pull")) {
					textField.setEditable(false);
					message_method = 10;
				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 20-pull")) {
					textField.setEditable(false);
					message_method = 21;
				} else if (comboBox.getSelectedItem().toString().equals("obsDREvent")) {
					textField.setEditable(true);
					message_method = 22;
					obsFlag = "obs";
				} else if (comboBox.getSelectedItem().toString().equals("obsDREvent : 3")) {
					textField.setEditable(false);
					message_method = 23;
					obsFlag = "obs";

				} else if (comboBox.getSelectedItem().toString().equals("obsDREvent : 9")) {
					textField.setEditable(false);
					message_method = 24;
					obsFlag = "obs";

				} else if (comboBox.getSelectedItem().toString().equals("obsDREvent : 20")) {
					textField.setEditable(true);
					message_method = 25;
					obsFlag = "obs";

				} else if (comboBox.getSelectedItem().toString().equals("MultiCast : 1-pull")) {
					textField.setEditable(false);
					message_method = 1;
				}
			}
		});

		sYMD = textField_1.getText();
		sTime = textField_2.getText() + textField_3.getText();

		eYMD = textField_4.getText();
		eTime = textField_5.getText() + textField_6.getText();

		btnNewButton.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String ema_name = textField.getText();
				value = Integer.parseInt(textField_7.getText());
				// indexOfEma = Integer.parseInt(ema_name);

				String process_value;

				// if (global.getProtocol_type_global().equals("MQTT")) {
				if (protocol_comboBox.getSelectedItem().toString().equals("MQTT")) {
					protocol = 1;
					// ProcessStatus value = new
					// ProcessStatus().getProcess_value();
					process_value = ProcessStatus.getProcess_value();

					if (message_method == 0) {

						drmsg.put("Service", "DistributeEvent");
						drmsg.put("StartYMD", sYMD);
						drmsg.put("StartTime", sTime);
						drmsg.put("EndYMD", eYMD);
						drmsg.put("EndTime", eTime);
						drmsg.put("Value", value);
						drmsg.put("Response", 200);
						drmsg.put("RequestID", 1);
						drmsg.put("EventID", 1);
						drmsg.put("ModificationNumber", 0);
						drmsg.put("TargetVEN", "MIR_VEN" + String.valueOf(indexOfEma));
						drmsg.put("Responsedescription", "MIR");
						drmsg.put("OptType", "optIn");

						String p_topic = "gw/" + "pushall" + "/DistributeEvent";
						String m_msg = drmsg.toJSONString();

						// Runnable p = new publishThread(p_topic, 0,
						// m_msg.getBytes());
						// Thread p7 = new Thread(p);
						// p7.start();
					} else if (message_method == 1) {

						if (global.emaProtocolCoAP_EventFlag.containsKey(ema_name)) {

							if (global.emaProtocolCoAP.get(ema_name).isPullModel()) {
								global.emaProtocolCoAP_EventFlag.get(textField.getText()).setEventFlag(true)
										.setStartYMD(Integer.parseInt(sYMD))
										.setStartTime(Integer.parseInt(sTime + "11")).setEndYMD(Integer.parseInt(eYMD))
										.setEndTime(Integer.parseInt(eTime + "11"))
										.setThreshold(Double.parseDouble(textField_7.getText()));
							} else {

								System.err.println(ema_name + " is not supported Pull Model");
								return;
							}

						}

						// state = 1;
						// JSONObject drmsg = new JSONObject();
						//
						// drmsg.put("Service", "oadrDistributeEvent");
						// drmsg.put("StartYMD", sYMD);
						// drmsg.put("StartTime", sTime);
						// drmsg.put("EndYMD", eYMD);
						// drmsg.put("EndTime", eTime);
						// drmsg.put("Value", value);
						// drmsg.put("Response", value);
						// drmsg.put("RequestID", 1);
						// drmsg.put("EventID", 1);
						// drmsg.put("ModificationNumber", 0);
						// drmsg.put("TargetVEN", "MIR_VEN" +
						// String.valueOf(indexOfEma));
						// drmsg.put("Responsedescription", "MIR");
						// drmsg.put("OptType", "optIn");
						//
						// String m_msg = drmsg.toJSONString();
						// DR_MSG = m_msg;
					} else if (message_method == 4) {
						for (int i = 1; i < message_method; i++) {
							gwNumQueue.add(i);
							// System.out.println("���ڳ���");
							// System.out.println(gwNumQueue.get(i));
						}
						state = 2; // multicast flag

					} else if (message_method == 10) {
						for (int i = 1; i < message_method; i++) {
							gwNumQueue.add(i);
						}
						state = 2; // multicast flag

					} else if (message_method == 21) {
						for (int i = 1; i < message_method; i++) {
							gwNumQueue.add(i);
						}
						state = 2; // multicast flag

					} else if (message_method == 2) {

						if (!global.emaProtocolCoAP.get(ema_name).isPullModel()) {

							global.initiater.eventOccur(ema_name, 1, Integer.parseInt(sYMD),
									Integer.parseInt(sTime + "11"), Integer.parseInt(eYMD),
									Integer.parseInt(eTime + "11"), Double.parseDouble(textField_7.getText()));
						} else {

							System.err.println(ema_name + " is not supported Push Model");
							return;
						}
						//
						// indexOfEma = Integer.parseInt(ema_name);
						//
						// drmsg.put("Service", "DistributeEvent");
						// drmsg.put("StartYMD", sYMD);
						// drmsg.put("StartTime", sTime);
						// drmsg.put("EndYMD", eYMD);
						// drmsg.put("EndTime", eTime);
						// drmsg.put("Value", value);
						// drmsg.put("Response", 200);
						// drmsg.put("RequestID", 1);
						// drmsg.put("EventID", 1);
						// drmsg.put("ModificationNumber", 0);
						// drmsg.put("TargetVEN", "MIR_VEN" +
						// String.valueOf(indexOfEma));
						// drmsg.put("Responsedescription", "MIR");
						// drmsg.put("OptType", "optIn");
						//
						// String p_topic = "gw/" + String.valueOf(indexOfEma) +
						// "/oadr" + "/DistributeEvent";
						// String m_msg = drmsg.toJSONString();

						// Runnable p = new publishThread(p_topic, 0,
						// m_msg.getBytes());
						// Thread p7 = new Thread(p);
						// p7.start();
					} else if (message_method == 3) {
						drmsg.put("Service", "DistributeEvent");
						drmsg.put("StartYMD", sYMD);
						drmsg.put("StartTime", sTime);
						drmsg.put("EndYMD", eYMD);
						drmsg.put("EndTime", eTime);
						drmsg.put("Value", value);
						drmsg.put("Response", 200);
						drmsg.put("RequestID", 1);
						drmsg.put("EventID", 1);
						drmsg.put("ModificationNumber", 0);
						drmsg.put("TargetVEN", "MIR_VEN" + String.valueOf(indexOfEma));
						drmsg.put("Responsedescription", "MIR");
						drmsg.put("OptType", "optIn");
						//
						// for (int i = 1; i <= message_method; i++) {
						// String p_topic = "gw/" + i + "/oadr" +
						// "/DistributeEvent";
						// String m_msg = drmsg.toJSONString();
						//
						// Runnable p = new publishThread(p_topic, 0,
						// m_msg.getBytes());
						// Thread p7 = new Thread(p);
						// p7.start();
						// }
					} else if (message_method == 9) {
						drmsg.put("Service", "DistributeEvent");
						drmsg.put("StartYMD", sYMD);
						drmsg.put("StartTime", sTime);
						drmsg.put("EndYMD", eYMD);
						drmsg.put("EndTime", eTime);
						drmsg.put("Value", value);
						drmsg.put("Response", 200);
						drmsg.put("RequestID", 1);
						drmsg.put("EventID", 1);
						drmsg.put("ModificationNumber", 0);
						drmsg.put("TargetVEN", "MIR_VEN" + String.valueOf(indexOfEma));
						drmsg.put("Responsedescription", "MIR");
						drmsg.put("OptType", "optIn");

						for (int i = 1; i <= message_method; i++) {
							String p_topic = "gw/" + i + "/oadr" + "/DistributeEvent";
							String m_msg = drmsg.toJSONString();

							// Runnable p = new publishThread(p_topic, 0,
							// m_msg.getBytes());
							// Thread p7 = new Thread(p);
							// p7.start();
						}
					} else if (message_method == 20) {
						drmsg.put("Service", "DistributeEvent");
						drmsg.put("StartYMD", sYMD);
						drmsg.put("StartTime", sTime);
						drmsg.put("EndYMD", eYMD);
						drmsg.put("EndTime", eTime);
						drmsg.put("Value", value);
						drmsg.put("Response", 200);
						drmsg.put("RequestID", 1);
						drmsg.put("EventID", 1);
						drmsg.put("ModificationNumber", 0);
						drmsg.put("TargetVEN", "MIR_VEN" + String.valueOf(indexOfEma));
						drmsg.put("Responsedescription", "MIR");
						drmsg.put("OptType", "optIn");

						for (int i = 1; i <= message_method; i++) {
							String p_topic = "gw/" + i + "/oadr" + "/DistributeEvent";
							String m_msg = drmsg.toJSONString();

							// Runnable p = new publishThread(p_topic, 0,
							// m_msg.getBytes());
							// Thread p7 = new Thread(p);
							// p7.start();
						}
					}

				}

				else if (protocol_comboBox.getSelectedItem().toString().equals("COAP")) {

					// else if (global.getProtocol_type_global().equals("CoAP"))
					// {
					protocol = 2;
					// System.out.println("�����?");
					if (message_method == 0) {

						drmsg.put("Service", "DistributeEvent");
						drmsg.put("StartYMD", sYMD);
						drmsg.put("StartTime", sTime);
						drmsg.put("EndYMD", eYMD);
						drmsg.put("EndTime", eTime);
						drmsg.put("Value", value);
						drmsg.put("Response", 200);
						drmsg.put("RequestID", 1);
						drmsg.put("EventID", 1);
						drmsg.put("ModificationNumber", 0);
						drmsg.put("TargetVEN", "MIR_VEN" + String.valueOf(indexOfEma));
						drmsg.put("Responsedescription", "MIR");
						drmsg.put("OptType", "optIn");

						String m_msg = drmsg.toJSONString();
						DR_MSG = m_msg;

					} else if (message_method == 1) {

						if (global.emaProtocolCoAP_EventFlag.containsKey(ema_name)) {
							if (global.emaProtocolCoAP.get(ema_name).isPullModel()) {

								// if (global.autoDR) {
								// while (global.autoDRCNT < global.autoDRTOTAL)
								// {
								// global.emaProtocolCoAP_EventFlag.get(textField.getText()).setEventFlag(true)
								// .setStartYMD(Integer.parseInt(sYMD))
								// .setStartTime(Integer.parseInt(sTime + "11"))
								// .setEndYMD(Integer.parseInt(eYMD))
								// .setEndTime(Integer.parseInt(eTime + "11"))
								// .setThreshold(Double.parseDouble(textField_7.getText()));
								// }
								// }else{

								String eventID = global.eventID.get(0);

								global.emaProtocolCoAP_EventFlag.get(textField.getText()).setEventFlag(true)
										.setStartYMD(Integer.parseInt(sYMD))
										.setStartTime(Integer.parseInt(sTime + "11")).setEndYMD(Integer.parseInt(eYMD))
										.setEndTime(Integer.parseInt(eTime + "11"))
										.setThreshold(Double.parseDouble(textField_7.getText())).setEventID(eventID);
								;

								global.eventFromServer = false;
								global.autoDRCNT = 0;

								// }
							} else {
								System.err.println(ema_name + " is not supported Pull Model");
								return;
							}
						}

					} else if (message_method == 2) {

						indexOfEma = Integer.parseInt(ema_name);
						state = 1;
						JSONObject drmsg = new JSONObject();

						drmsg.put("Service", "oadrDistributeEvent");
						drmsg.put("StartYMD", sYMD);
						drmsg.put("StartTime", sTime);
						drmsg.put("EndYMD", eYMD);
						drmsg.put("EndTime", eTime);
						drmsg.put("Value", value);
						drmsg.put("Response", 200);
						drmsg.put("RequestID", 1);
						drmsg.put("EventID", 1);
						drmsg.put("ModificationNumber", 0);
						drmsg.put("TargetVEN", "MIR_VEN" + String.valueOf(indexOfEma));
						drmsg.put("Responsedescription", "MIR");
						drmsg.put("OptType", "optIn");
						String m_msg = drmsg.toJSONString();
						DR_MSG = m_msg;
						// for(int i=0; i<=20; i++){
						InetAddress gatewayIp = MainFrame.gateway_db.gateway_list.get(1).ip_addr;
						System.out.println(gatewayIp);
						System.out.println(gatewayIp);
						System.out.println(gatewayIp);
						System.out.println(gatewayIp);
						System.out.println(gatewayIp);

						String ipParse[] = gatewayIp.toString().split("/");
						String ipaddr = ipParse[1];
						CoAPClient coapClient = new CoAPClient(ipaddr, "oadrDistributeEvent", m_msg);
						// coapClient.
						// }
					} else if (message_method == 22) {

						// System.out.println("일단여기");C
						// COAP OBS
						String destEMA = textField.getText();
						String eventID = global.eventID.get(0);

						System.out.println(eventID);

						if (global.obs_emaProtocolCoAP_EventFlag.containsKey(destEMA)) {
							// obsEventFlag = true;
							global.obs_emaProtocolCoAP_EventFlag.get(destEMA).setEventFlag(true)
									.setStartYMD(Integer.parseInt(sYMD)).setStartTime(Integer.parseInt(sTime + "11"))
									.setEndYMD(Integer.parseInt(eYMD)).setEndTime(Integer.parseInt(eTime + "11"))
									.setThreshold(Double.parseDouble(textField_7.getText())).setEventID(eventID);

							global.autoDRCNT = 0;

							if (global.autoDR) {
								while (true) {

									if (global.autoDRCNT < global.autoDRTOTAL) {
										global.autoDRCNT += 1;
										eventID = global.eventID.get(global.autoDRCNT);
										global.obs_emaProtocolCoAP_EventFlag.get(destEMA).setEventFlag(true)
												.setThreshold(Double.parseDouble(textField_7.getText()));
										global.obs_emaProtocolCoAP_EventFlag.get(destEMA).setEventID(eventID);
										System.out.println(global.autoDRCNT + "==>");
										global.observeManager.get(destEMA).changed();

										try {
											Thread.sleep(1000);
										} catch (InterruptedException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									} else {
										global.obs_emaProtocolCoAP_EventFlag.get(destEMA).setEventFlag(false);
										
									}
								}

							} else {
								global.observeManager.get(destEMA).changed();
							}
							global.eventFromServer = false;
						}
						//
						// indexOfEma = Integer.parseInt(ema_name);
						//
						// JSONObject drmsg = new JSONObject();
						//
						// drmsg.put("Service", "oadrDistributeEvent");
						// drmsg.put("StartYMD", sYMD);
						// drmsg.put("StartTime", sTime);
						// drmsg.put("EndYMD", eYMD);
						// drmsg.put("EndTime", eTime);
						// drmsg.put("Value", value);
						// drmsg.put("Response", value);
						// drmsg.put("RequestID", 1);
						// drmsg.put("EventID", 1);
						// drmsg.put("ModificationNumber", 0);
						// drmsg.put("TargetVEN", "MIR_VEN" +
						// String.valueOf(indexOfEma));
						// drmsg.put("Responsedescription", "MIR");
						// drmsg.put("OptType", "optIn");
						//
						// String m_msg = drmsg.toJSONString();
						//
						// DR_MSG = m_msg;
						// global.eventStatus = true;
					} else if (message_method == 23) {

						// System.out.println("드루갔니?");
						// System.out.println(
						// "한번보자꾸나" +
						// global.emaProtocolCoAP_EventFlag.get(textField.getText()).isEventFlag());

						// obsEventFlag = true;
						for (multiNum = 1; multiNum < 4; multiNum++) {

							if (global.getObs_emaProtocolCoAP_EventFlag().containsKey(String.valueOf(multiNum))) {

								global.getObs_emaProtocolCoAP_EventFlag().get(String.valueOf(multiNum))
										.setEventFlag(true).setStartYMD(Integer.parseInt(sYMD))
										.setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(eYMD))
										.setEndTime(Integer.parseInt(eTime)).setThreshold(value);
							}
						}

						// System.out.println(
						// "한번보자꾸나" +
						// global.emaProtocolCoAP_EventFlag.get(textField.getText()).isEventFlag());

						/*
						 * COAP
						 */
						// for (multiNum = 1; multiNum < 4; multiNum++) {
						// if
						// (global.getObs_emaProtocolCoAP_EventFlag().containsKey(String.valueOf(multiNum)))
						// {
						//
						// global.getObs_emaProtocolCoAP_EventFlag().get(String.valueOf(multiNum)).setEventFlag(true)
						// .setStartYMD(Integer.parseInt(sYMD)).setStartTime(Integer.parseInt(sTime))
						// .setEndYMD(Integer.parseInt(eYMD)).setEndTime(Integer.parseInt(eTime));
						//
						// System.out.println("멀티보자!"
						// +
						// global.emaProtocolCoAP_EventFlag.get(String.valueOf(multiNum)).isEventFlag());
						// }
						// }
						//
						// for (multiNum = 1; multiNum < 4; multiNum++) {
						// indexOfEma = multiNum;
						// /*
						// * maximal matching algorithm
						// */
						// JSONObject drmsg = new JSONObject();
						//
						// drmsg.put("Service", "oadrDistributeEvent");
						// drmsg.put("StartYMD", sYMD);
						// drmsg.put("StartTime", sTime);
						// drmsg.put("EndYMD", eYMD);
						// drmsg.put("EndTime", eTime);
						// drmsg.put("Value", value);
						// drmsg.put("Response", value);
						// drmsg.put("RequestID", 1);
						// drmsg.put("EventID", 1);
						// drmsg.put("ModificationNumber", 0);
						// drmsg.put("TargetVEN", "MIR_VEN" +
						// String.valueOf(indexOfEma));
						// drmsg.put("Responsedescription", "MIR");
						// drmsg.put("OptType", "optIn");
						//
						// String m_msg = drmsg.toJSONString();
						//
						// DR_MSG = m_msg;
						// global.eventStatus = true;
						// }
					} else if (message_method == 24) {

						for (multiNum = 1; multiNum < 10; multiNum++) {

							if (global.getObs_emaProtocolCoAP_EventFlag().containsKey(String.valueOf(multiNum))) {

								global.getObs_emaProtocolCoAP_EventFlag().get(String.valueOf(multiNum))
										.setEventFlag(true).setStartYMD(Integer.parseInt(sYMD))
										.setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(eYMD))
										.setEndTime(Integer.parseInt(eTime)).setThreshold(value);
							}
						}

						// for (multiNum = 1; multiNum < 10; multiNum++) {
						// if
						// (global.getObs_emaProtocolCoAP_EventFlag().containsKey(String.valueOf(multiNum)))
						// {
						//
						// global.getObs_emaProtocolCoAP_EventFlag().get(String.valueOf(multiNum))
						// .setEventFlag(true).setStartYMD(Integer.parseInt(sYMD))
						// .setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(eYMD))
						// .setEndTime(Integer.parseInt(eTime));
						//
						// System.out.println("멀티보자! 99999999999"
						// +
						// global.emaProtocolCoAP_EventFlag.get(String.valueOf(multiNum)).isEventFlag());
						// }
						// }

					} else if (message_method == 25) {
						for (multiNum = 1; multiNum < 21; multiNum++) {

							if (global.getObs_emaProtocolCoAP_EventFlag().containsKey(String.valueOf(multiNum))) {

								global.getObs_emaProtocolCoAP_EventFlag().get(String.valueOf(multiNum))
										.setEventFlag(true).setStartYMD(Integer.parseInt(sYMD))
										.setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(eYMD))
										.setEndTime(Integer.parseInt(eTime)).setThreshold(value);
							}
						}

						// for (multiNum = 1; multiNum < 21; multiNum++) {
						// indexOfEma = multiNum;
						//
						// JSONObject drmsg = new JSONObject();
						//
						// drmsg.put("Service", "oadrDistributeEvent");
						// drmsg.put("StartYMD", sYMD);
						// drmsg.put("StartTime", sTime);
						// drmsg.put("EndYMD", eYMD);
						// drmsg.put("EndTime", eTime);
						// drmsg.put("Value", value);
						// drmsg.put("Response", value);
						// drmsg.put("RequestID", 1);
						// drmsg.put("EventID", 1);
						// drmsg.put("ModificationNumber", 0);
						// drmsg.put("TargetVEN", "MIR_VEN" +
						// String.valueOf(indexOfEma));
						// drmsg.put("Responsedescription", "MIR");
						// drmsg.put("OptType", "optIn");
						//
						// String m_msg = drmsg.toJSONString();
						//
						// DR_MSG = m_msg;
						// global.eventStatus = true;
						// }
					}
				}

				//
				// Minute a ;
				// a = new Minute minute;
				Minute sm = new Minute(Integer.parseInt(textField_3.getText()), Integer.parseInt(textField_2.getText()),
						calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
				Minute em = new Minute(Integer.parseInt(textField_6.getText()), Integer.parseInt(textField_5.getText()),
						calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

				DRSchedulingGraph.updateScheduling(indexOfEma, sm, em, value);

			}
		});
	}

	public String DR_MSG() {
		state = 2;
		return DR_MSG;
	}

	public int VEN_ID() {
		return indexOfEma;
	}

	public int state() {
		System.out.println("DR State: " + state);
		return state;
	}
}
