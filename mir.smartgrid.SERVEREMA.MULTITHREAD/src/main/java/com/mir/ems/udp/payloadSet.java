package com.mir.ems.udp;

import java.util.Arrays;

public class payloadSet {
	
	public payloadSet(){

	}
	
	public String payloadMsgSet(String message){
		
		int msgLength = 0;
		char[] msgChar = message.toCharArray();
		String strMsg;
		
		for (int i = 0; i < msgChar.length; i++) {
			if (msgChar[i] == '}') {
				msgLength = i;
				break;
			}
		}
		char[] msg = Arrays.copyOfRange(msgChar, 0, msgLength + 1);
		
		strMsg = new String(msg);

		return strMsg;
	}
}
