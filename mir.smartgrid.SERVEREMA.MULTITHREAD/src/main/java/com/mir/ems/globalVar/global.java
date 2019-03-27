package com.mir.ems.globalVar;

import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.mir.ems.coap.CoAPObserverSubPath;
import com.mir.ems.database.item.DeviceClass;
import com.mir.ems.database.item.DeviceClass_temp;
import com.mir.ems.database.item.EMAClass_temp;
import com.mir.ems.database.item.EMAP_CoAP_EMA_DR;
import com.mir.ems.database.item.EMAP_CoAP_Schedule;
import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.database.item.ServerEmaProfile;
import com.mir.ems.database.item.Schedule_Profile;
import com.mir.ems.mqtt.EventInitiater;
import com.mir.ems.price.Industrial;
import com.mir.ems.price.Industrial_RealTime;
import com.mir.update.database.DatabaseConnection;
import com.mir.update.database.cema_database;
import com.mir.update.database.device_total_database;

public class global {
	
	public static boolean eventFromServer = false;
	public static int autoDRCNT = 0;
	public static int autoDRTOTAL = 60;
	
	public static String SUBBROKER = "166.104.28.131";
	public static boolean CLIENTOPTION = false;
	public static int cnt = 10;
	public static int devCnt = 1;
	//HTTP VEN INFO
	public static String vtnURL, udpServerPort;
	public static int httpPollingInterval, udpFlag;
	public static int reportInterval = 1000;
	public static int summaryInterval = 1000;	
	public static boolean pullModel = false;
	public static double currentVal = 400;
	public static String VEN_NAME = "MIR_Ven_test1";
	public static String HASHED_VEN_NAME = "";
	public static boolean RegistrationFlag = false;
	public static boolean reportOnly = false;
	public static String marketContext = "http://MarketContext1";
	public static String eventURL;
	public static String summaryURL;
	public static boolean audoRdrRequest = false;
	public static boolean summaryReport = false;
	public static boolean autoDR = false;
//	public static int Interval = 2000;
	
	// DATABASE
	public static int REPORTSEQ_CEMA = 0;
	public static int REPORTCNT_CEMA = 0;
	public static int REPORTSEQ = 0;
	public static int REPORTCNT = 0;
	public static ConcurrentHashMap<Integer, device_total_database> saveDevice = new ConcurrentHashMap<Integer, device_total_database>();
	public static ConcurrentHashMap<Integer, cema_database> saveCEMA = new ConcurrentHashMap<Integer, cema_database>();
//	public static DatabaseConnection databaseConnection = new DatabaseConnection();
	// END DATABASE

	//CoAP Server Info
	public static String coapServerIP = "localhost";
	public static String coapServerPort = "5683";
	public static String qosType = "Controllable";
	public static final int interval = 2000;
	public static String communicationModel= "Pull";

	
	// System Info
	public static String version = "1.0b";
	public static String openADRVersion = "2.0b";
	public static String CHILD_ID = "SERVER_EMA3";
	public static String SYSTEM_TYPE = "SEMA";
	public static String SYSTEM_ID = "SERVER_EMA3";
	
	/*****************************************************/
	// Experiement
	public static int EXPERIMENTNUM = 20;
	public static double EXPERIMENTPERCENT = 0.8;
	public static boolean EXPERIMENTAUTODR = false;
//	public static boolean emergencyCheck = false;
	//
	// Price Info
	public static Vector<Industrial> priceTable = new Vector<>();
	public static Vector<Industrial_RealTime> realTimePriceTable = new Vector<>();
	public static String createTableDate = null;
	public int negotiationPrice = 1000;
	public static boolean tableHasChanged = false;
	public static boolean realTimeTableHasChanged = false;
	public static boolean realTimeCheck = false;
	/*****************************************************/

	// Duration Info
	public static String duration = "PT2S";
	/*****************************************************/

	public static boolean negotiationStatus = false;

	/*-------------------------
	 * Threshold SET*/
	public static double THRESHOLD = 0;
	public static double AVAILABLE_THRESHOLD = 10000;
	public static double RESERVE_THRESHOLD = 0;
	public static final int RESERVE_THRESHOLD_PERCENTAGE = 10;
	public static double AVAILABLE_RESERVE_THRESHOLD = 200;
	
	public static TreeMap<Date, Double> availableSchedule = new TreeMap<Date, Double>();
	public static ConcurrentHashMap<String, Schedule_Profile> scheduleDate = new ConcurrentHashMap<String, Schedule_Profile>();
	// -------------------------

	//OpenADR Manager
	public static ConcurrentHashMap<String, Integer> venRegisterFlag = new ConcurrentHashMap<String, Integer>();

	
	/*
	 * 하위 EMA Threshold정보 관리
	 */ 

	public static ConcurrentHashMap<String, Double> emaThresholdManage = new ConcurrentHashMap<String, Double>();
	
	// -------------------------
	public static final String profileName = "EMAProtocol";

	/*
	 * incoming EMA SEQUENCE
	 */

	public static int EMASEQ = 0;
	// ===========================

	// Device 정보 관리
	public static ConcurrentHashMap<String, DeviceClass> devManger = new ConcurrentHashMap<String, DeviceClass>();

	//VEN 입장
	public static int venRegisterSeqNumber = 0;
	
	
	//
	public static ConcurrentHashMap<Integer, String> eventID= new ConcurrentHashMap<Integer, String>();
	public static ConcurrentHashMap<Integer, String> rdrID= new ConcurrentHashMap<Integer, String>();

	
	
