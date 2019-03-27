package com.mir.ems.udp;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.globalVar.global;

public class UdpClient_virtual_Device extends Thread {

	String udpServerIP, emaID;
	int port;
	int POLLING_INTERVAL = 1;
	String reportType = "Implicit";
 
	public static void main(String[] args) {

		int gateway = 20;

		for (int i = 1; i <= gateway; i++) {
			Emap_Device_Profile deviceProfile = new Emap_Device_Profile("CLIENT_EMA" + i, "CLIENT_EMA" + i, "CEMA",
					"ON", 0, 0, 1, 100.1, 0.0, 0.0, 0.0, 0.0, 0.0);
			global.emaProtocolCoAP_Device.put("CLIENT_EMA" + i, deviceProfile);

			for (int j = 0; j < 5; j++) {

				// EMAP_CoAP_Device에 Date 항목이 추가되어야함.
				Emap_Device_Profile deviceProfile1 = new Emap_Device_Profile("CLIENT_EMA" + i, i + "0" + (j + 1), "LED",
						"ON", 0, 0, 1, 100.1, 0.0, 0.0, 0.0, 0.0, 0.0);
				global.emaProtocolCoAP_Device.put("CLIENT_EMA" + i + "0" + (j + 1), deviceProfile1);
			}
		}

		new UdpClient_virtual_Device("192.168.1.201", 12345, "CLIENT_EMA1").start();
		new UdpClient_virtual_Device("192.168.1.202", 12345, "CLIENT_EMA2").start();
		new UdpClient_virtual_Device("192.168.1.203", 12345, "CLIENT_EMA3").start();

		
//		for (int i = 0; i < gateway; i++) {
//			// new UdpClient_virtual_Device("192.168.1.125", 12345).start();
//
//			if (i < 9) {
//				new UdpClient_virtual_Device("192.168.1.10" + (i + 1), 12346, "CLIENT_EMA" + (i + 1)).start();
//			} else {
//				new UdpClient_virtual_Device("192.168.1.1" + (i + 1), 12346, "CLIENT_EMA" + (i + 1)).start();
//
//			}
//
//		}

	}
	
	public UdpClient_virtual_Device(String udpServerIP, int port, String emaID) {
		setPort(port);
		setUdpServerIP(udpServerIP);
		setEmaID(emaID);
	}

	public String setUpdateReportPayload(String payload, String reportType, String format) {
		String setUpdatePayload = payload;

		if (format.equals("text")) {

			if (reportType.equals("Explicit")) {
				setUpdatePayload += "Explicit/";

				
				double totalPower = 0;

				Iterator<String> keys = global.emaProtocolCoAP_Device.keySet().iterator();
				while (keys.hasNext()) {
					String dev_key = keys.next();

				
					if(global.emaProtocolCoAP_Device.get(dev_key).getDeviceType().contains("CEMA")){
						
						if(dev_key.equals(emaID)){
							setUpdatePayload += getDateYMD("YYYYMMdd") + "/" + getDateYMD("HHmmss") + "/" + dev_key + "/"
									+ 400.0 + "/" + "PT"+POLLING_INTERVAL+"S" + "/" + "TELEMETRY_USAGE" + "/";
						}

					}
					
					if(global.emaProtocolCoAP_Device.get(dev_key).getDeviceType().contains("LED")){
						
						if(global.emaProtocolCoAP_Device.get(dev_key).getEmaID().equals(emaID)){
							
							dev_key = dev_key.replaceAll("CLIENT_EMA", "");
							
							setUpdatePayload += getDateYMD("YYYYMMdd") + "/" + getDateYMD("HHmmss") + "/" + dev_key + "/"
									+ 80.0 + "/" + "PT"+POLLING_INTERVAL+"S" + "/" + "TELEMETRY_USAGE" + "/";
						}

					}
					
//					
//					
//					if (dev_key.contains(emaID)) {
//						setUpdatePayload += getDateYMD("YYYYMMdd") + "/" + getDateYMD("HHmmss") + "/" + dev_key + "/"
//								+ 400.0 + "/" + "PT2S" + "/" + "TELEMETRY_USAGE" + "/";
//					}
				}

			} else if (reportType.equals("Implicit")) {
				setUpdatePayload += "Implicit/";

				Iterator<String> keys = global.emaProtocolCoAP_Device.keySet().iterator();
				double totalPower = 0;
				while (keys.hasNext()) {
					String dev_key = keys.next();

					if (dev_key.contains(emaID)) {

						totalPower += global.emaProtocolCoAP_Device.get(dev_key).getPower();
					}
				}

				setUpdatePayload += getDateYMD("YYYYMMdd") + "/" + getDateYMD("HHmmss") + "/" + emaID + "/" + 400.0
						+ "/" + "PT"+POLLING_INTERVAL+"S" + "/" + "TELEMETRY_USAGE";
			}

		} else if (format.equals("json")) {

		}
		// System.out.println(setUpdatePayload);

		return setUpdatePayload;
	}

