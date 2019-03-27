package com.mir.ems.deviceProfile;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.mir.ems.database.EMADatabase;

@SuppressWarnings("serial")
public class EMA_tab extends JPanel{
	public static EMADatabase gateway_db;
	public static DefaultTableModel ema_table_model;

	public EMA_tab(){
		String[] ven_colHeadings = {" ", "EMA Name", "Protocol", "Resource", "Threshold"};
		String[][] data = {};
		ema_table_model = new DefaultTableModel(data, ven_colHeadings);

		setBorder(null);
		setForeground(Color.WHITE);
		JTable ven_table = new JTable() {
			public java.lang.Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Boolean.class;
				case 1:
					return String.class;
				case 3:
					return String.class;
				case 5:
					return Double.class;
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

		JScrollPane scrollPane = new JScrollPane(ven_table);
		scrollPane.setBounds(0, 0, 615, 300);
		add(scrollPane);
		
		gateway_db = new EMADatabase();
		gateway_db.addValue("MIR", "protocol", 0.0, 0.0);
		modify_EMA_table();
	}
	public static void modify_EMA_table(){
		
		int ema_rows_num = EMA_tab.ema_table_model.getRowCount();
		for(int i = ema_rows_num -1; i>=0; i--){
			ema_table_model.removeRow(i);
		}
		int emaListSize = gateway_db.ema_list.size();

		for (int i = 0; i < emaListSize; i++) {
			ema_table_model.addRow(new Object[] { false, 
					gateway_db.ema_list.get(i).emaName, gateway_db.ema_list.get(i).protocol,gateway_db.ema_list.get(i).resource
					, gateway_db.ema_list.get(i).threshold });
		}
	}
}
