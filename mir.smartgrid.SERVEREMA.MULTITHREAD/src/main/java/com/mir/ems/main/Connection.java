package com.mir.ems.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.mir.ems.coap.CoAPServer;
import com.mir.ems.coap.client.SummaryObs;
import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.Mqtt;
import com.mir.ems.mqtt.Mqtt_ClientEMA;
import com.mir.ems.mqtt.Mqtt_ServerEMA;
import com.mir.ems.mqtt.Publishing;
import com.mir.ems.mqtt.emap.PeriodicalSummaryReport;
import com.mir.ems.udp.UdpClient;
import com.mir.smartgrid.simulator.profile.emap.ConnectRegistration;
import com.mir.update.database.StoreData_cema;
import com.mir.ven.Httpclient;

public class Connection {

	String protocol_type;

	public String udpIP = "166.104.28.51";

	public int udpPort = 12346;

	public static Mqtt mqttclient = null;
	public static Mqtt mqttclient2 = null;
	public static Mqtt_ClientEMA mqttclient3 = null;
	public static Mqtt_ClientEMA mqttclient4 = null;
	public Mqtt_ServerEMA openADRmqttclient = null;
	public Mqtt_ServerEMA emapmqttclient = null;

	private MqttClient client;

	public static String mqttIP = "166.104.28.51";

	public static String mqttPort = "1883";

	public static String submqttIP = "166.104.28.131";

	// public static String submqttPort = "1883";

	// public static String semamqttIP = "166.104.28.51";
	//
	// public static String semamqttPort = "1883";

	public static int qos = 0;

	public boolean connection_Status = false;

	private String emaID;

	private String registrationID, reportType;
	private double threshold, minValue = Integer.MAX_VALUE, maxValue = Integer.MIN_VALUE, currentPower, avgValue,
			generate, storage;
	private String maxTime, minTime;
	private boolean pullModel;

	enum InitialProcedure {
		ConnectRegistration, QueryRegistration
	};