	@Override
	public void run() {
		DatagramSocket clientSocket;
		try {

			
//			System.out.println(udpServerIP);
			clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(udpServerIP);
			byte[] sendData = new byte[60000];
			byte[] receiveData = new byte[60000];
			String sentence = "InitialConnection";
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, getPort());
			clientSocket.send(sendPacket);

			TimerTask chartUpdaterTask = new TimerTask() {

				@Override
				public void run() {

					DatagramSocket clientUpdateSocket;
					try {

//						System.out.println("aa");
						
						clientUpdateSocket = new DatagramSocket();
						InetAddress IPAddress = InetAddress.getByName(udpServerIP);
						byte[] sendUpdateData = new byte[60000];

						String updateReport = "EiUpdate/";

						sendUpdateData = setUpdateReportPayload(updateReport, reportType, "text").getBytes();
						
						System.out.println(emaID+"\t"+setUpdateReportPayload(updateReport, reportType, "text"));
						
						DatagramPacket sendUpdatePacket = new DatagramPacket(sendUpdateData, sendUpdateData.length,
								IPAddress, getPort());

						clientUpdateSocket.send(sendUpdatePacket);
					} catch (Exception e) {

					}
				}
			};
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(chartUpdaterTask, 5000, 2000);

//			while (true) {
//
//				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//				clientSocket.receive(receivePacket);
//				String modifiedSentence = new String(receivePacket.getData());
//				// System.out.println("FROM SERVER:" + modifiedSentence);
//
//				String[] drMSG = modifiedSentence.split(",");
//				int type = 0, strday = 0, strtime = 0, endday = 0, endtime = 0;
//				double value = 0;
//
//				type = 1;
//
//				String[] strday_str = drMSG[0].split("2018");
//
//				strday = Integer.parseInt("2018" + strday_str[1]);
//				strtime = Integer.parseInt(drMSG[1]);
//				endday = Integer.parseInt(drMSG[2]);
//				endtime = Integer.parseInt(drMSG[3]);
//				System.out.println("@@2");
//				System.out.println(drMSG[4]);
//				System.out.println("@@@");
//				value = Double.parseDouble(drMSG[4]);
//
//				// if (global.getProtocol_type_global().equals("MQTT")) {
//				//
//				// global.initiater.eventOccur(type, strday, strtime, endday,
//				// endtime, value);
//				// } else {
//				//
//				// if
//				// (global.getObs_emaProtocolCoAP_EventFlag().containsKey("1"))
//				// {
//				//
//				// global.getObs_emaProtocolCoAP_EventFlag().get("1").setEventFlag(true).setStartYMD(strday)
//				// .setStartTime(strtime).setEndYMD(endday).setEndTime(endtime).setThreshold(value);
//				//
//				// }
//				//
//				// }
//
//			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getUdpServerIP() {
		return udpServerIP;
	}

	public UdpClient_virtual_Device setUdpServerIP(String udpServerIP) {
		this.udpServerIP = udpServerIP;
		return this;
	}

	public int getPort() {
		return port;
	}

	public UdpClient_virtual_Device setPort(int port) {
		this.port = port;
		return this;
	}



	public static String getDateYMD(String timeFormat) {
		return new SimpleDateFormat(timeFormat).format(System.currentTimeMillis());
	}

	public static String getDateTime(String timeFormat) {
		return new SimpleDateFormat(timeFormat).format(System.currentTimeMillis());
	}

	public String getEmaID() {
		return emaID;
	}

	public void setEmaID(String emaID) {
		this.emaID = emaID;
	}

	//
	//
	// int ymd = 20180712;
	// int time = 115000;
	//
	//
	// String date = ""+ymd+""+time;
	//
	// System.out.println(date);
	// SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//
	// try {
	// Date sDate = sdformat.parse(date);
	// System.out.println(sdformat.format(sDate));
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	//
	// System.out.println(System.currentTimeMillis());
	//

}
