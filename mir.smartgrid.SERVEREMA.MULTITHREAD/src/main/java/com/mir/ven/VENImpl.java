package com.mir.ven;

import javax.xml.parsers.DocumentBuilder;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mir.ems.globalVar.global;

public class VENImpl implements VEN2b {

	@Override
	public String QueryRegistration(String requestID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("OpenADR_VEN_XML/oadrQueryRegistration.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().contains("requestID")) {
				node.setTextContent(requestID);
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();

	}

	@Override
	public String CreatePartyRegistration(String profileName, String transportName, String transportAddress,
			boolean reportOnly, boolean xmlSignature, boolean httpPullModel, String requestID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("OpenADR_VEN_XML/oadrCreatePartyRegistration.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().contains("requestID")) {
				node.setTextContent(requestID);
			}
			if (node.getNodeName().contains("oadrProfileType")) {
				node.setTextContent(profileName);
			}
			if (node.getNodeName().contains("oadrTransportName")) {
				node.setTextContent(transportName);
			}
			if (node.getNodeName().contains("oadrReportOnly")) {
				node.setTextContent(reportOnly + "");
			}
			if (node.getNodeName().contains("oadrXmlSignature")) {
				node.setTextContent(xmlSignature + "");
			}
			if (node.getNodeName().contains("oadrVenName")) {
				node.setTextContent(global.VEN_NAME);
			}
			if (node.getNodeName().contains("oadrHttpPullModel")) {
				node.setTextContent(httpPullModel + "");
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();

	}

	@Override
	public String CreatedEvent(String responseCode, String responseDescription, String eventID,
			String modificationNumber, String requestID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("OpenADR_VEN_XML/oadrCreatedEvent_1.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().contains("responseCode")) {
				node.setTextContent(responseCode);
			}
			if (node.getNodeName().contains("responseDescription")) {
				node.setTextContent(responseDescription);
			}
			if (node.getNodeName().contains("eventID")) {
				node.setTextContent(eventID);
			}
			if (node.getNodeName().contains("modificationNumber")) {
				node.setTextContent(modificationNumber);
			}
			if (node.getNodeName().contains("requestID")) {
				node.setTextContent(requestID);
			}
			if (node.getNodeName().contains("venID")) {
				node.setTextContent(global.HASHED_VEN_NAME);
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();
	}

	@Override
	public String RegisterReport(String rID, String resourceID, String oadrMinPeriod, String oadrMaxPeriod,
			String oadrOnChange, String marketContext, String reportRequestID, String reportSpecifierID,
			String reportName, String createdDateTime, String venID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("OpenADR_VEN_XML/oadrRegisterReport_1.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().equals("d3p1:rID")) {

				node.setTextContent(rID);

			}
			if (node.getNodeName().contains("resourceID")) {
				node.setTextContent(resourceID);
			}

			if (node.getNodeName().contains("oadrMinPeriod")) {
				node.setTextContent(oadrMinPeriod);
			}
			if (node.getNodeName().contains("oadrMaxPeriod")) {
				node.setTextContent(oadrMaxPeriod);
			}
			if (node.getNodeName().contains("oadrOnChange")) {
				node.setTextContent(oadrOnChange);
			}
			if (node.getNodeName().contains("marketContext")) {
				node.setTextContent(marketContext);
			}
			if (node.getNodeName().contains("reportRequestID")) {
				node.setTextContent(reportRequestID);
			}
			if (node.getNodeName().contains("reportSpecifierID")) {
				node.setTextContent(reportSpecifierID);
			}
			if (node.getNodeName().contains("reportName")) {
				node.setTextContent(reportName);
			}
			// if (node.getNodeName().contains("createdDateTime")) {
			// node.setTextContent(createdDateTime);
			// }
			if (node.getNodeName().contains("venID")) {
				node.setTextContent(venID);
			}

		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();
	}

	@Override
	public String RegisterReport(String createdDateTime, String venID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		// TODO Auto-generated method stub

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("OpenADR_VEN_XML/oadrRegisterReport_Ori.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node.getNodeName().contains("createdDateTime")) {
				node.setTextContent(createdDateTime);
			}
			if (node.getNodeName().contains("venID")) {
				node.setTextContent(venID);
			}

		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();

	}

	@Override
	public String RegisteredReport(String requestID, String responseCode, String responseDescription)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("OpenADR_VEN_XML/oadrRegisteredReport.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().contains("requestID")) {
				node.setTextContent(requestID);
			}
			if (node.getNodeName().contains("responseCode")) {
				node.setTextContent(responseCode);
			}
			if (node.getNodeName().contains("responseDescription")) {
				node.setTextContent(responseDescription);
			}
			if (node.getNodeName().contains("venID")) {
				node.setTextContent(global.HASHED_VEN_NAME);
			}

		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();
	}

	@Override
	public String RequestEvent() throws ParserConfigurationException, SAXException, IOException, TransformerException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("OpenADR_VEN_XML/oadrRequestEvent.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node.getNodeName().contains("venID")) {
				node.setTextContent(global.HASHED_VEN_NAME);
			}

		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();
	}

	@Override
	public String UpdateReport() throws ParserConfigurationException, TransformerException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		dbf.setNamespaceAware(true);

		dbf.setIgnoringElementContentWhitespace(true);

		dbf.setValidating(false);

		DocumentBuilder db = dbf.newDocumentBuilder();

		Document doc = db.newDocument();

		Element Envelope = doc.createElement("p1:oadrPayload");
		Envelope.setAttribute("xmlns:p1", "http://openadr.org/oadr-2.0b/2012/07");

		Element Header = createElement("p1:oadrSignedObject", Envelope, doc);

		Element OadrUpdateReport = createElement("p1:oadrUpdateReport", Header, doc);
		OadrUpdateReport.setAttribute("xmlns:p3", "http://docs.oasis-open.org/ns/energyinterop/201110");
		OadrUpdateReport.setAttribute("p3:schemaVersion", "2.0b");
		OadrUpdateReport.setAttribute("xmlns:p2", "http://docs.oasis-open.org/ns/energyinterop/201110/payloads");

		createElement("p2:requestID", OadrUpdateReport, doc, "MIR_REQ_ID");
		createElement("p3:venID", OadrUpdateReport, doc, global.HASHED_VEN_NAME);

		Iterator<String> it = global.emaProtocolCoAP.keySet().iterator();

		double power=0;

		while (it.hasNext()) {
			String key = it.next();
			power += global.emaProtocolCoAP.get(key).getPower();
		}

		Element oadrReport = createElement("p1:oadrReport", OadrUpdateReport, doc);
		oadrReport.setAttribute("xmlns:p3", "urn:ietf:params:xml:ns:icalendar-2.0:stream");
		oadrReport.setAttribute("xmlns:p4", "http://docs.oasis-open.org/ns/energyinterop/201110");

		createElement("p4:reportRequestID", oadrReport, doc, "MIR_REPORT_REQ_ID");
		createElement("p4:reportSpecifierID", oadrReport, doc, "MIR_REPORT_SPEC_ID");
		createElement("p4:reportName", oadrReport, doc, "TELEMETRY_USAGE");
		createElement("p4:createdDateTime", oadrReport, doc, new TimeFormat().getCurrentTime());

		Element intervals = createElement("p3:intervals", oadrReport, doc);
		intervals.setAttribute("xmlns:p4", "http://docs.oasis-open.org/ns/energyinterop/201110");

		Element interval = createElement("p4:interval", intervals, doc);
		interval.setAttribute("xmlns:p5", "urn:ietf:params:xml:ns:icalendar-2.0");

		Element dtstart = createElement("p5:dtstart", interval, doc);
		createElement("p5:date-time", dtstart, doc, new TimeFormat().getCurrentTime());

		Element duration = createElement("p5:duration", interval, doc);
		createElement("p5:duration", duration, doc, global.duration);

		Element oadrReportPayload = createElement("p1:oadrReportPayload", interval, doc);
		createElement("p4:rID", oadrReportPayload, doc, global.CHILD_ID);
		createElement("p4:confidence", oadrReportPayload, doc, "100");
		createElement("p4:accuracy", oadrReportPayload, doc, "100");

		Element payloadFloat = createElement("p1:payloadFloat", oadrReportPayload, doc);
		createElement("p4:value", payloadFloat, doc, power + "");
		createElement("p1:oadrDataQuality", oadrReportPayload, doc, "Quality Good - Non Specific");

		if (global.reportType.equals("Explicit")) {

			it = global.emaProtocolCoAP.keySet().iterator();

			while (it.hasNext()) {

				String key = it.next();

				oadrReport = createElement("p1:oadrReport", OadrUpdateReport, doc);
				oadrReport.setAttribute("xmlns:p3", "urn:ietf:params:xml:ns:icalendar-2.0:stream");
				oadrReport.setAttribute("xmlns:p4", "http://docs.oasis-open.org/ns/energyinterop/201110");

				createElement("p4:reportRequestID", oadrReport, doc, "MIR_REPORT_REQ_ID");
				createElement("p4:reportSpecifierID", oadrReport, doc, "MIR_REPORT_SPEC_ID");
				createElement("p4:reportName", oadrReport, doc, "TELEMETRY_USAGE");
				createElement("p4:createdDateTime", oadrReport, doc, new TimeFormat().getCurrentTime());

				intervals = createElement("p3:intervals", oadrReport, doc);
				intervals.setAttribute("xmlns:p4", "http://docs.oasis-open.org/ns/energyinterop/201110");

				interval = createElement("p4:interval", intervals, doc);
				interval.setAttribute("xmlns:p5", "urn:ietf:params:xml:ns:icalendar-2.0");

				dtstart = createElement("p5:dtstart", interval, doc);
				createElement("p5:date-time", dtstart, doc, new TimeFormat().getCurrentTime());

				duration = createElement("p5:duration", interval, doc);
				createElement("p5:duration", duration, doc, global.duration);

				oadrReportPayload = createElement("p1:oadrReportPayload", interval, doc);
				createElement("p4:rID", oadrReportPayload, doc, key);
				createElement("p4:confidence", oadrReportPayload, doc, "100");
				createElement("p4:accuracy", oadrReportPayload, doc, "100");

				payloadFloat = createElement("p1:payloadFloat", oadrReportPayload, doc);
				createElement("p4:value", payloadFloat, doc, global.emaProtocolCoAP.get(key).getPower() + "");
				createElement("p1:oadrDataQuality", oadrReportPayload, doc, "Quality Good - Non Specific");

			}
		}

		StringWriter sw = new StringWriter();

		StreamResult result = new StreamResult(sw);

		DOMSource source = new DOMSource(Envelope);

		TransformerFactory tf = TransformerFactory.newInstance();

		Transformer transformer = tf.newTransformer();

		transformer.transform(source, result);

		String xmlString = sw.toString();

		return xmlString;
	}