	public Connection(String protocol_type) {

		parseCFG();
		eventIDFileIO();
		setPullModel(global.communicationModel.equals("Pull") ? true : false);

		this.protocol_type = protocol_type;

		Random rand = new Random();

		int randomNum = rand.nextInt((10000000 - 20) + 1) + 20;

		if (protocol_type.equals("MQTT")) {
			try {

				mqttIP = global.tempIP;
				mqttPort = global.tempPort;
				qos = global.qos;

				submqttIP = global.SUBBROKER;

				String mqtt4 = "tcp://" + submqttIP + ":" + mqttPort;

				emapmqttclient = new Mqtt_ServerEMA(mqtt4, "SERVER_EMA13bbeeeeee" + randomNum, false, false, null,
						null);
				emapmqttclient.start();
				emapmqttclient.subscribe("/EMAP/" + global.SYSTEM_ID + "/1.0b/#", qos);

				openADRmqttclient = new Mqtt_ServerEMA(mqtt4, "SERVER_EMA13bbaaaaaaaaa" + randomNum, false, false, null,
						null);
				openADRmqttclient.start();
				openADRmqttclient.subscribe("/OpenADR/" + global.SYSTEM_ID + "/1.0b/#", qos);

				if (global.summaryReport) {
					setEmapmqttclient(emapmqttclient);
					new PeriodicalSummaryReport(this).start();
				}

				String mqtt3 = "tcp://" + mqttIP + ":" + mqttPort;
				//
				// mqttclient3 = new Mqtt_ClientEMA(mqtt3, "CLIENT" + randomNum,
				// false, false, null, null);
				// mqttclient3.start();
				// mqttclient3.subscribe("/EMAP/" + global.SYSTEM_ID +
				// "/1.0b/#", qos);

				// String mqtt3 = "tcp://" + mqttIP + ":" + mqttPort;
				//
				// if (global.profile.equals("OpenADR2.0b")) {
				//
				//
				// mqttclient3 = new Mqtt_ClientEMA(mqtt3,
				// "SERVER_EMA13bbbbb"+randomNum, false, false, null, null,
				// this);
				// this.client = mqttclient3.getMqttClient();
				//
				// String[] gwNum = global.SYSTEM_ID.split("A");
				// genericEmaID<Integer> ts = new
				// genericEmaID<Integer>(Integer.parseInt(gwNum[1]));
				// setEmaID(ts.getEmaID() + "");
				// mqttclient3.subscribe("gw/" + getEmaID() + "/#", 0);
				//// initialSessionSetup(InitialProcedure.QueryRegistration.toString());
				//
				// } else if (global.profile.equals("EMAP")) {
				// mqttclient3 = new Mqtt_ClientEMA(mqtt3,
				// "SERVER_EMA1bbzxc"+randomNum, false, false, null, null,
				// this);
				// this.client = mqttclient3.getMqttClient();
				//
				// setEmaID(global.SYSTEM_ID);
				// mqttclient3.subscribe("CEMA/" + getEmaID().toUpperCase() +
				// "/#", 0);
				//// initialSessionSetup(InitialProcedure.ConnectRegistration.toString());
				//
				// }
				// String mqtt4 = "tcp://" + submqttIP + ":" + mqttPort;

				if (global.CLIENTOPTION) {
					if (global.profile.equals("EMAP1.0b")) {

						setEmaID(global.CHILD_ID);
						mqttclient4 = new Mqtt_ClientEMA(mqtt3, "SERVER_EMA1EMA3bbzxc" + randomNum, false, false, null,
								null, this);
						this.client = mqttclient4.getMqttClient();

						mqttclient4.subscribe("/EMAP/" + global.CHILD_ID + "/1.0b/#", 0);

						System.out.println("보냄?");
						initialSessionSetup(InitialProcedure.ConnectRegistration.toString());

					} else if (global.profile.equals("OpenADR2.0b_new")) {

						setEmaID(global.CHILD_ID);
						mqttclient4 = new Mqtt_ClientEMA(mqtt3, "SERVER_EMA1sssss" + randomNum, false, false, null,
								null, this);
						this.client = mqttclient4.getMqttClient();

						mqttclient4.subscribe("/OpenADR/" + global.CHILD_ID + "/1.0b/#", 0);

						initialSessionSetup(InitialProcedure.ConnectRegistration.toString());

					}
				}
				connection_Status = true;

			} catch (MqttException e) {

				e.printStackTrace();

				connection_Status = false;

			}

		} else if (protocol_type.equals("CoAP")) {

			CoAPServer coapServer = new CoAPServer();

			setEmaID(global.CHILD_ID);
			if (global.CLIENTOPTION) {

				com.mir.ems.coap.client.CoAPClient coapClient;
				coapClient = new com.mir.ems.coap.client.CoAPClient(this);
				coapClient.start();
			}
			connection_Status = true;

		} else if (protocol_type.equals("BOTH")) {

			CoAPServer coapServer = new CoAPServer();

			connection_Status = true;

			try {
				String mqtt4 = "tcp://" + submqttIP + ":" + mqttPort;

				Mqtt_ServerEMA emapmqttclient = new Mqtt_ServerEMA(mqtt4, "SERVER_EMA1EMA3bbff" + randomNum, false,
						false, null, null);

				emapmqttclient.start();

				System.out.println("Connected to Mqtt Broker" + mqtt4);
				System.out.println("MQTT SERVER ON!");

				emapmqttclient.subscribe("/EMAP/" + global.SYSTEM_ID + "/1.0b/#", qos);

				openADRmqttclient = new Mqtt_ServerEMA(mqtt4, "SERVER_EMA1EMA3bbee" + randomNum, false, false, null,
						null);

				openADRmqttclient.start();

				System.out.println("Connected to Mqtt Broker" + mqtt4);
				System.out.println("MQTT SERVER ON!");

				openADRmqttclient.subscribe("/OpenADR/" + global.SYSTEM_ID + "/1.0b/#", qos);

				if (global.summaryReport) {
					new PeriodicalSummaryReport(this).start();
				}

				String mqtt3 = "tcp://" + mqttIP + ":" + mqttPort;

				if (global.profile.equals("OpenADR2.0b")) {

					String[] gwNum = global.SYSTEM_ID.split("A");
					genericEmaID<Integer> ts = new genericEmaID<Integer>(Integer.parseInt(gwNum[1]));

					setEmaID(ts.getEmaID() + "");
					mqttclient3 = new Mqtt_ClientEMA(mqtt3, "cSERVER_EMA1sss" + randomNum, false, false, null, null,
							this);
					this.client = mqttclient3.getMqttClient();
					mqttclient3.subscribe("gw/" + getEmaID() + "/#", 0);
					// initialSessionSetup(InitialProcedure.QueryRegistration.toString());

				} else if (global.profile.equals("EMAP")) {

					setEmaID(global.SYSTEM_ID);
					mqttclient3 = new Mqtt_ClientEMA(mqtt3, "SERVER_EMA1wqeqwewq" + randomNum, false, false, null, null,
							this);
					this.client = mqttclient3.getMqttClient();
					mqttclient3.subscribe("CEMA/" + getEmaID().toUpperCase() + "/#", 0);
					// initialSessionSetup(InitialProcedure.ConnectRegistration.toString());

				} else if (global.profile.equals("EMAP1.0b")) {

					setEmaID(global.CHILD_ID);
					mqttclient4 = new Mqtt_ClientEMA(mqtt3, "SERVER_EMA1qqqqqq" + randomNum, false, false, null, null,
							this);
					this.client = mqttclient4.getMqttClient();
					mqttclient4.subscribe("/EMAP/" + global.CHILD_ID + "/1.0b/#", 0);

					// initialSessionSetup(InitialProcedure.ConnectRegistration.toString());

				} else if (global.profile.equals("OpenADR2.0b_new")) {

					setEmaID(global.CHILD_ID);
					mqttclient4 = new Mqtt_ClientEMA(mqtt3, "SERVER_EMA1xxxxx" + randomNum, false, false, null, null,
							this);
					this.client = mqttclient4.getMqttClient();
					mqttclient4.subscribe("/OpenADR/" + global.CHILD_ID + "/1.0b/#", 0);

					// initialSessionSetup(InitialProcedure.ConnectRegistration.toString());

				}

			} catch (Exception e) {

			}

		}

		setUdpIP(global.udpIP);
		setUdpPort(global.udpPort);

		// UdpClient udpClient = new UdpClient(getUdpIP(), getUdpPort());
		// udpClient.start();

		// try {
		// new StoreData_cema();
		// new StoreData_device();
		// global.databaseConnection.start();
		// } catch (ClassNotFoundException | SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// HTTP Client, HTTP XML VEN
		if (!global.CLIENTOPTION) {
			new Httpclient().start();
//			String temp = global.vtnURL;			
//			global.coapServerIP = temp.split(":")[1].replaceAll("//", "");
//			if(global.summaryReport){
//				new SummaryObs(global.CHILD_ID).start();
//			}
		}
		// udpServer udpServer = new udpServer();
		// udpServer.start();

	}

