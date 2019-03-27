package com.mir.ems.GUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mir.ems.Graph.EMARealTimeGraph;
import com.mir.ems.Graph.EMAScheduleGraph;
import com.mir.ems.Graph.EnergyGraph;
import com.mir.ems.database.GatewayDatabase;
import com.mir.ems.deviceProfile.Ess_tab;
import com.mir.ems.deviceProfile.GatewaySettingPanel;
import com.mir.ems.deviceProfile.Led_tab;
import com.mir.ems.deviceProfile.Pv_tab;
import com.mir.ems.deviceProfile.Recloser_tab;
import com.mir.ems.deviceProfile.Resource_tab;
import com.mir.ems.deviceProfile.SmartMeter_tab;
import com.mir.ems.deviceProfile.VTN_tab;
import com.mir.ems.globalVar.RegisteredFileReader;
import com.mir.ems.globalVar.global;
import com.mir.ems.deviceProfile.EMA_tab;
import com.mir.ems.deviceProfile.EMA_tab_temp;
import com.mir.ems.monitoring.MicrogridSummary;
import com.mir.ems.price.PriceFileReader;
import com.mir.ems.price.RealTimePriceFileReader;
import com.mir.ems.topTab.DRScheduling;
import com.mir.ems.topTab.EmaTopology;

