package com.mir.ems.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Dimension;

import java.awt.Font;

import com.mir.ems.globalVar.global;
import com.mir.ems.main.EmaMainClass;

import java.awt.Color;

import javax.swing.border.EtchedBorder;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class Initial extends JFrame {
	private EmaMainClass main;
	private JPasswordField passText;
	private JTextField userText;
	private boolean bLoginCheck;
	private JTextField mqttBrokerIPtextField;
	private JTextField mqttBrokerPorttextField;
	private JTextField qos_textField;
	private JTextField textField_2;
	private JTextField textField_3;
	
	public JComboBox<String> comboBox_2;
	public JComboBox<String> model;
	public JComboBox<String> profileCombo;
	private JTextField textField_4;
	private JTextField textField_5;
	
	public Initial() {
		// setting
		setTitle("MIREnergy Management System");
		setSize(384, 792);
		setResizable(false);
		setLocation(800, 450);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		// panel
		JPanel panel = new JPanel();
		placeLoginPanel(panel);

		// add
		getContentPane().add(panel);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(12, 66, 354, 118);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel userLabel = new JLabel("Account");
		userLabel.setBounds(39, 35, 80, 25);
		panel_1.add(userLabel);
		userLabel.setFont(new Font("Arial", Font.BOLD, 13));

		JLabel passLabel = new JLabel("Passcode");
		passLabel.setBounds(39, 70, 80, 25);
		panel_1.add(passLabel);
		passLabel.setFont(new Font("Arial", Font.BOLD, 13));

		userText = new JTextField(20);
		userText.setText("test");
		userText.setBounds(219, 35, 123, 25);
		panel_1.add(userText);

		passText = new JPasswordField(20);
		passText.setToolTipText("");
		passText.setText("1234");
		passText.setBounds(219, 70, 123, 25);
		panel_1.add(passText);

		JLabel lblUser = new JLabel("User ");
		lblUser.setBounds(12, 10, 57, 15);
		panel_1.add(lblUser);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(12, 194, 354, 350);
		panel.add(panel_2);
		panel_2.setLayout(null);

		final JLabel mqttIPlabel = new JLabel("MQTT Broker: IP");
		mqttIPlabel.setFont(new Font("Arial", Font.BOLD, 13));
		mqttIPlabel.setBounds(39, 75, 154, 25);
		panel_2.add(mqttIPlabel);

		final JLabel mqttPortlabel = new JLabel("MQTT Broker: Port");
		mqttPortlabel.setFont(new Font("Arial", Font.BOLD, 13));
		mqttPortlabel.setBounds(39, 110, 154, 25);
		panel_2.add(mqttPortlabel);

		mqttBrokerIPtextField = new JTextField();
		mqttBrokerIPtextField.setText("166.104.28.51");
		mqttBrokerIPtextField.setBounds(219, 75, 123, 25);
		panel_2.add(mqttBrokerIPtextField);
		mqttBrokerIPtextField.setColumns(10);

		mqttBrokerPorttextField = new JTextField();
		mqttBrokerPorttextField.setText("1883");
		mqttBrokerPorttextField.setBounds(219, 110, 123, 25);
		panel_2.add(mqttBrokerPorttextField);
		mqttBrokerPorttextField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Protocol");
		lblNewLabel_2.setFont(new Font("Arial", Font.BOLD, 13));
		lblNewLabel_2.setBounds(39, 40, 154, 15);
		panel_2.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Connection Info");
		lblNewLabel_3.setBounds(12, 10, 103, 15);
		panel_2.add(lblNewLabel_3);

		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setForeground(Color.BLACK);
		comboBox.setBackground(Color.WHITE);
		comboBox.setBounds(219, 35, 123, 21);

		comboBox.addItem("MQTT");
		comboBox.addItem("CoAP");
		comboBox.addItem("BOTH");
		comboBox.addItem("UDP");
		
		comboBox.setSelectedIndex(0);

		panel_2.add(comboBox);
		
		JLabel lblQos = new JLabel("MQTT Broker: QOS");
		lblQos.setFont(new Font("Arial", Font.BOLD, 13));
		lblQos.setBounds(39, 145, 154, 25);
		panel_2.add(lblQos);
		
		qos_textField = new JTextField();
		qos_textField.setText("0");
		qos_textField.setColumns(10);
		qos_textField.setBounds(219, 145, 123, 25);
		panel_2.add(qos_textField);
		
		JLabel lblUdpServer = new JLabel("UDP: Server IP");
		lblUdpServer.setFont(new Font("Arial", Font.BOLD, 13));
		lblUdpServer.setBounds(39, 264, 154, 25);
		panel_2.add(lblUdpServer);
		
		textField_2 = new JTextField();
		textField_2.setText("192.168.1.199");
		textField_2.setColumns(10);
		textField_2.setBounds(219, 264, 123, 25);
		panel_2.add(textField_2);
		
		JLabel lblUdpPort = new JLabel("UDP: Server Port");
		lblUdpPort.setFont(new Font("Arial", Font.BOLD, 13));
		lblUdpPort.setBounds(39, 299, 154, 25);
		panel_2.add(lblUdpPort);
		
		textField_3 = new JTextField();
		textField_3.setText("12346");
		textField_3.setColumns(10);
		textField_3.setBounds(219, 299, 123, 25);
		panel_2.add(textField_3);
		
		JLabel lblCoapServer = new JLabel("Coap: Server IP");
		lblCoapServer.setFont(new Font("Arial", Font.BOLD, 13));
		lblCoapServer.setBounds(39, 180, 154, 25);
		panel_2.add(lblCoapServer);
		
		textField_4 = new JTextField();
		textField_4.setText("192.168.1.101");
		textField_4.setColumns(10);
		textField_4.setBounds(219, 180, 123, 25);
		panel_2.add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setText("5683");
		textField_5.setColumns(10);
		textField_5.setBounds(219, 215, 123, 25);
		panel_2.add(textField_5);
		
		JLabel lblCoapServerPort = new JLabel("Coap: Server Port");
		lblCoapServerPort.setFont(new Font("Arial", Font.BOLD, 13));
		lblCoapServerPort.setBounds(39, 215, 154, 25);
		panel_2.add(lblCoapServerPort);

		
		java.net.URL url = Initial.class.getResource("/IMAGE/123.png");

		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setIcon(new ImageIcon(url));
		lblNewLabel_4.setBounds(196, 0, 182, 65);
		lblNewLabel_4.setPreferredSize(new Dimension(100, 100));
		panel.add(lblNewLabel_4);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_3.setBounds(12, 554, 354, 164);
		panel.add(panel_3);
		
		JLabel lblConfig = new JLabel("Configuration");
		lblConfig.setBounds(12, 10, 95, 15);
		panel_3.add(lblConfig);
		
		JLabel label = new JLabel("Profile");
		label.setFont(new Font("Arial", Font.BOLD, 13));
		label.setBounds(39, 40, 76, 15);
		panel_3.add(label);
		
		profileCombo = new JComboBox<String>();
		profileCombo.addItem("EMAP1.0b");
		profileCombo.addItem("EMAP");
		profileCombo.addItem("OpenADR2.0b");
		profileCombo.addItem("OpenADR2.0b_new");

		profileCombo.setSelectedIndex(0);
		profileCombo.setForeground(Color.BLACK);
		profileCombo.setBackground(Color.WHITE);
		profileCombo.setBounds(219, 37, 123, 21);
		panel_3.add(profileCombo);
		
		model = new JComboBox<String>();
		
		model.addItem("Pull");
		model.addItem("Push");

		model.setSelectedIndex(0);
		model.setForeground(Color.BLACK);
		model.setBackground(Color.WHITE);
		model.setBounds(219, 120, 123, 21);
		panel_3.add(model);
		
		JLabel lblModel = new JLabel("Model");
		lblModel.setFont(new Font("Arial", Font.BOLD, 13));
		lblModel.setBounds(39, 123, 76, 15);
		panel_3.add(lblModel);
		
		comboBox_2 = new JComboBox<String>();
		comboBox_2.addItem("Explicit");
		comboBox_2.addItem("Implicit");
		comboBox_2.setSelectedIndex(0);
		comboBox_2.setForeground(Color.BLACK);
		comboBox_2.setBackground(Color.WHITE);
		comboBox_2.setBounds(219, 76, 123, 21);
		panel_3.add(comboBox_2);
		
		JLabel label_1 = new JLabel("Report");
		label_1.setFont(new Font("Arial", Font.BOLD, 13));
		label_1.setBounds(39, 79, 76, 15);
		panel_3.add(label_1);
		
		JButton reset_button = new JButton("Reset");
		reset_button.setBounds(52, 728, 100, 25);
		panel.add(reset_button);
		
		JButton cfm_button_1 = new JButton("Confirm");
		cfm_button_1.setBounds(165, 728, 100, 25);
		panel.add(cfm_button_1);
		
		textField_4.setEnabled(false);
		textField_5.setEnabled(false);


		
		comboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
	
				if(comboBox.getSelectedItem().toString().equals("MQTT")){
					
					qos_textField.setEnabled(true);
					mqttBrokerIPtextField.setEnabled(true);
					mqttBrokerPorttextField.setEnabled(true);
					
					textField_4.setEnabled(false);
					textField_5.setEnabled(false);

					global.setProtocol_type_global("MQTT");
					global.tempIP = mqttBrokerIPtextField.getText();
					global.tempPort = mqttBrokerPorttextField.getText();
					global.qos = Integer.parseInt(qos_textField.getText());


				}
				else if(comboBox.getSelectedItem().toString().equals("CoAP")){
					
					textField_4.setEnabled(true);
					textField_5.setEnabled(true);
					
					qos_textField.setEnabled(false);
					mqttBrokerIPtextField.setEnabled(false);
					mqttBrokerPorttextField.setEnabled(false);
					

					global.setProtocol_type_global("CoAP");
					global.coapServerIP = textField_4.getText();
					global.coapServerPort = textField_5.getText();
					
				}
				else if(comboBox.getSelectedItem().toString().equals("UDP")){
					
//					lblNewLabel.setVisible(false);
//					lblNewLabel_1.setVisible(false);
//					textField.setVisible(false);
//					textField_1.setVisible(false);
//					
					global.setProtocol_type_global("UDP");
				}
				else if(comboBox.getSelectedItem().toString().equals("BOTH")){
					
					qos_textField.setEnabled(true);
					mqttBrokerIPtextField.setEnabled(true);
					mqttBrokerPorttextField.setEnabled(true);
					textField_4.setEnabled(true);
					textField_5.setEnabled(true);
					
					global.tempIP = mqttBrokerIPtextField.getText();
					global.tempPort = mqttBrokerPorttextField.getText();
					global.qos = Integer.parseInt(qos_textField.getText());

					global.coapServerIP = textField_4.getText();
					global.coapServerPort = textField_5.getText();
					
					global.setProtocol_type_global("BOTH");
				}
			}
		});

		
		
		reset_button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				userText.setText("");
				passText.setText("");
			}
		});
		cfm_button_1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				isLoginCheck();
			}
		});

		
		userText.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {

				isLoginCheck();
				
			}
		});
		
		passText.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				isLoginCheck();
			}
		});

		setVisible(true);
	}

	public void placeLoginPanel(JPanel panel) {
		panel.setLayout(null);

	}

	
	public void isLoginCheck() {
		
		if (userText.getText().equals("test") && new String(passText.getPassword()).equals("1234")) {
			bLoginCheck = true;
			
			if (isLogin()) {
				global.tempIP = mqttBrokerIPtextField.getText();
				global.tempPort = mqttBrokerPorttextField.getText();

				global.coapServerIP = textField_4.getText();
				global.coapServerPort = textField_5.getText();
				
				global.udpIP = textField_2.getText();
				global.udpPort = Integer.parseInt(textField_3.getText());

				global.reportType = comboBox_2.getSelectedItem().toString();
				global.profile = profileCombo.getSelectedItem().toString();

				global.qos = Integer.parseInt(qos_textField.getText());
				
				global.communicationModel = model.getSelectedItem().toString();
				
				main.showFrameTest(); 
			}
		} else {
			JOptionPane.showMessageDialog(null, "Faild");
		}
	}

	public void setMain(EmaMainClass main) {
		this.main = main;
	}

	public boolean isLogin() {
		return bLoginCheck;
	}
}
