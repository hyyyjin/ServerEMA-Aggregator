package com.mir.ems.hashMap;

import java.net.InetAddress;

public class VTN_values {
	InetAddress vtnIP;
	int vtnPort, venNum;
	String vtnID, profileName, TransportName;
	double currentValue;
	
	public VTN_values(InetAddress vtnIP, int vtnPort, String vtnID, int venNum, String profileName, String TransportName, double currentValue){
		this.vtnIP = vtnIP;
		this.vtnPort = vtnPort;
		this.vtnID = vtnID;
		this.venNum = venNum;
		this.profileName = profileName;
		this.TransportName = TransportName;
		this.currentValue = currentValue;
		
	}
	public String toString() {
		return vtnIP+"/"+vtnPort + "/" + vtnID + "/" + venNum + "/"
				+ profileName + "/" + TransportName + "/" + currentValue;
	}
}