	public void initialSessionSetup(String inputProcedure) {

		InitialProcedure procedure = InitialProcedure.valueOf(inputProcedure);
		String setPayload = "";
		switch (procedure) {
		case ConnectRegistration:
			if (global.profile.equals("EMAP1.0b")) {

				com.mir.ems.profile.emap.v2.ConnectRegistration connectRegistration = new com.mir.ems.profile.emap.v2.ConnectRegistration();
				connectRegistration.setDestEMA(global.getParentnNodeID());
				connectRegistration.setRequestID("REQUESTID");
				connectRegistration.setService(procedure.toString());
				connectRegistration.setSrcEMA(getEmaID());
				connectRegistration.setTime(getCurrentTime(System.currentTimeMillis()));
				connectRegistration.setVersion(global.profile);
				setPayload = connectRegistration.toString();

				new Publishing().publishThread(this.client, "/EMAP/" + global.getParentnNodeID() + "/1.0b/SessionSetup",
						0, setPayload.getBytes());

			}

			else if (global.profile.equals("OpenADR2.0b_new")) {

				com.mir.ems.profile.openadr.recent.ConnectRegistration connectR = new com.mir.ems.profile.openadr.recent.ConnectRegistration();
				connectR.setRequestID("requestID");
				connectR.setService("oadrQueryRegistration");
				connectR.setSrcEMA(global.CHILD_ID);

				setPayload = connectR.toString();

				new Publishing().publishThread(this.client,
						"/OpenADR/" + global.getParentnNodeID() + "/2.0b/EiRegisterParty", 0, setPayload.getBytes());

			} else if (global.profile.equals("EMAP")) {
				setPayload = new ConnectRegistration(getEmaID(), global.getParentnNodeID(), 1, 1, 1, "Controllable",
						"ConnectRegistration", "Explicit", getCurrentTime(System.currentTimeMillis())).toString();

				new Publishing().publishThread(this.client,
						"SEMA/" + global.getParentnNodeID() + "/SessionSetup/ConnectRegistration", 0,
						setPayload.getBytes());

			}

			break;
		case QueryRegistration:

			// setPayload = new QueryRegistration(Integer.parseInt(getEmaID()),
			// "VEN_MIR" + getEmaID(), 2, 2).toString();

			new Publishing().publishThread(this.client, "EMS" + "/" + "oadrinit" + "/" + "QueryRegistration", 0,
					setPayload.getBytes());
			break;
		}
	}

