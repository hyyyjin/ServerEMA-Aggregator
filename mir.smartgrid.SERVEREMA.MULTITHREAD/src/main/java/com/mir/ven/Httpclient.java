package com.mir.ven;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.mir.ems.globalVar.global;
import com.mir.update.database.StoreData_cema;

public class Httpclient extends Thread {

	@Override
	public void run() {
		try {
			// Thread.sleep(1000);

			new HTTPRequest("EiRegisterParty").getHttpResponse();
		} catch (ParserConfigurationException | SAXException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