import javax.swing.JTabbedPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener, MouseListener {

	private JPanel contentPane;
	public static GatewayDatabase gateway_db;
	public static JRadioButtonMenuItem rdbtnmntmNewRadioItem;
	public static JRadioButtonMenuItem rdbtnmntmNewRadioItem_1;

	/*
	 * Launch the application.
	 */

	public MainFrame() {

		setTitle("MIR Energy Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1500, 992);
		setResizable(false);
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Open");
		mnNewMenu.add(mntmNewMenuItem_1);

		JMenuItem mntmNewMenuItem = new JMenuItem("Exit");
		mnNewMenu.add(mntmNewMenuItem);

		JMenu mnNewMenu_3 = new JMenu("Scenario");
		menuBar.add(mnNewMenu_3);

		rdbtnmntmNewRadioItem = new JRadioButtonMenuItem("Passive");
		mnNewMenu_3.add(rdbtnmntmNewRadioItem);
		rdbtnmntmNewRadioItem.setSelected(true);

		rdbtnmntmNewRadioItem_1 = new JRadioButtonMenuItem("Active");
		mnNewMenu_3.add(rdbtnmntmNewRadioItem_1);

		JMenu mnNewMenu_1 = new JMenu("Energy Optimization Algorithm");
		menuBar.add(mnNewMenu_1);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Priority");
		mnNewMenu_1.add(mntmNewMenuItem_2);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Maximal Matching");
		JMenuItem mntmNewMenuItem_game = new JMenuItem("Game Theory");
		JMenuItem mntmNewMenuItem_negotiation = new JMenuItem("Negotiation");

		mnNewMenu_1.add(mntmNewMenuItem_3);
		mnNewMenu_1.add(mntmNewMenuItem_game);
		mnNewMenu_1.add(mntmNewMenuItem_negotiation);

		JMenu mnNewMenu_2 = new JMenu("Automated DR");
		menuBar.add(mnNewMenu_2);

//		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Provider");
//		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Facility");
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Emergency Demand Response");
		mnNewMenu_2.add(chckbxNewCheckBox);
//		mnNewMenu_2.add(mntmNewMenuItem_4);
//		mnNewMenu_2.add(mntmNewMenuItem_5);

		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(14, 43, 1467, 700);
		contentPane.add(tabbedPane);

		/*
		 * Top Menu
		 */
		EmaTopology emaTopology = new EmaTopology();
		tabbedPane.addTab("EMA Topology", null, emaTopology, null);

		MicrogridSummary microgridSummary = new MicrogridSummary();
		tabbedPane.addTab("Microgrid Summary", null, microgridSummary, null);

		EMARealTimeGraph emaRealTimeGraph = new EMARealTimeGraph();
		tabbedPane.addTab("Each EMA Energy Monitoring", null, emaRealTimeGraph, null);

		EnergyGraph energyGraph = new EnergyGraph();
		tabbedPane.addTab("RealTime Energy Monitoring", null, energyGraph, null);

		DRScheduling drScheduling = new DRScheduling();
		tabbedPane.addTab("Demand Resource Scheduling", null, drScheduling, null);

		EMAScheduleGraph schedulingMonitor = new EMAScheduleGraph();
		tabbedPane.addTab("Schedule Manager", null, schedulingMonitor, null);

		GatewaySettingPanel thresholdSetting = new GatewaySettingPanel();
		tabbedPane.addTab("Threshold Value Setting", null, thresholdSetting, null);

		/*
		 * 
		 * Bottom Menu - Left Side(Device info:: LED dev, ESS dev, PV dev)
		 * 
		 */

		JTabbedPane dev_tab = new JTabbedPane(JTabbedPane.TOP);
		dev_tab.setBounds(14, 753, 720, 166);
		contentPane.add(dev_tab);

		Led_tab led_panel = new Led_tab();
		dev_tab.addTab("Appliance", null, led_panel, null);

		Recloser_tab recloser_panel = new Recloser_tab();
		dev_tab.addTab("Recloser", null, recloser_panel, null);

		Resource_tab resource_panel = new Resource_tab();
		dev_tab.addTab("Resource", null, resource_panel, null);

		Ess_tab ess_panel = new Ess_tab();
		dev_tab.addTab("Battery", null, ess_panel, null);

		Pv_tab pv_panel = new Pv_tab();
		dev_tab.addTab("Solar", null, pv_panel, null);

		SmartMeter_tab smartMeter_panel = new SmartMeter_tab();
		dev_tab.addTab("SmartMeter", null, smartMeter_panel, null);

		/*
		 * Bottom Menu - Right Side(Gateway)
		 */
		JTabbedPane tabbedPane_6 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_6.setBounds(761, 753, 720, 166);
		contentPane.add(tabbedPane_6);

		EMA_tab_temp ema_panel1 = new EMA_tab_temp();
		tabbedPane_6.addTab("EMA temp", null, ema_panel1, null);

		EMA_tab ema_panel = new EMA_tab();
		tabbedPane_6.addTab("EMA Status", null, ema_panel, null);
		// tabbedPane_6.addTab("VEN Status", null, table, null);
		VTN_tab vtn_panel = new VTN_tab();
		tabbedPane_6.addTab("VTN Status", null, vtn_panel, null);

		JLabel lblNewLabel = new JLabel("Device Information");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 13));
		lblNewLabel.setBounds(14, 585, 156, 15);
		contentPane.add(lblNewLabel);

		JButton btnNewButton = new JButton("Network Configuration");
		btnNewButton.setBounds(1313, 10, 168, 23);
		contentPane.add(btnNewButton);

		/*
		 * WebView: This will run initFX as JavaFX-Thread
		 */

		// SwingUtilities.invokeLater(new Runnable() {
		// @Override
		// public void run() {
		// emaTopology.createViewer();
		// }
		// });

		initialize_database();
		new RegisteredFileReader("RegisteredEMAList.cfg");

		
		
		chckbxNewCheckBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (chckbxNewCheckBox.isSelected()) {
					
					
					global.EXPERIMENTAUTODR = true;
					
				}else{
					global.EXPERIMENTAUTODR = false;

				}
			}
		});
		
		rdbtnmntmNewRadioItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (rdbtnmntmNewRadioItem.isSelected()) {
					rdbtnmntmNewRadioItem_1.setSelected(false);
				}
			}
		});

		rdbtnmntmNewRadioItem_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (rdbtnmntmNewRadioItem_1.isSelected()) {
					rdbtnmntmNewRadioItem.setSelected(false);

					JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					jfc.setDialogTitle("Select an configuration file");
					jfc.setAcceptAllFileFilterUsed(false);
					FileNameExtensionFilter filter = new FileNameExtensionFilter(".cfg files", "cfg", "CFG");
					jfc.addChoosableFileFilter(filter);

					int returnValue = jfc.showOpenDialog(null);

					if (returnValue == JFileChooser.APPROVE_OPTION)
						new RealTimePriceFileReader(jfc.getSelectedFile().getPath());

				}
			}
		});

		mntmNewMenuItem_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle("Select an configuration file");
				jfc.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".cfg files", "cfg", "CFG");
				jfc.addChoosableFileFilter(filter);

				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION)
					new PriceFileReader(jfc.getSelectedFile().getPath());
			}
		});

	}

	public void initialize_database() {

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