	public String getCurrentTime(long currentTime) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
	}

	public double getGenerate() {
		return generate;
	}

	public void setGenerate(double generate) {
		this.generate = generate;
	}

	public double getStorage() {
		return storage;
	}

	public void setStorage(double storage) {
		this.storage = storage;
	}

	public static String getSubmqttIP() {

		return submqttIP;

	}

	public static void setSubmqttIP(String submqttIP) {

		Connection.submqttIP = submqttIP;

	}

	public String getUdpIP() {
		return udpIP;
	}

	public Connection setUdpIP(String udpIP) {
		this.udpIP = udpIP;
		return this;
	}

	public int getUdpPort() {
		return udpPort;
	}

	public Connection setUdpPort(int udpPort) {
		this.udpPort = udpPort;
		return this;
	}

	public String getEmaID() {
		return emaID;
	}

	public void setEmaID(String emaID) {
		this.emaID = emaID;
	}

	public String getRegistrationID() {
		return registrationID;
	}

	public void setRegistrationID(String registrationID) {
		this.registrationID = registrationID;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public double getCurrentPower() {
		return currentPower;
	}

	public void setCurrentPower(double currentPower) {
		this.currentPower = currentPower;
	}

	public double getAvgValue() {
		return avgValue;
	}

	public Connection setAvgValue(double avgValue) {

		if (this.avgValue <= 0) {
			this.avgValue = avgValue;
		} else {
			this.avgValue = (this.avgValue + avgValue) / 2;
		}
		return this;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public Connection setMaxValue(double maxValue) {

		if (this.maxValue < maxValue) {
			this.maxValue = maxValue;
			setMaxTime(System.currentTimeMillis());
		}

		return this;
	}

	public double getMinValue() {
		return minValue;
	}

	public Connection setMinValue(double minValue) {

		if (this.minValue > minValue) {
			this.minValue = minValue;
			setMinTime(System.currentTimeMillis());
		}

		return this;
	}

	public String getMaxTime() {
		return maxTime;
	}

	public Connection setMaxTime(long maxTime) {

		this.maxTime = getCurrentTime(maxTime);
		return this;
	}

	public String getMinTime() {
		return minTime;
	}

	public Connection setMinTime(long minTime) {

		this.minTime = getCurrentTime(minTime);

		return this;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public boolean isPullModel() {
		return pullModel;
	}

	public void setPullModel(boolean pullModel) {
		this.pullModel = pullModel;
	}

	class genericEmaID<T> {
		private T emaID;

		public genericEmaID(T emaID) {
			this.emaID = emaID;
		}

		public T getEmaID() {
			return emaID;
		}

	}

	public Mqtt_ServerEMA getEmapmqttclient() {
		return emapmqttclient;
	}

	public void setEmapmqttclient(Mqtt_ServerEMA emapmqttclient) {
		this.emapmqttclient = emapmqttclient;
	}

	public void parseCFG() {

		try {
			FileReader fileReader = new FileReader("ven_java.cfg");

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			global.VEN_NAME = bufferedReader.readLine();
			global.CHILD_ID = global.VEN_NAME;
			global.SYSTEM_ID = global.VEN_NAME;
			global.vtnURL = bufferedReader.readLine() + "/";
			global.udpServerPort = bufferedReader.readLine();
			global.httpPollingInterval = Integer.parseInt(bufferedReader.readLine().split(":")[1]);
			global.reportInterval = Integer.parseInt(bufferedReader.readLine().split(":")[1]);
			global.udpFlag = Integer.parseInt(bufferedReader.readLine());
			global.eventURL = bufferedReader.readLine();
			global.summaryURL = bufferedReader.readLine();
			global.pullModel = Boolean.parseBoolean(bufferedReader.readLine().split(":")[1]);
			System.out.println("HTTP Pull Model" + global.pullModel);
			System.out.println("Polling Interval" + global.httpPollingInterval);
			boolean dbCheck = Boolean.parseBoolean(bufferedReader.readLine().split(":")[1]);
			global.audoRdrRequest = Boolean.parseBoolean(bufferedReader.readLine().split(":")[1]);
			global.autoDR = Boolean.parseBoolean(bufferedReader.readLine().split(":")[1]);
			global.summaryReport = Boolean.parseBoolean(bufferedReader.readLine().split(":")[1]);
			global.summaryInterval = Integer.parseInt(bufferedReader.readLine().split(":")[1]);
			global.SUBBROKER = bufferedReader.readLine().split(":")[1];
			global.ParentnNodeID = bufferedReader.readLine().split(":")[1];
			global.CLIENTOPTION = Boolean.parseBoolean(bufferedReader.readLine().split(":")[1]);
			global.reportType = bufferedReader.readLine().split(":")[1];

			System.out.println(global.summaryReport);

			if (dbCheck) {
				// global.databaseConnection.start();
				new StoreData_cema();
			}

			bufferedReader.close();
			Thread.sleep(500);
		} catch (Exception e) {

		}

	}
	
	public void eventIDFileIO() {

		FileReader fileReader;
		try {
			fileReader = new FileReader("EVENTID.txt");

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String type = null;

			while ((type = bufferedReader.readLine()) != null) {
				
				int seq = Integer.parseInt(type.split("=>")[0]);
				String eventID = type.split("/")[1];
				
				global.eventID.put(seq, eventID);
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
