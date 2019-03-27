package com.mir.ems.mqtt;

public class ProcessStatus{
	public static String process_value;
	
	public ProcessStatus(){
		
	}

	public static String getProcess_value() {
		return process_value;
	}

	public static void setProcess_value(String process_value) {
		ProcessStatus.process_value = process_value;
	}


}