
package com.mir.ems.deviceProfile;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.mir.ems.coap.CoAPClient;
import com.mir.ems.database.DeviceDatabase;
import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.Publishing;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Led_tab extends JPanel implements ActionListener, MouseListener {

	public static JTable led_table;
	public static DeviceDatabase device_db;
	public static DefaultTableModel led_table_model;
	public JComboBox<String> comboBox;
	private JButton dimming_plus, dimming_minus;

	public Led_tab() {

		String[] led_colHeadings = { " ", "EMA ID", "Name", "State", "Dimming", "Priority" };
		String[][] data = {};
		led_table_model = new DefaultTableModel(data, led_colHeadings);

		// JPanel panel = new JPanel();
		setBorder(null);
		setForeground(Color.WHITE);
		led_table = new JTable() {
			public java.lang.Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Boolean.class;
				case 2:
				case 3:
					return String.class;
				default:
					return Integer.class;
				}
			};
		};
		led_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		led_table.setBorder(null);
		led_table.setFont(new Font("Arial", Font.PLAIN, 12));
		led_table.setForeground(Color.DARK_GRAY);
		setLayout(null);
		led_table.setModel(led_table_model);
		led_table.setBounds(0, 0, 503, 240);

		// JScrollPane sc = new JScrollPane(table_2);
		JScrollPane scrollPane = new JScrollPane(led_table);
		scrollPane.setBounds(0, 0, 716, 108);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(143, 112, 175, 21);
		comboBox.addItem("on");
		comboBox.addItem("off");
		comboBox.addActionListener(this); // combobox on/off mode

		add(comboBox);
		add(scrollPane);

		dimming_minus = new JButton(" -1 ");
		dimming_minus.setBounds(425, 111, 97, 23);
		add(dimming_minus);

		dimming_plus = new JButton(" +1 ");
		dimming_plus.setBounds(523, 111, 97, 23);
		add(dimming_plus);

		dimming_plus.addMouseListener(this); // dimming mouseListener
		dimming_minus.addMouseListener(this);

		JLabel lblNewLabel = new JLabel("Control");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel.setBounds(89, 115, 57, 15);
		add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Dimming");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_1.setBounds(355, 115, 57, 15);
		add(lblNewLabel_1);

		device_db = new DeviceDatabase();

		TimerTask chartUpdaterTask = new TimerTask() {

			@Override
			public void run() {

				modify_LED_table();

			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 2000, 10000);

	}

	public static void modify_LED_table() {
		int LED_rows_num = Led_tab.led_table_model.getRowCount();

		for (int i = LED_rows_num - 1; i >= 0; i--) {
			led_table_model.removeRow(i);
		}

//		Iterator<String> it = global.devManger.keySet().iterator();
//		
//		while(it.hasNext()){
//			
//			String key = it.next();
//			
//			
//			String state = global.devManger.get(key).getMode() ==1 ? "ON" : "OFF";
//			
//			Led_tab.led_table_model
//			.addRow(new Object[] { false, global.devManger.get(key).getEma_id(), key,
//					state, global.devManger.get(key).getDimming(), global.devManger.get(key).getPriority() });
//				
//			
//		}
//		
//		
//		ConcurrentHashMap<String, ServerEmaProfile> globalEmaListDup = global.getOpenADRHashMap();
//
//		Set<String> emaSet = globalEmaListDup.keySet();
//		ArrayList<String> emaString = new ArrayList<String>(emaSet);
//		String[] emaList = new String[emaString.size()];
//
//		for (int i = 0; i < emaString.size(); i++) {
//			emaList[i] = emaString.get(i).toString();
//		}
//
//		Arrays.sort(emaList);
//		for (int i = 0; i < emaList.length; i++) {
//			String key = emaList[i];
//			if (global.getOpenADRHashMap().get(key).getState().equals("OFF")) {
//				Iterator<String> keys = global.devHashMap.keySet().iterator();
//				while (keys.hasNext()) {
//					String dev_key = keys.next();
//					if (key.equals(global.getDevHashMap().get(dev_key).getEmaID())) {
//
//						if (global.devHashMap.get(dev_key).getSort().contains("LED")) {
//							Led_tab.led_table_model
//									.addRow(new Object[] { false, global.devHashMap.get(dev_key).getEmaID(), dev_key,
//											"OFF", 0, global.devHashMap.get(dev_key).getPriority() });
//						}
//					}
//
//				}
//			} else if (global.getOpenADRHashMap().get(key).getState().equals("ON")) {
//				Iterator<String> keys = global.devHashMap.keySet().iterator();
//				while (keys.hasNext()) {
//					String dev_key = keys.next();
//					if (key.equals(global.getDevHashMap().get(dev_key).getEmaID())) {
//
//						if (global.devHashMap.get(dev_key).getSort().contains("LED")) {
//							Led_tab.led_table_model
//									.addRow(new Object[] { false, global.devHashMap.get(dev_key).getEmaID(), dev_key,
//											global.getDevHashMap().get(dev_key).getState(),
//											global.getDevHashMap().get(dev_key).getDimming(),
//											global.devHashMap.get(dev_key).getPriority() });
//						}
//					}
//				}
//			} else {
//				Iterator<String> keys = global.devHashMap.keySet().iterator();
//				while (keys.hasNext()) {
//					String dev_key = keys.next();
//					if (key.equals(global.getDevHashMap().get(dev_key).getEmaID())) {
//
//						if (global.devHashMap.get(dev_key).getSort().contains("LED")) {
//							Led_tab.led_table_model
//									.addRow(new Object[] { false, global.devHashMap.get(dev_key).getEmaID(), dev_key,
//											"Uncontrollable", global.getDevHashMap().get(dev_key).getDimming(),
//											global.devHashMap.get(dev_key).getPriority() });
//						}
//					}
//				}
//			}
//		}
//
//		/*
//		 * 
//		 * =====================================
//		 */
//
		Iterator<String> keys = global.emaProtocolCoAP_Device.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();

			if (global.emaProtocolCoAP_Device.get(key).getDeviceType().contains("LED")) {
				Led_tab.led_table_model.addRow(new Object[] { false, global.emaProtocolCoAP_Device.get(key).getEmaID(), global.emaProtocolCoAP_Device.get(key).getDeviceEMAID(),
						global.emaProtocolCoAP_Device.get(key).getState(), global.emaProtocolCoAP_Device.get(key).getDimming(), global.emaProtocolCoAP_Device.get(key).getPriority() });
			}
		}

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

		// dimming button
		JButton dimBtn = (JButton) arg0.getSource();
		int dimming;

		byte[] buf = new byte[6];
		buf[0] = (byte) 3;
		byte[] mqtt = new byte[3];
		String[] coap = { "B3", "01", "03", "00", "00", "00", "00", "FF", "F1", "31", "00", "00", "00" };

		int row_num = led_table.getRowCount();
		int nodeId;
		int roomId;
		InetAddress gatewayIp;
		int gatewayPort;
		int protocol;

		String emaID, devID;
		String topic;

		for (int i = 0; i < row_num; i++) {
			if ((Boolean) led_table_model.getValueAt(i, 0) == true) {
				try {

					String key = led_table_model.getValueAt(i, 2).toString();
					
					int protocolSort = global.devManger.get(key).getProtocol();
					roomId = global.devManger.get(key).room_id;
					String Ip = global.devManger.get(key).ipAddr;
					

					if(Ip.contains("/")) Ip = Ip.replace("/", "");
					if(Ip.contains("tcp:")) Ip = Ip.replace("tcp:", "");
					if(Ip.contains(":1883")) Ip = Ip.replace(":1883", "");
					
					devID = global.devManger.get(key).device_id;
					emaID = global.devManger.get(key).ema_id;
					dimming = global.devManger.get(key).dimming;

					if (dimBtn.getText().equals(" -1 ")) {
						dimming = dimming - 1;
						if (protocolSort == 1 || protocolSort == 2) {
							buf[5] = (byte) 0;
						} else {
							buf[5] = (byte) -1;
						}
					} else {
						buf[5] = (byte) 1;
						dimming = dimming + 1;
					}

					//MQTT
					if (protocolSort == 2) {
						mqtt[0] = 0x31;
						mqtt[1] = 0x1A;
						mqtt[2] = buf[5];
	
						String pubTopic = "gw/"+emaID+"/dev/" + devID + "/dimming";
						new Publishing().publishThread(global.devManger.get(key).getClient(), pubTopic, global.qos, mqtt);;
						
					} 
					
					//COAP
					else if (protocolSort == 1) {
						try {
							
							mqtt[0] = 0x31;
							mqtt[1] = 0x1A;
							mqtt[2] = buf[5];
							
							if(Ip.contains("/")) Ip = Ip.replace("/", "");
							
							String addr = Ip + ":5683";
							String url = "/gw/"+emaID+"/dev/"+devID+"/dimming";

							
//							CoAPClient client = new CoAPClient(addr, "/dimming", c_msg);
							new CoAPClient(addr, url, mqtt).start();
							
						} catch (Exception e) {
							// TODO Auto-generated catch block	
							e.printStackTrace();
						}
					} else {
					
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}

		}
	}

	public void actionPerformed(ActionEvent e) {
		int led_state = 0;
		int type;
		int protocol;
		int size;

		String[] mqtt = new String[2];
		String[] coap = { "B3", "01", "02", "00", "00", "00", "00", "FF", "F1", "31", "00", "00", "00" };
		
		if (comboBox.getSelectedItem().toString().equals("on"))
			led_state = 1;
		else if (comboBox.getSelectedItem().toString().equals("off"))
			led_state = 2;

		if (led_state == 1) {
			
			type = 1;
			mqtt[0] = "11";
			mqtt[1] = "1F";
//			coap[10] = "1F";
			
		} else {
			
			type = 0;
			mqtt[0] = "11";
			mqtt[1] = "10";
//			coap[10] = "10";
			
		}
		byte[] buf = new byte[6];
		buf[0] = (byte) 2;
		buf[5] = (byte) type;

		int nodeId;
		int gatewayId;
		InetAddress gatewayIp;
		InetAddress tempgatewayIp = null;
		int gatewayPort;
		int tempgatewayPort = 0;

		String topic, devID, emaID;
		byte[] m_token = toByte(mqtt[0] + mqtt[1]);

		int row_num = led_table.getRowCount();
		for (int i = 0; i < row_num; i++) {
			if ((Boolean) led_table_model.getValueAt(i, 0) == true) {
				try {

					String key = led_table_model.getValueAt(i, 2).toString();
					
					int protocolSort = global.devManger.get(key).getProtocol();
					String Ip = global.devManger.get(key).ipAddr;
					
					
					if(Ip.contains("/")) Ip = Ip.replace("/", "");
					if(Ip.contains("tcp:")) Ip = Ip.replace("tcp:", "");
					if(Ip.contains(":1883")) Ip = Ip.replace(":1883", "");

					
					devID = global.devManger.get(key).device_id;
					emaID = global.devManger.get(key).ema_id;
					
					
					if (comboBox.getSelectedItem().toString().equals("on"))
						led_state = 1;
					else if (comboBox.getSelectedItem().toString().equals("off"))
						led_state = 2;

					if (led_state == 1) {
						
						type = 1;
						mqtt[0] = "11";
						mqtt[1] = "1F";
						
					} else {
						
						type = 0;
						mqtt[0] = "11";
						mqtt[1] = "10";
						
					}
					m_token = toByte(mqtt[0] + mqtt[1]);

					//MQTT
					if (protocolSort == 2) {// gatewayPort == 1883) {
						

						String pubTopic = "gw/"+emaID+"/dev/" + devID + "/control";
						new Publishing().publishThread(global.devManger.get(key).getClient(), pubTopic, global.qos, m_token);;

					} 
					
					//COAP
					else if (protocolSort == 1) {
						try {
							String addr = Ip + ":5683";
							System.out.println(addr);
							
							String url = "/gw/"+emaID+"/dev/"+devID+"/control";
							
							System.out.println(addr);
							System.out.println(url);
							
							new CoAPClient(addr, url, m_token).start();

						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		}
	}

	public static byte[] toByte(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
