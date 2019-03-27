package com.mir.ems.globalVar;

public class test {

	
	public static void main(String[] args){
		
		String aa = "http://166.104.28.51:8080/OpenADR2/Simple/2.0b";
		
		String temp = aa.split(":")[1].replaceAll("//", "");
		System.out.println(temp);
		
	}
}
