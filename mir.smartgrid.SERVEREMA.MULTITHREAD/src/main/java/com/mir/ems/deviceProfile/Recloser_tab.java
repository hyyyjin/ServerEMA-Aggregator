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

import com.mir.ems.database.RecloserDatabase;
//import com.mir.ems.mqtt.Mqtt.publishThread;

@SuppressWarnings("serial")
public class Recloser_tab extends JPanel implements ActionListener, MouseListener {
	public static RecloserDatabase recloser_db;

	public JComboBox<String> comboBox_1;
	public JTable recloser_table;
	public static DefaultTableModel recloser_table_model;

	public Recloser_tab() {
		String[] recloser_colHeadings = { " ", "EMA ID", "Name", "Mode", "Power", "Priority" };
		String[][] data = {};
		recloser_table_model = new DefaultTableModel(data, recloser_colHeadings);

		setBorder(null);
		setForeground(Color.WHITE);
		recloser_table = new JTable() {
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
		recloser_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		recloser_table.setBorder(null);
		recloser_table.setFont(new Font("Arial", Font.PLAIN, 12));
		recloser_table.setForeground(Color.DARK_GRAY);
		setLayout(null);
		recloser_table.setModel(recloser_table_model);
		recloser_table.setBounds(0, 0, 503, 240);

		// JScrollPane sc = new JScrollPane(table_2);
		JScrollPane scrollPane = new JScrollPane(recloser_table);
		scrollPane.setBounds(0, 0, 564, 220);
		add(scrollPane);

		comboBox_1 = new JComboBox<String>();
		comboBox_1.setBounds(126, 231, 239, 21);

		comboBox_1.addItem("Trip");
		comboBox_1.addItem("Close");

		comboBox_1.addActionListener(this);

		add(comboBox_1);
		recloser_db = new RecloserDatabase();
		
	}
	public static void modify_recloser_table() {
		int recloser_rows_num = Recloser_tab.recloser_table_model.getRowCount();

		for (int i = recloser_rows_num - 1; i >= 0; i--) {
			recloser_table_model.removeRow(i);
		}
		int recloserListSize = recloser_db.recloser_list.size();
		for (int i = 0; i < recloserListSize; i++) {
			recloser_table_model.addRow(new Object[] { false, "EMA"+recloser_db.recloser_list.get(i).gateway_id,
					recloser_db.recloser_list.get(i).name, recloser_db.recloser_list.get(i).mode,
					recloser_db.recloser_list.get(i).power, recloser_db.recloser_list.get(i).priority });
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		int recloser_state = 0;
		int type;
		int protocol;

		String[] mqtt = new String[2];
		String[] coap = { "B3", "01", "02", "00", "00", "00", "00", "FF", "F1", "31", "00", "00", "00" };
		if (comboBox_1.getSelectedItem().toString().equals("Trip"))
			recloser_state = 1;
		else if (comboBox_1.getSelectedItem().toString().equals("Close"))
			recloser_state = 2;

		if (recloser_state % 2 == 1) {
			type = 1;
			mqtt[0] = "21";
			mqtt[1] = "1F";
			coap[10] = "1F";
		} else {
			type = 0;
			mqtt[0] = "21";
			mqtt[1] = "10";
			coap[10] = "10";
		}
		byte[] buf = new byte[6];
		buf[0] = (byte) 2;
		buf[5] = (byte) type;

		int recloser_id;
		int gatewayId;

		String topic;
		byte[] m_token = toByte(mqtt[0] + mqtt[1]);

		int row_num = recloser_table.getRowCount();
		for (int i = 0; i < row_num; i++) {
			if ((Boolean) recloser_table_model.getValueAt(i, 0) == true) {
				try {

					recloser_id = Recloser_tab.recloser_db.recloser_list.get(i).recloser_id;
					gatewayId = Recloser_tab.recloser_db.recloser_list.get(i).gateway_id;
					protocol = Recloser_tab.recloser_db.recloser_list.get(i).protocol;
					if (recloser_state == 3 || recloser_state == 4) {
						buf[1] = (byte) 127;
						buf[3] = (byte) gatewayId;
					} else if (recloser_state == 5 || recloser_state == 6) {
						buf[1] = (byte) 127;
						buf[3] = (byte) 127;
					} else {
						buf[1] = (byte) recloser_id;
						buf[3] = (byte) gatewayId;
					}

					if (recloser_id < 10) {
						coap[4] = "0" + String.valueOf(recloser_id);
					} else {
						coap[4] = String.valueOf(recloser_id);
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

					topic = "gw/" + String.valueOf(gatewayId) + "/OpenFMB/" + "/RECLOSER/" + String.valueOf(recloser_id)+"/"+comboBox_1.getSelectedItem().toString();
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
