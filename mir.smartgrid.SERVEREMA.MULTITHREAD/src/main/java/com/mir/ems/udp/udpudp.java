package com.mir.ems.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class udpudp {

	String ipADDR;
	int port;
	public udpudp(String ipADDR, int port) {

		this.ipADDR= ipADDR;
		this.port = port;
		// BufferedReader inFromUser = new BufferedReader(new
		// InputStreamReader(System.in));
		DatagramSocket clientSocket;
		try {
			clientSocket = new DatagramSocket();
			InetAddress IPAddress;

			IPAddress = InetAddress.getByName(this.ipADDR);
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];
			String sentence = "Hellowrold";
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, this.port);
			clientSocket.send(sendPacket);
			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				String modifiedSentence = new String(receivePacket.getData());
				System.out.println("FROM SERVER:" + modifiedSentence);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// clientSocket.close();
	}

	// BufferedReader inFromUser = new BufferedReader(new
	// InputStreamReader(System.in));

}
