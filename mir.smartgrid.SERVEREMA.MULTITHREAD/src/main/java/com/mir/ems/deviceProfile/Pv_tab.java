package com.mir.ems.deviceProfile;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.mir.ems.database.GeneratorDatabase;

@SuppressWarnings("serial")
public class Pv_tab extends JPanel {
	
	public static GeneratorDatabase generator_db;
	public static DefaultTableModel pv_table_model;

	public Pv_tab() {
		String[] pv_colHeadings = {" ", "EMA ID", "Name", "Mode", "Power", "Priority"};
		String[][] data = {};
		pv_table_model = new DefaultTableModel(data, pv_colHeadings);

		setBorder(null);
		setForeground(Color.WHITE);
		JTable pv_table= new JTable() {
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
		pv_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		pv_table.setBorder(null);
		pv_table.setFont(new Font("Arial", Font.PLAIN, 12));
		pv_table.setForeground(Color.DARK_GRAY);
		setLayout(null);
		pv_table.setModel(pv_table_model);
		pv_table.setBounds(0, 0, 503, 240);

		// JScrollPane sc = new JScrollPane(table_2);
		JScrollPane scrollPane = new JScrollPane(pv_table);
		scrollPane.setBounds(0, 0, 564, 220);
		add(scrollPane);
		generator_db = new GeneratorDatabase();

	}
	public static void modify_generator_table(){
		int PV_rows_num = Pv_tab.pv_table_model.getRowCount();
		
		for(int i = PV_rows_num -1; i>=0; i--){
			pv_table_model.removeRow(i);
		}
		int pvListSize = generator_db.generator_list.size();
		for (int i = 0; i < pvListSize; i++) {
			pv_table_model.addRow(new Object[] { false,
					"EMA"+generator_db.generator_list.get(i).gateway_id, generator_db.generator_list.get(i).name,
					generator_db.generator_list.get(i).mode, generator_db.generator_list.get(i).power });
		}
	}
}
