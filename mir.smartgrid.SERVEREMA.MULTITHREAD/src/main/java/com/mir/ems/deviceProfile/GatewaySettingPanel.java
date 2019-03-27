package com.mir.ems.deviceProfile;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.codec.digest.DigestUtils;

import com.mir.ems.coap.CoAPClient;
import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.database.item.GatewayClass;
import com.mir.ems.globalVar.global;
//import com.mir.ems.mqtt.Mqtt;

public class GatewaySettingPanel extends JPanel implements ActionListener {
	// public static JPanel gatewayTablePanel;
	public static JPanel thresholdSettingPanel;
	public static JTable gatewayTable;
	public static DefaultTableModel gatewayTableModel;
	public static JTextField thresholdText;
	public static JButton thresholdBtn;
	private Component rigidArea;
	private Component rigidArea_1;
	private Component rigidArea_2;
	private Component rigidArea_3;
	private Component rigidArea_4;
	private Component rigidArea_5;
	private Component rigidArea_6;
	private Component rigidArea_7;
	private Component rigidArea_8;
	private Component rigidArea_9;
	private Component rigidArea_10;

	// public static Mqtt mqttclient = null;

	public GatewaySettingPanel() {
		setPreferredSize(new Dimension(1219, 684));
		setMinimumSize(new Dimension(1000, 600));

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		// setBounds(14, 60, 1467, 700);

		String[][] data = {};
		String[] colHeadings = { " ", "EMA ID", "Registered EMA ID", "IP Address", "port number", "building", "floor", "room no.",
				"room name"};
		gatewayTable = new JTable() {
			public java.lang.Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Boolean.class;
				case 1:
					return String.class;
				case 2:
				case 4:
				case 7:
					return String.class;
				default:
					return Integer.class;
				}
			};
		};

		gatewayTableModel = new DefaultTableModel(new Object[][] {}, colHeadings);
		gatewayTable.setBackground(Color.white);
		gatewayTable.setModel(gatewayTableModel);
		gatewayTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// gatewayTable.addMouseListener(this);

		DefaultTableCellRenderer center_renderer = new DefaultTableCellRenderer();
		center_renderer.setHorizontalAlignment(JLabel.CENTER);
		gatewayTable.getColumn(gatewayTable.getColumnName(1)).setCellRenderer(center_renderer);
		gatewayTable.getColumn(gatewayTable.getColumnName(3)).setCellRenderer(center_renderer);
		gatewayTable.getColumn(gatewayTable.getColumnName(5)).setCellRenderer(center_renderer);
		gatewayTable.getColumn(gatewayTable.getColumnName(6)).setCellRenderer(center_renderer);
		gatewayTable.getColumn(gatewayTable.getColumnName(8)).setCellRenderer(center_renderer);

		// set the preference width for each column
		gatewayTable.getColumnModel().getColumn(0).setPreferredWidth(10);
		gatewayTable.getColumnModel().getColumn(1).setPreferredWidth(15);
		gatewayTable.getColumnModel().getColumn(2).setPreferredWidth(30);
		gatewayTable.getColumnModel().getColumn(3).setPreferredWidth(20);
		gatewayTable.getColumnModel().getColumn(5).setPreferredWidth(15);
		gatewayTable.getColumnModel().getColumn(6).setPreferredWidth(15);
		gatewayTable.getColumnModel().getColumn(8).setPreferredWidth(25);

		// initialize threshold setting panel
		thresholdSettingPanel = new JPanel();

		thresholdSettingPanel.setLayout(new BoxLayout(thresholdSettingPanel, BoxLayout.LINE_AXIS));

		JLabel thresholdLabel = new JLabel("EMA Register");
		thresholdText = new JTextField();
		thresholdBtn = new JButton("set");
		thresholdBtn.addActionListener(this);

		thresholdBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub


				String registrationID = DigestUtils.md5Hex(thresholdText.getText().toString());
				global.emaRegister.put(thresholdText.getText().toString(), registrationID);

				FileWriter fw;
				try {
					fw = new FileWriter(new File("RegisteredEMAList.cfg"));
					
					Iterator<String> it = global.emaRegister.keySet().iterator();
					
					while(it.hasNext()){
						String key = it.next();
						
						fw.write(String.format("%s/%s",key, global.emaRegister.get(key).toString()));
						fw.write(System.lineSeparator()); 
						
					}
					

					fw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
				modify_gateway_table();

			}
		});

		// add threshold setting components
		thresholdSettingPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		thresholdSettingPanel.add(thresholdLabel);
		thresholdSettingPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		thresholdSettingPanel.add(thresholdText);
		thresholdSettingPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		thresholdSettingPanel.add(thresholdBtn);
		thresholdSettingPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		// thresholdSettingPanel.add(Box.createHorizontalGlue());

		// add components (panel)
		add(Box.createRigidArea(new Dimension(0, 10)));
		JScrollPane scrollPane = new JScrollPane(gatewayTable);
		scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
		add(scrollPane);
		add(Box.createRigidArea(new Dimension(0, 10)));

		rigidArea = Box.createRigidArea(new Dimension(0, 10));
		add(rigidArea);

		rigidArea_1 = Box.createRigidArea(new Dimension(0, 10));
		add(rigidArea_1);

		rigidArea_2 = Box.createRigidArea(new Dimension(0, 10));
		add(rigidArea_2);

		rigidArea_3 = Box.createRigidArea(new Dimension(0, 10));
		add(rigidArea_3);

		rigidArea_4 = Box.createRigidArea(new Dimension(0, 10));
		add(rigidArea_4);

		rigidArea_5 = Box.createRigidArea(new Dimension(0, 10));
		add(rigidArea_5);

		rigidArea_6 = Box.createRigidArea(new Dimension(0, 10));
		add(rigidArea_6);

		rigidArea_7 = Box.createRigidArea(new Dimension(0, 10));
		add(rigidArea_7);

		rigidArea_8 = Box.createRigidArea(new Dimension(0, 10));
		add(rigidArea_8);

		rigidArea_9 = Box.createRigidArea(new Dimension(0, 10));
		add(rigidArea_9);

		rigidArea_10 = Box.createRigidArea(new Dimension(0, 10));
		add(rigidArea_10);
		add(thresholdSettingPanel);
		add(Box.createRigidArea(new Dimension(0, 10)));
	}

	// public void actionPerformed(ActionEvent arg0) {
	// int rowCount = gatewayTableModel.getRowCount();
	// DatagramPacket sendPacket;
	// byte b[] = new byte[10];
	// b[0] = (byte) 4;
	// int currentThresholdValue;
	// int gatewayID;
	// ArrayList<GatewayClass> gatewayList =
	// MainFrame.gateway_db.getGatewayList();
	//
	// for (int i = 0; i < rowCount; i++) {
	// if ((boolean) gatewayTableModel.getValueAt(i, 0) == true) {
	// currentThresholdValue = Integer.parseInt(thresholdText.getText());
	// gatewayID = Integer.parseInt(gatewayTableModel.getValueAt(i,
	// 1).toString());
	// // b[1] = (byte) gatewayID;
	// // b[2] = (byte) (currentThresholdValue/127); //byte max value = 127
	// // b[3] = (byte) (currentThresholdValue%127);
	// //
	// // sendPacket = new DatagramPacket(b, b.length,
	// // gatewayList.get(i).ip_addr, gatewayList.get(i).port_num);
	// sendThreshold(b, gatewayID, currentThresholdValue, gatewayList.get(i));
	// GatewaySettingPanel.gatewayTableModel.setValueAt(currentThresholdValue,
	// i, 8);
	// // try {
	// // MainFrame.socket.send(sendPacket);
	// // } catch (IOException e) { e.printStackTrace(); }
	// }
	// }
	// }

	@SuppressWarnings({ "unused", "static-access" })
	public static void sendThreshold(byte[] b, int gatewayID, int currentThresholdValue, GatewayClass gateway) {
		b[1] = (byte) gatewayID;
		b[2] = (byte) (currentThresholdValue / 127); // byte max value = 127
		b[3] = (byte) (currentThresholdValue % 127);
		String topicName = "gw/" + String.valueOf(gatewayID);
		String msg = String.valueOf(currentThresholdValue);
		DatagramPacket sendPacket = new DatagramPacket(b, b.length, gateway.ip_addr, gateway.port_num);
		if (gateway.port_num == 1883) {
			/*
			 * try { MainFrame.mqttclient.publish(topicName, 0, msg.getBytes());
			 * } catch (MqttException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
		} else if (gateway.port_num == 5683) {
			try {
				String addr = gateway.ip_addr.toString() + ":5683";
				CoAPClient client = new CoAPClient(addr, "/threshold", msg.getBytes());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// else{
		// try {
		// MainFrame.socket.send(sendPacket);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
	}

	public static void modify_gateway_table() {
		// // delete all data in gateway table
		int gatewayRowsNum = GatewaySettingPanel.gatewayTableModel.getRowCount();
		for (int i = gatewayRowsNum - 1; i >= 0; i--)
			GatewaySettingPanel.gatewayTableModel.removeRow(i);
		//
		// ArrayList<GatewayClass> gateway_list =
		// MainFrame.gateway_db.getGatewayList();
		//
		// int gatewayListSize = gateway_list.size();
		// for (int i = 0; i < gatewayListSize; i++) {
		// GatewaySettingPanel.gatewayTableModel.addRow(new Object[] { false,
		// gateway_list.get(i).gateway_id,
		// gateway_list.get(i).ip_addr, gateway_list.get(i).port_num,
		// gateway_list.get(i).building,
		// gateway_list.get(i).floor, gateway_list.get(i).room_num,
		// gateway_list.get(i).room_name,
		// gateway_list.get(i).threshold });
		// }

		// int size = global.emaProtocolCoAP.keySet().size();
		//
		// for (int i = 0; i < size; i++) {
		//
		// GatewaySettingPanel.gatewayTableModel.addRow(new Object[] { false,
		// glboal.get(i).gateway_id,
		// gateway_list.get(i).ip_addr, gateway_list.get(i).port_num,
		// gateway_list.get(i).building,
		// gateway_list.get(i).floor, gateway_list.get(i).room_num,
		// gateway_list.get(i).room_name,
		// gateway_list.get(i).threshold });
		//
		// }


		Iterator<String> it = global.emaRegister.keySet().iterator();

		while (it.hasNext()) {

			String key = it.next();

			GatewaySettingPanel.gatewayTableModel
					.addRow(new Object[] { false, key, global.emaRegister.get(key).toString(), "SAMPLE IP", "SAMPLE PORT", "ITBT", "4TH", "402-1", "ROOM", "MIR"});

		}

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
