package com.mir.ems.deviceProfile;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONObject;

import com.mir.ems.udp.UdpMessageHandler;
import com.mir.ems.udp.udpServer.sendUDPThread;

import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class VTN_tab extends JPanel implements ActionListener, MouseListener {
	public static DefaultTableModel ema_table_model;
	public JComboBox<String> comboBox;
	public JTable ven_table;

	public VTN_tab() {
		String[] ven_colHeadings = { " ", "VTN Name", "VEN Num", "Profile", "Protocol", "Value", "Unit", "Price" };
		String[][] data = {};
		ema_table_model = new DefaultTableModel(data, ven_colHeadings);

		setBorder(null);
		setForeground(Color.WHITE);
		ven_table = new JTable() {
			public java.lang.Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Boolean.class;
				case 1:
					return String.class;
				case 3:
					return String.class;
				default:
					return String.class;
				}
			};
		};
		ven_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		ven_table.setBorder(null);
		ven_table.setFont(new Font("Arial", Font.PLAIN, 12));
		ven_table.setForeground(Color.DARK_GRAY);
		setLayout(null);
		ven_table.setModel(ema_table_model);
		ven_table.setBounds(0, 0, 615, 240);
		// 615, 304
		JScrollPane scrollPane = new JScrollPane(ven_table);
		scrollPane.setBounds(0, 0, 615, 239);
		add(scrollPane);

		comboBox = new JComboBox<String>();
		comboBox.addItem("Maximum_Demand");
		comboBox.addItem("Base_Load");
		comboBox.setBounds(448, 246, 161, 21);
		add(comboBox);

		comboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int row_num = ven_table.getRowCount();
				Object[] Keyset = UdpMessageHandler.vtnHash.keySet().toArray();
				JSONObject drmsg = new JSONObject();
				System.out.println("Aaaa" + row_num);

				for (int i = 0; i < row_num; i++) {
					if ((Boolean) ema_table_model.getValueAt(i, 0) == true) {
						int vtn_state = 0;
						int type;
						int protocol;
						System.out.println("aaa");

						if (comboBox.getSelectedItem().toString().equals("Maximum_Demand")) {
							vtn_state = 1;
							String keyValue = UdpMessageHandler.vtnHash.get(Keyset[i]).toString();
							String[] valueSplit = keyValue.split("/");
							InetAddress vtnIP;
							try {
								vtnIP = InetAddress.getByName(valueSplit[1]);
								int vtnPort = Integer.parseInt(valueSplit[2]);
								drmsg.put("msgType", "Maximum Demand");
								drmsg.put("VTNID", "vtnID");
								drmsg.put("EMSID", "EMSID");
								drmsg.put("TransportName", "UDP/JSON");
								drmsg.put("Version", "VERSION");
								drmsg.put("Response", 200);
								drmsg.put("ResponseType", "OK");
								drmsg.put("RegistrationID", "registrationID");
								drmsg.put("Duration", 2000);

								String setREGACK = drmsg.toJSONString();
								System.out.println("aaa");
								Runnable p2 = new sendUDPThread(vtnIP, vtnPort, setREGACK);
								Thread p5 = new Thread(p2);
								p5.start();
							} catch (UnknownHostException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}

						else if (comboBox.getSelectedItem().toString().equals("off")) {
							vtn_state = 2;
						}
					}
				}
			}
		});

		JCheckBox chckbxNewCheckBox = new JCheckBox("Auto Control by Algorithm(check True)");
		chckbxNewCheckBox.setBounds(10, 245, 251, 23);
		add(chckbxNewCheckBox);

		JLabel lblNewLabel = new JLabel("Control ComboBox");
		lblNewLabel.setBounds(318, 249, 118, 15);
		add(lblNewLabel);

	}

	public static void modify_EMA_table() {

		int ema_rows_num = VTN_tab.ema_table_model.getRowCount();
		for (int i = ema_rows_num - 1; i >= 0; i--) {
			ema_table_model.removeRow(i);
		}

		int VTNListSize = UdpMessageHandler.vtnHash.size();
		Object[] Keyset = UdpMessageHandler.vtnHash.keySet().toArray();

		for (int i = 0; i < VTNListSize; i++) {
			String keyValue = UdpMessageHandler.vtnHash.get(Keyset[i]).toString();
			String[] valueSplit = keyValue.split("/");
			ema_table_model.addRow(new Object[] { false, valueSplit[3], valueSplit[4], valueSplit[5], valueSplit[6],
					valueSplit[7], "kW", "null"

			});

		}

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

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
