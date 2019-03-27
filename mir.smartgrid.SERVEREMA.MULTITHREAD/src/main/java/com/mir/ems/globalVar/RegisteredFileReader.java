package com.mir.ems.globalVar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import com.mir.ems.deviceProfile.GatewaySettingPanel;

public class RegisteredFileReader {

	private String fileName;

	public RegisteredFileReader(String fileName) {

		setFileName(fileName);
		String type = null;

		try {
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringTokenizer st;

			while ((type = bufferedReader.readLine()) != null) {

				st = new StringTokenizer(type, "/");
				
				String emaName = st.nextToken();
				String emaHashName = st.nextToken();

				global.emaRegister.put(emaName, emaHashName);
				
			}
			
			GatewaySettingPanel.modify_gateway_table();

			
			bufferedReader.close();
		} catch (FileNotFoundException ex) {

			System.out.println("Unable to open file '" + fileName + "'");

		} catch (IOException ex) {

			System.out.println("Error reading file '" + fileName + "'");

		}

	}
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