	// EMA 정보관리
	public static ConcurrentHashMap<String, Emap_Cema_Profile> emaProtocolCoAP = new ConcurrentHashMap<String, Emap_Cema_Profile>();
	public static ConcurrentHashMap<String, String> emaRegister = new ConcurrentHashMap<String, String>();
	public static ConcurrentHashMap<String, Emap_Cema_Profile> getEmaProtocolCoAP() {
		return emaProtocolCoAP;
	}
	public static ConcurrentHashMap<String, CoAPObserverSubPath> observeManager = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, Integer> rdrMap = new ConcurrentHashMap<>();

	public static void putEmaProtocolCoAP(String emaID, Emap_Cema_Profile emap_CoAP_EMA) {
		try {
			Thread.sleep(20);
			emaProtocolCoAP.put(emaID, emap_CoAP_EMA);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void setEmaProtocolCoAP(ConcurrentHashMap<String, Emap_Cema_Profile> emaProtocolCoAP) {
		global.emaProtocolCoAP = emaProtocolCoAP;
	}

	public static ConcurrentHashMap<String, Emap_Device_Profile> emaProtocolCoAP_Device = new ConcurrentHashMap<String, Emap_Device_Profile>();
	public static HashMap<String, EMAP_CoAP_Schedule> emaProtocolCoAP_Schedule = new HashMap<String, EMAP_CoAP_Schedule>();
	public static HashMap<String, EMAP_CoAP_EMA_DR> emaProtocolCoAP_EventFlag = new HashMap<String, EMAP_CoAP_EMA_DR>();
	public static HashMap<String, EMAP_CoAP_EMA_DR> obs_emaProtocolCoAP_EventFlag = new HashMap<String, EMAP_CoAP_EMA_DR>();

	public static ConcurrentHashMap<String, EMAClass_temp> emaHashMap = new ConcurrentHashMap<String, EMAClass_temp>();

	public static ConcurrentHashMap<String, DeviceClass_temp> devHashMap = new ConcurrentHashMap<String, DeviceClass_temp>();

	public static ConcurrentHashMap<String, ServerEmaProfile> serverEmaMap = new ConcurrentHashMap<String, ServerEmaProfile>();

	public static ConcurrentHashMap<String, EMAClass_temp> getEmaHashMap() {
		return emaHashMap;
	}

	public static void setEmaHashMap(ConcurrentHashMap<String, EMAClass_temp> emaHashMap) {
		global.emaHashMap = emaHashMap;
	}

	public static ConcurrentHashMap<String, DeviceClass_temp> getDevHashMap() {
		return devHashMap;
	}

	public static void setDevHashMap(ConcurrentHashMap<String, DeviceClass_temp> devHashMap) {
		global.devHashMap = devHashMap;
	}

	public static ConcurrentHashMap<String, ServerEmaProfile> getOpenADRHashMap() {
		return serverEmaMap;
	}

	public static HashMap<String, Long> gatheredPrice = new HashMap<String, Long>();

	private double totalEnergyUsage = 0;

	// public static String ServerEMAID = "SERVER_EMA1";

	public static EventInitiater initiater = new EventInitiater();

	public static String tempIP = null;
	public static String tempPort = null;

	public static String udpIP = null;
	public static int udpPort;

	
	/////////////////////////// 2018-08-13 EDITED
	public static String reportType = "Explicit";
	public static String profile = "EMAP";
	public static String ParentnNodeID = "EMS";
	////////////////////////////////////////////
	

	public static int qos = 0;

	public static String protocol_type_global = "MQTT";

	public static boolean eventStatus = false;

	public static double total_ess_power, total_pv_power;

	public static String devControl = null;

	public static HashMap<String, EMAP_CoAP_EMA_DR> getObs_emaProtocolCoAP_EventFlag() {
		return obs_emaProtocolCoAP_EventFlag;
	}

	public static String getDevControl() {
		return devControl;
	}

	public static void setDevControl(String devControl) {
		global.devControl = devControl;
	}

	public static String getProtocol_type_global() {
		return protocol_type_global;
	}

	public static void setProtocol_type_global(String protocol_type_global) {
		global.protocol_type_global = protocol_type_global;
	}

	public static boolean isEventStatus() {
		return eventStatus;
	}

	public static void setEventStatus(boolean eventStatus) {
		global.eventStatus = eventStatus;
	}

	public static String getSYSTEM_TYPE() {
		return SYSTEM_TYPE;
	}

	public static void setSYSTEM_TYPE(String sYSTEM_TYPE) {
		SYSTEM_TYPE = sYSTEM_TYPE;
	}

	public static String getSYSTEM_ID() {
		return SYSTEM_ID;
	}

	public static void setSYSTEM_ID(String sYSTEM_ID) {
		SYSTEM_ID = sYSTEM_ID;
	}

	public double getTotalEnergyUsage() {
		return totalEnergyUsage;
	}

	public void setTotalEnergyUsage(double totalEnergyUsage) {
		this.totalEnergyUsage = totalEnergyUsage;
	}

	public int getNegotiationPrice() {
		return negotiationPrice;
	}

	public void setNegotiationPrice(int negotiationPrice) {
		this.negotiationPrice = negotiationPrice;
	}

	public static String getParentnNodeID() {
		return ParentnNodeID;
	}

	public static void setParentnNodeID(String parentnNodeID) {
		ParentnNodeID = parentnNodeID;
	}

	
}