	@Override
	public String UpdateReport(String rID, double value, String dtStart, String createdDateTime, String requestID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("OpenADR_VEN_XML/oadrUpdatedReport_1.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node.getNodeName().contains("venID")) {
				node.setTextContent(global.HASHED_VEN_NAME);
			}
			if (node.getNodeName().contains("value")) {
				node.setTextContent(global.currentVal + "");
			}
			if (node.getNodeName().contains("createdDateTime")) {
				node.setTextContent(createdDateTime);
			}
			if (node.getNodeName().equals("p4:rID")) {
				node.setTextContent(rID);
			}
			if (node.getNodeName().contains("date-time")) {
				node.setTextContent(dtStart);
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();

	}

	@Override
	public String CreateOptSchedule(String optReason, String optType, String dtStart, String optID)
			throws ParserConfigurationException, SAXException, IOException, TransformerException, DOMException,
			TransformerFactoryConfigurationError {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("OpenADR_VEN_XML/oadrCreateOpt.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node.getNodeName().contains("venID")) {
				node.setTextContent(global.HASHED_VEN_NAME);
			}
			if (node.getNodeName().contains("optReason")) {
				node.setTextContent(optReason);
			}
			if (node.getNodeName().contains("optType")) {
				node.setTextContent(optType);
			}
			if (node.getNodeName().contains("date-time")) {
				node.setTextContent(dtStart);
			}
			if (node.getNodeName().contains("createdDateTime")) {
				node.setTextContent(dtStart);
			}
			if (node.getNodeName().contains("optID")){
				node.setTextContent(optID);
			}
			
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();

	}

	@Override
	public void CancelOptSchedule(String optID, String requestID) {

	}

	@Override
	public String Poll() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File("OpenADR_VEN_XML/oadrPoll.xml"));

		NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*", "*");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node.getNodeName().contains("venID")) {
				node.setTextContent(global.HASHED_VEN_NAME);
			}

		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter sw = new StringWriter();
		transformer.transform(source, new StreamResult(sw));

		return sw.toString();
	}

	private Element createElement(String name, Element el, Document doc) {

		Element element = doc.createElement(name);

		el.appendChild(element);

		return element;

	}

	private void createElement(String name, Element el, Document doc, String value) {

		Element element = doc.createElement(name);

		el.appendChild(element);

		if (!value.equals("")) {

			element.setTextContent(value);

		}

	}

}
