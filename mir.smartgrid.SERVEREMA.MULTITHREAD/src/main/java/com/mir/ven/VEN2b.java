package com.mir.ven;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

/*
 * Copyright (c) 2018, Hyunjin Park, Mobile Intelligence and Routing Lab
 * All rights reserved
 * 
 * 2018.07.05(Thu)
 * Edited by Hyunjin Park
 * Hanyang University
 * 
 */
public interface VEN2b {

	public String QueryRegistration(String requestID) throws ParserConfigurationException, SAXException, IOException,
			TransformerConfigurationException, TransformerException;

	// profileType 클래스 하나 만들어서 oadrProfileType Object로 변경
	// transportType 클래스 하나 만들어서 oadrTransportType Object로 변경
	public String CreatePartyRegistration(String profileName, String transportName, String transportAddress,
			boolean reportOnly, boolean xmlSignature, boolean httpPullModel, String requestID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException;

	// response 클래스 하나 만들어서 responses Object로 변경, 이벤트에 참여할지 optIn, optOut 여부를
	// 결정하는 클래스로 생성한다.
	public String CreatedEvent(String responseCode, String responseDescription, String eventID,
			String modificationNumber, String requestID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException;

	// UpdateReport와 공유할수 있는 oadrReport 인터페이스를 하나만들고 그걸 구현한뒤에 여기다가 넣는 형태로
	// public String RegisterReport(String oadrReport, String requestID)
	// throws ParserConfigurationException, SAXException, IOException,
	// TransformerException;

	public String RegisteredReport(String requestID, String responseCode, String responseDescription)
			throws ParserConfigurationException, SAXException, IOException, TransformerException;

	// dtStart와 createdDateTime은 Time혹은 Date Date Type으로 변경해주어야 한다.
	public String UpdateReport(String rID, double value, String dtStart, String createdDateTime, String requestID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException;

	public String UpdateReport() throws ParserConfigurationException, SAXException, IOException, TransformerException;

	// optSchedule 클래스 하나 만들어서 optSchedule Object로 변경
	public String CreateOptSchedule(String optReason, String optType, String dtStart, String optID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException;

	public void CancelOptSchedule(String optID, String requestID);

	public String RequestEvent() throws ParserConfigurationException, SAXException, IOException, TransformerException;

	public String Poll() throws ParserConfigurationException, SAXException, IOException, TransformerException;

	public String RegisterReport(String rID, String resourceID, String oadrMinPeriod, String oadrMaxPeriod,
			String oadrOnChange, String marketContext, String reportRequestID, String reportSpecifierID,
			String reportName, String createdDateTime, String venID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException;

	public String RegisterReport(String createdDateTime, String venID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException;

}
