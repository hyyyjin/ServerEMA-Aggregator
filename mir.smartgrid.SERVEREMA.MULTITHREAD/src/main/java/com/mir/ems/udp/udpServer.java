package com.mir.ems.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.mir.ems.udp.udpServer.sendUDPThread;

public class udpServer extends Thread {
	UdpMessageHandler udpMessageHandler;
	public static DatagramSocket serverSocket;

	public udpServer() {
		try {
			serverSocket = new DatagramSocket(9090);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		Runnable p = new check();
		Thread p4 = new Thread(p);
		p4.start();
		System.out.println("udpServer is Starting");
		byte[] recvData = new byte[1024];
		byte[] sendData = new byte[1024];

		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(recvData, recvData.length);
			try {

				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData());

				InetAddress vtnIP = receivePacket.getAddress();
				int vtnPort = receivePacket.getPort();
				udpMessageHandler = new UdpMessageHandler(sentence, vtnIP, vtnPort);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static class sendUDPThread implements Runnable {

		InetAddress IPAddress;
		String payload;
		int port;

		public sendUDPThread(InetAddress IPAddress, int port, String payload) {
			this.IPAddress = IPAddress;
			this.payload = payload;
			this.port = port;
		}

		public void run() {

			DatagramPacket sendPacket = new DatagramPacket(payload.getBytes(), payload.getBytes().length, IPAddress,
					port);
			try {
				serverSocket.send(sendPacket);
				System.out.println("send:::"+payload);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static class check implements Runnable {

		public void run() {
			while (true) {
				Scanner sc = new Scanner(System.in);
				String name = sc.nextLine();
				if (name.equals("a")) {
					System.out.println("hi" + name);
					Object[] Keyset = UdpMessageHandler.vtnHash.keySet().toArray();
					System.out.println(Keyset.toString());
					System.out.println(UdpMessageHandler.vtnHash.get(Keyset[0]).toString());

				}
			}
		}
	}
}
