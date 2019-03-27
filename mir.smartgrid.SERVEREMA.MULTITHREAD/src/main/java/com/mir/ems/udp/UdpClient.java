package com.mir.ems.udp;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.deviceProfile.Led_tab;
import com.mir.ems.globalVar.global;

public class UdpClient extends Thread {

	String udpServerIP;
	int port;

	public UdpClient(String udpServerIP, int port) {
		setPort(port);
		setUdpServerIP(udpServerIP);

	}

	public static String setUpdateReportPayload(String payload, String reportType, String format) {
		String setUpdatePayload = payload;

		if (format.equals("text")) {

			if (reportType.equals("Explicit")) {
				setUpdatePayload += "Explicit/";

				Iterator<String> keys = global.emaProtocolCoAP_Device.keySet().iterator();
				while (keys.hasNext()) {
					String dev_key = keys.next();

					setUpdatePayload += getDateYMD("YYYYMMdd") + "/" + getDateYMD("HHmmss") + "/" + dev_key + "/" + 200.1
							+ "/" + "PT2S" + "/" + "TELEMETRY_USAGE" + "/";

				}

			} else if (reportType.equals("Implicit")) {
				setUpdatePayload += "Implicit/";

				Iterator<String> keys = global.emaProtocolCoAP_Device.keySet().iterator();
				double totalPower= 0;
				while (keys.hasNext()) {
					String dev_key = keys.next();

					totalPower += global.emaProtocolCoAP_Device.get(dev_key).getPower();
				}
				
				setUpdatePayload += getDateYMD("YYYYMMdd") + "/" + getDateYMD("HHmmss") + "/" + global.getSYSTEM_ID() + "/" + totalPower
						+ "/" + "PT2S" + "/" + "TELEMETRY_USAGE";
			}

		} else if (format.equals("json")) {

		}
//		System.out.println(setUpdatePayload);

		return setUpdatePayload;
	}

	@Override
	public void run() {
		DatagramSocket clientSocket;
		try {

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

						clientUpdateSocket = new DatagramSocket();
						InetAddress IPAddress = InetAddress.getByName(udpServerIP);
						byte[] sendUpdateData = new byte[60000];
						
						String updateReport = "EiUpdate/";

						sendUpdateData = setUpdateReportPayload(updateReport, global.reportType, "text").getBytes();
						DatagramPacket sendUpdatePacket = new DatagramPacket(sendUpdateData, sendUpdateData.length, IPAddress, getPort());

						clientUpdateSocket.send(sendUpdatePacket);
					} catch (Exception e) {

					}
				}
			};
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(chartUpdaterTask, 1000, 2000);

			while (true) {

				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				String modifiedSentence = new String(receivePacket.getData());
				// System.out.println("FROM SERVER:" + modifiedSentence);

				String[] drMSG = modifiedSentence.split(",");
				int type = 0, strday = 0, strtime = 0, endday = 0, endtime = 0;
				double value = 0;

				type = 1;

				String[] strday_str = drMSG[0].split("2018");

				strday = Integer.parseInt("2018" + strday_str[1]);
				strtime = Integer.parseInt(drMSG[1]);
				endday = Integer.parseInt(drMSG[2]);
				endtime = Integer.parseInt(drMSG[3]);
				System.out.println("@@2");
				System.out.println(drMSG[4]);
				System.out.println("@@@");
				value = Double.parseDouble(drMSG[4]);

				// if (global.getProtocol_type_global().equals("MQTT")) {
				//
				// global.initiater.eventOccur(type, strday, strtime, endday,
				// endtime, value);
				// } else {
				//
				// if
				// (global.getObs_emaProtocolCoAP_EventFlag().containsKey("1"))
				// {
				//
				// global.getObs_emaProtocolCoAP_EventFlag().get("1").setEventFlag(true).setStartYMD(strday)
				// .setStartTime(strtime).setEndYMD(endday).setEndTime(endtime).setThreshold(value);
				//
				// }
				//
				// }

			}

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

	public UdpClient setUdpServerIP(String udpServerIP) {
		this.udpServerIP = udpServerIP;
		return this;
	}

	public int getPort() {
		return port;
	}

	public UdpClient setPort(int port) {
		this.port = port;
		return this;
	}

	public static void main(String[] args) {

		
//		System.out.println(getDateYMD("YYYYMMdd"));
		
		
//		Emap_Device_Profile deviceProfile = new Emap_Device_Profile("CEMA1", "LED" + i, "LED", "ON", 0, 0, 1, 100.1, 0.0,
//				0.0, 0.0, 0.0, 0.0);
//		global.emaProtocolCoAP_Device.put("LED" , deviceProfile);
//		
//		for (int i = 0; i < 5; i++) {
//
//			// EMAP_CoAP_Device에 Date 항목이 추가되어야함.
//			Emap_Device_Profile deviceProfile = new Emap_Device_Profile("CEMA1", "LED" + i, "LED", "ON", 0, 0, 1, 100.1, 0.0,
//					0.0, 0.0, 0.0, 0.0);
//			global.emaProtocolCoAP_Device.put("LED" + i, deviceProfile);
//		}

//		System.out.println(setUpdateReportPayload("EiUpdate/", global.reportType, "text"));

		
		new UdpClient("192.168.1.125", 12345).start();
		

	}

	public static String getDateYMD(String timeFormat) {
		return new SimpleDateFormat(timeFormat).format(System.currentTimeMillis());
	}

	public static String getDateTime(String timeFormat) {
		return new SimpleDateFormat(timeFormat).format(System.currentTimeMillis());
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
