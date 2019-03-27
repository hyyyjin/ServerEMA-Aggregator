package com.mir.ems.monitoring;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.mir.ems.hashMap.ESS_values;
import com.mir.ems.hashMap.PV_values;
import com.mir.ems.mqtt.HandleMqttMessage;

@SuppressWarnings("serial")
public class MicrogridSummary extends JPanel {
	public MicrogridSummary() {
		setBounds(14, 60, 1201, 513);
//		setForeground(Color.LIGHT_GRAY);
//		setBackground(Color.LIGHT_GRAY);

		setLayout(null);

		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(181, 140, 185, 15);
		add(lblStatus);

		final JTextField textAreaStatus = new JTextField();
		textAreaStatus.setHorizontalAlignment(SwingConstants.RIGHT);
//		textAreaStatus.setBackground(Color.LIGHT_GRAY);
		textAreaStatus.setForeground(Color.DARK_GRAY);
		textAreaStatus.setBorder(null);
		
		textAreaStatus.setEditable(false);
		textAreaStatus.setBounds(378, 136, 134, 24);
		add(textAreaStatus);
		textAreaStatus.setEditable(false);

		JLabel lblResource = new JLabel("Resource(non-controllable load)");
		lblResource.setBounds(181, 194, 185, 15);
		add(lblResource);

		final JTextField textAreaResource = new JTextField();
		textAreaResource.setHorizontalAlignment(SwingConstants.RIGHT);
//		textAreaResource.setBackground(Color.LIGHT_GRAY);
		textAreaResource.setForeground(Color.DARK_GRAY);
		textAreaResource.setBorder(null);
		;
		textAreaResource.setEditable(false);
		textAreaResource.setBounds(378, 190, 134, 24);
		add(textAreaResource);

		JLabel lblLocalGeneration = new JLabel("Local Generation");
		lblLocalGeneration.setBounds(181, 254, 185, 15);
		add(lblLocalGeneration);

		final JTextField textAreaLocalGeneration = new JTextField();
		textAreaLocalGeneration.setHorizontalAlignment(SwingConstants.RIGHT);
//		textAreaLocalGeneration.setBackground(Color.LIGHT_GRAY);
		textAreaLocalGeneration.setForeground(Color.DARK_GRAY);
		textAreaLocalGeneration.setBorder(null);
		;
		textAreaLocalGeneration.setEditable(false);
		textAreaLocalGeneration.setBounds(378, 250, 134, 24);
		add(textAreaLocalGeneration);

		JLabel lblGridexportNeg = new JLabel("Grid(export neg.)");
		lblGridexportNeg.setBounds(181, 314, 185, 15);
		add(lblGridexportNeg);

		final JTextField textAreaGridexportNeg = new JTextField();
		textAreaGridexportNeg.setHorizontalAlignment(SwingConstants.RIGHT);
//		textAreaGridexportNeg.setBackground(Color.LIGHT_GRAY);
		textAreaGridexportNeg.setForeground(Color.DARK_GRAY);
		textAreaGridexportNeg.setBorder(null);
		;
		textAreaGridexportNeg.setEditable(false);
		textAreaGridexportNeg.setBounds(378, 310, 134, 24);
		add(textAreaGridexportNeg);

		JLabel lblSolar = new JLabel("Solar");
		lblSolar.setBounds(664, 140, 134, 15);
		add(lblSolar);

		final JTextField textAreaSolar = new JTextField();
		textAreaSolar.setHorizontalAlignment(SwingConstants.RIGHT);
//		textAreaSolar.setBackground(Color.LIGHT_GRAY);
		textAreaSolar.setForeground(Color.DARK_GRAY);
		textAreaSolar.setBorder(null);
		;
		textAreaSolar.setEditable(false);
		textAreaSolar.setBounds(868, 136, 134, 24);
		add(textAreaSolar);

		JLabel lblBattery = new JLabel("Battery");
		lblBattery.setBounds(664, 194, 134, 15);
		add(lblBattery);

		final JTextField textAreaBattery = new JTextField();
		textAreaBattery.setHorizontalAlignment(SwingConstants.RIGHT);
//		textAreaBattery.setBackground(Color.LIGHT_GRAY);
		textAreaBattery.setForeground(Color.DARK_GRAY);
		textAreaBattery.setBorder(null);
		;
		textAreaBattery.setEditable(false);
		textAreaBattery.setBounds(868, 190, 134, 24);
		add(textAreaBattery);

		JLabel lblLocalGenerationtotal = new JLabel("Local Generation(total)");
		lblLocalGenerationtotal.setBounds(664, 254, 134, 15);
		add(lblLocalGenerationtotal);

		final JTextField textAreaGenerationtotal = new JTextField();
		textAreaGenerationtotal.setHorizontalAlignment(SwingConstants.RIGHT);
//		textAreaGenerationtotal.setBackground(Color.LIGHT_GRAY);
		textAreaGenerationtotal.setForeground(Color.DARK_GRAY);
		textAreaGenerationtotal.setBorder(null);
		;
		textAreaGenerationtotal.setEditable(false);
		textAreaGenerationtotal.setBounds(868, 250, 134, 24);
		add(textAreaGenerationtotal);

		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.LIGHT_GRAY);
		separator_1.setBounds(181, 171, 338, 2);
		add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.LIGHT_GRAY);
		separator_2.setBounds(181, 231, 338, 2);
		add(separator_2);

		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.LIGHT_GRAY);
		separator_3.setBounds(181, 290, 338, 2);
		add(separator_3);

		JSeparator separator_5 = new JSeparator();
		separator_5.setForeground(Color.LIGHT_GRAY);
		separator_5.setBounds(664, 171, 338, 2);
		add(separator_5);

		JSeparator separator_6 = new JSeparator();
		separator_6.setForeground(Color.DARK_GRAY);
		separator_6.setBounds(664, 231, 338, 2);
		add(separator_6);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("123.png"));
		lblNewLabel_1.setBounds(1015, 448, 174, 55);
		add(lblNewLabel_1);

		setVisible(true);
		

		new Thread(new Runnable() {
			PV_values pvValue;
			ESS_values essValue;

			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
						int essCnt = 0, pvCnt = 0;
						double totalPVPower = 0.0;
						double totalESSPower = 0.0;
						double essPowerTemp = 0;
						double pvPowerTemp = 0;

						Random random1 = new Random();
//						textAreaResource.setText(random1.nextInt(100) + "KW");
						Random random2 = new Random();
//						textAreaGridexportNeg.setText(random2.nextInt(100) + "KW");

						if(HandleMqttMessage.essValueCheck&&HandleMqttMessage.pvValueCheck)
							textAreaStatus.setText("Grid Connected");
						else if(HandleMqttMessage.essValueCheck&& !(HandleMqttMessage.pvValueCheck)){
							textAreaStatus.setText("Grid(ESS) Connected");
							 textAreaLocalGeneration.setText("NONE KW");
							 textAreaSolar.setText("NONE KW");

						}
						else if(!(HandleMqttMessage.essValueCheck)&& HandleMqttMessage.pvValueCheck){
							textAreaStatus.setText("Grid(PV) Connected");
							textAreaBattery.setText("NONE KW");

						}else{
							textAreaStatus.setText("Grid is not Connected");

						}
						
						
						if (HandleMqttMessage.essValueCheck) {

							Set<String> essKey = HandleMqttMessage.getEssHashMap().keySet();
							System.out.println("I get ESS!");
							System.out.println(essKey);

							Iterator<String> essIterator = essKey.iterator();
							while (essIterator.hasNext()) {
								String essKeys = essIterator.next();
								if (essKeys.contains("ESS")) {
									essCnt++;
									System.out.println(essCnt);
								}
							}
							for (int i = 1; i <= essCnt; i++) {
								System.out.println("¾ÆÀÌ°ª!"+i);
								essValue = HandleMqttMessage.getEssHashMap().get("ESS" + i);
								System.out.println("essValue"+essValue);
								String essToString = essValue.toString();
								System.out.println("essToString::" + essToString);
								String[] essValueArr = essToString.split("/");
								double essPower = Double.parseDouble(essValueArr[5]);
								System.err.println(essPower + ":::::");
								essPowerTemp += essPower;
								System.out.println("TEMPVALUE:::" + essPowerTemp);
							}

							totalESSPower = essPowerTemp;
							System.out.println("totalPVPower::" + totalESSPower);
							textAreaBattery.setText(totalESSPower + "KW");
							textAreaGenerationtotal.setText((totalPVPower + totalESSPower) + "KW");

						} 

						if (HandleMqttMessage.pvValueCheck) {

							Set<String> pvKey = HandleMqttMessage.getPvHashMap().keySet();
							System.out.println("I get PV!");
							System.out.println(pvKey);

							Iterator<String> pvIterator = pvKey.iterator();
							while (pvIterator.hasNext()) {
								String pvKeys = pvIterator.next();

								if (pvKeys.contains("PV")) {
									pvCnt++;
									System.out.println(pvCnt);

								}
							}
							for (int i = 1; i <= pvCnt; i++) {
								pvValue = HandleMqttMessage.getPvHashMap().get("PV" + i);
								System.out.println(pvValue);
								String pvToString = pvValue.toString();
								System.out.println(pvToString);
								String[] pvValueArr = pvToString.split("/");
								double pvPower = Double.parseDouble(pvValueArr[5]);
								System.err.println(pvPower + ":::::");
								pvPowerTemp += pvPower;
							}

							totalPVPower = pvPowerTemp;
							System.out.println("totalPVPower::" + totalPVPower);
							textAreaLocalGeneration.setText(totalPVPower + "KW");
							textAreaSolar.setText(totalPVPower + "KW");
							textAreaGenerationtotal.setText((totalPVPower + totalESSPower) + "KW");

						} 
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
