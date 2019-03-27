package com.mir.ems.deviceProfile;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.mir.ems.database.SmartMeterDatabase;

@SuppressWarnings("serial")
public class SmartMeter_tab extends JPanel{
	public static SmartMeterDatabase smartMeter_db;

	public JTable smartMeter_table;
	public static DefaultTableModel smartMeter_table_model;

	public SmartMeter_tab() {
		String[] smartMeter_colHeadings = { " ", "EMA ID", "DCU ID", "Active Energy"};
		String[][] data = {};
		smartMeter_table_model = new DefaultTableModel(data, smartMeter_colHeadings);

		setBorder(null);
		setForeground(Color.WHITE);
		smartMeter_table= new JTable() {
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
		smartMeter_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		smartMeter_table.setBorder(null);
		smartMeter_table.setFont(new Font("Arial", Font.PLAIN, 12));
		smartMeter_table.setForeground(Color.DARK_GRAY);
		setLayout(null);
		smartMeter_table.setModel(smartMeter_table_model);
		smartMeter_table.setBounds(0, 0, 503, 240);

		// JScrollPane sc = new JScrollPane(table_2);
		JScrollPane scrollPane = new JScrollPane(smartMeter_table);
		scrollPane.setBounds(0, 0, 564, 220);
		add(scrollPane);
		
		smartMeter_db = new SmartMeterDatabase();
	}

	public static void modify_meter_table() {
		int meter_rows_num = SmartMeter_tab.smartMeter_table_model.getRowCount();

		for (int i = meter_rows_num - 1; i >= 0; i--) {
			smartMeter_table_model.removeRow(i);
		}
		int meterListSize = smartMeter_db.smartMeter_list.size();
		for (int i = 0; i < meterListSize; i++) {
			smartMeter_table_model.addRow(new Object[] { false, "EMA" + smartMeter_db.smartMeter_list.get(i).emaNum,
					smartMeter_db.smartMeter_list.get(i).devID, smartMeter_db.smartMeter_list.get(i).energyValue });
		}
	}
}
