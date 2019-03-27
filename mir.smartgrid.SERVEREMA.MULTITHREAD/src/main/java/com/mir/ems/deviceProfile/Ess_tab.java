package com.mir.ems.deviceProfile;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.mir.ems.database.StorageDatabase;
//import com.mir.ems.mqtt.Mqtt.publishThread;

@SuppressWarnings("serial")
public class Ess_tab extends JPanel implements ActionListener, MouseListener {

	public static StorageDatabase storage_db;
	public static DefaultTableModel ess_table_model;
	public JComboBox<String> comboBox_2;
	public JTable ess_table;

	public Ess_tab() {
		String[] ess_colHeadings = { " ", "EMA ID", "Name", "Mode", "SoC", "Power", "Volt", "Frequency", "Priority" };
		String[][] data = {};
		ess_table_model = new DefaultTableModel(data, ess_colHeadings);

		setBorder(null);
		setForeground(Color.WHITE);
		ess_table = new JTable() {
			public java.lang.Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Boolean.class;
				case 1:
				case 3:
					return Integer.class;
				case 5:
					return Double.class;
				default:
					return String.class;
				}
			};
		};
		ess_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		ess_table.setBorder(null);
		ess_table.setFont(new Font("Arial", Font.PLAIN, 12));
		ess_table.setForeground(Color.DARK_GRAY);
		setLayout(null);
		ess_table.setModel(ess_table_model);
		ess_table.setBounds(0, 0, 503, 240);

		// JScrollPane sc = new JScrollPane(table_2);
		JScrollPane scrollPane = new JScrollPane(ess_table);
		scrollPane.setBounds(0, 0, 564, 220);

		comboBox_2 = new JComboBox<String>();
		comboBox_2.setBounds(134, 244, 232, 21);
		comboBox_2.addItem("Charge");
		comboBox_2.addItem("Discharge");

		add(comboBox_2);

		add(scrollPane);
		storage_db = new StorageDatabase();

	}

	public static void modify_storage_table() {
		int ESS_rows_num = Ess_tab.ess_table_model.getRowCount();

		for (int i = ESS_rows_num - 1; i >= 0; i--) {
			ess_table_model.removeRow(i);
		}
		int essListSize = storage_db.storage_list.size();
		for (int i = 0; i < essListSize; i++) {
			ess_table_model.addRow(new Object[] { false, "EMA"+storage_db.storage_list.get(i).gwNum,
					storage_db.storage_list.get(i).name, storage_db.storage_list.get(i).mode,
					storage_db.storage_list.get(i).soc, storage_db.storage_list.get(i).power,
					storage_db.storage_list.get(i).volt, storage_db.storage_list.get(i).hz,
					storage_db.storage_list.get(i).priority });
		}
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int ess_state = 0;
		int type;
		int protocol;

		String[] mqtt = new String[2];
		String[] coap = { "B3", "01", "02", "00", "00", "00", "00", "FF", "F1", "31", "00", "00", "00" };
		if (comboBox_2.getSelectedItem().toString().equals("Charge"))
			ess_state = 1;
		else if (comboBox_2.getSelectedItem().toString().equals("Discharge"))
			ess_state = 2;

		if (ess_state % 2 == 1) {
			type = 1;
			mqtt[0] = "22";
			mqtt[1] = "1F";
			coap[10] = "1F";
		} else {
			type = 0;
			mqtt[0] = "22";
			mqtt[1] = "10";
			coap[10] = "10";
		}
		byte[] buf = new byte[6];
		buf[0] = (byte) 2;
		buf[5] = (byte) type;

		int storage_id;
		int gatewayId;

		String topic;
		byte[] m_token = toByte(mqtt[0] + mqtt[1]);

		int row_num = ess_table.getRowCount();
		for (int i = 0; i < row_num; i++) {
			if ((Boolean) ess_table_model.getValueAt(i, 0) == true) {
				try {

					storage_id = Ess_tab.storage_db.storage_list.get(i).storage_id;
					gatewayId = Ess_tab.storage_db.storage_list.get(i).gwNum;
					protocol = Ess_tab.storage_db.storage_list.get(i).protocol;
					if (ess_state == 3 || ess_state == 4) {
						buf[1] = (byte) 127;
						buf[3] = (byte) gatewayId;
					} else if (ess_state == 5 || ess_state == 6) {
						buf[1] = (byte) 127;
						buf[3] = (byte) 127;
					} else {
						buf[1] = (byte) storage_id;
						buf[3] = (byte) gatewayId;
					}

					if (storage_id < 10) {
						coap[4] = "0" + String.valueOf(storage_id);
					} else {
						coap[4] = String.valueOf(storage_id);
					}

					String c_msgS = coap[0];

					for (int j = 1; j < 5; j++) {
						c_msgS += coap[j];
					}

					String c_msgS2 = coap[5];
					for (int k = 6; k < 10; k++) {
						c_msgS2 += coap[k];
					}
					String c_msgS_ctr = coap[10];
					String c_msgS3 = coap[11];
					c_msgS3 += coap[12];

					byte[] msg_buf0 = toByte(c_msgS);
					System.out.println(msg_buf0.length);
					byte[] msg_buf1 = toByte(c_msgS2);
					System.out.println(msg_buf1.length);

					byte[] msg_buf2 = toByte(coap[10] + coap[11] + coap[12]);
					System.out.println(msg_buf2.length);
					byte[] c_msg = new byte[msg_buf0.length + msg_buf1.length + msg_buf2.length];
					System.arraycopy(msg_buf0, 0, c_msg, 0, msg_buf0.length);

					System.arraycopy(msg_buf1, 0, c_msg, msg_buf0.length, msg_buf1.length);
					System.arraycopy(msg_buf2, 0, c_msg, msg_buf0.length + msg_buf1.length, msg_buf2.length);
					System.out.println(c_msg.length);

					topic = "gw/" + String.valueOf(gatewayId) + "/OpenFMB/" + "/ESS/"+String.valueOf(storage_id)+"/"+comboBox_2.getSelectedItem().toString();
					System.out.println(topic);

					System.out.println("Protocol Type: " + protocol);
					if (protocol == 1) {// gatewayPort == 1883) {

						
//						Runnable p = new publishThread(topic, 0, m_token);
//						Thread p7 = new Thread(p);
//						p7.start();

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

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
