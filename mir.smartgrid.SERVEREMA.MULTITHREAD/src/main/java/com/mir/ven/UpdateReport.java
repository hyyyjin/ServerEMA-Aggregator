package com.mir.ven;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.SocketException;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mir.ems.globalVar.global;
import com.mir.update.database.cema_database;

public class UpdateReport extends Thread {

	HttpClientBuilder hcb = HttpClientBuilder.create();
	HttpClient client = hcb.build();
	HttpPost post;
	private StringBuilder httpResponse_updateR = new StringBuilder();
	HttpClientBuilder hcb_updateReport = HttpClientBuilder.create();
	HttpClient client_updateReport = hcb_updateReport.build();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			sendUpdate();
		} catch (UnsupportedOperationException | IOException | ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendUpdate() throws UnsupportedOperationException, IOException, ParserConfigurationException,
			SAXException, TransformerException {

		HttpResponse updateReport_response;

		String time = new TimeFormat().getCurrentTime();

		HttpPost post_updateReport = new HttpPost(global.vtnURL + "EiReport");
		post_updateReport.setEntity(new StringEntity(new VENImpl().UpdateReport()));

		cema_database cd = new cema_database();
		cd.buildup(global.VEN_NAME, "qos", "state", global.currentVal, -1, -1, -1, -1, -1, -1, -1, time, time, -1);

		try {
			Thread.sleep(global.reportInterval);
			updateReport_response = client_updateReport.execute(post_updateReport);
			BufferedReader upd_rd = new BufferedReader(
					new InputStreamReader(updateReport_response.getEntity().getContent()));
			String upd_line = "";
			httpResponse_updateR = new StringBuilder();
			while ((upd_line = upd_rd.readLine()) != null) {
				setHttpUPDResponse(upd_line);
			}

		} catch (SocketException e) {
			client = hcb.build();
		}

		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (getHttpUPDResponse().toString().contains("oadrUpdatedReport"))
			sendUpdate();

	}

	public StringBuilder getHttpUPDResponse() {
		return httpResponse_updateR;
	}

	public void setHttpUPDResponse(String httpResponse) {
		this.httpResponse_updateR = this.httpResponse_updateR.append(httpResponse);
	}

}
