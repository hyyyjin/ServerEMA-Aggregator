package com.mir.ems.deviceProfile;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.mir.ems.database.ResourceDatabase;

@SuppressWarnings("serial")
public class Resource_tab extends JPanel{
	public static ResourceDatabase resource_db;

	public JTable resource_table;
	public static DefaultTableModel resource_table_model;

	public Resource_tab() {
		String[] resource_colHeadings = { " ", "EMA ID", "Name", "Mode", "Power", "Priority" };
		String[][] data = {};
		resource_table_model = new DefaultTableModel(data, resource_colHeadings);

		setBorder(null);
		setForeground(Color.WHITE);
		resource_table= new JTable() {
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
		resource_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		resource_table.setBorder(null);
		resource_table.setFont(new Font("Arial", Font.PLAIN, 12));
		resource_table.setForeground(Color.DARK_GRAY);
		setLayout(null);
		resource_table.setModel(resource_table_model);
		resource_table.setBounds(0, 0, 503, 240);

		// JScrollPane sc = new JScrollPane(table_2);
		JScrollPane scrollPane = new JScrollPane(resource_table);
		scrollPane.setBounds(0, 0, 564, 220);
		add(scrollPane);
		
		resource_db = new ResourceDatabase();
	}
	public static void modify_resource_table() {
		int resource_rows_num = Resource_tab.resource_table_model.getRowCount();

		for (int i = resource_rows_num - 1; i >= 0; i--) {
			resource_table_model.removeRow(i);
		}
		int resourceListSize = resource_db.resource_list.size();
		for (int i = 0; i < resourceListSize; i++) {
			resource_table_model.addRow(new Object[] { false, "EMA"+resource_db.resource_list.get(i).gateway_id,
					resource_db.resource_list.get(i).name, resource_db.resource_list.get(i).mode,
					resource_db.resource_list.get(i).power, resource_db.resource_list.get(i).priority });
		}
	}
}
