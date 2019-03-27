package com.mir.ems.coap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.mir.ems.database.item.EMAP_CoAP_EMA_DR;
import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.Publishing;
import com.mir.ems.profile.emap.v2.Event;
import com.mir.ems.profile.emap.v2.EventResponse;
import com.mir.ems.profile.emap.v2.EventSignals;
import com.mir.ems.profile.emap.v2.Intervals;
import com.mir.ems.profile.emap.v2.Summary;
import com.mir.ems.profile.emap.v2.SummaryReport;
import com.mir.ems.topTab.DRScheduling;

public class SummaryObserverSubPath extends CoapResource {

	// private boolean initialFlag = false;
	// int gwNum;
	private String name;
	private String parentPath;

	public SummaryObserverSubPath(String name, String parentPath) {
		super(name);
		this.name = name;
		System.out.println(name);
		setObservable(true);
		setObserveType(Type.NON);
		getAttributes().setObservable();

		setParentPath(parentPath);

		Timer timer = new Timer();
		timer.schedule(new UpdateTask(), 0, global.summaryInterval);
	}

	private class UpdateTask extends TimerTask {
		public void run() {

			changed();

		}
	}

	public void handleGET(CoapExchange exchange) {

//		Response response = new Response(ResponseCode.CONTENT);
//		clearObserveRelations();
		Iterator<String> it = global.emaProtocolCoAP.keySet().iterator();
		Summary sm = new Summary();

		while (it.hasNext()) {

			String key = it.next();

			sm.addsummaryParam(key, global.emaProtocolCoAP.get(key).getMargin(),
					global.emaProtocolCoAP.get(key).getAvgValue(), global.emaProtocolCoAP.get(key).getMaxValue(),
					global.emaProtocolCoAP.get(key).getGenerate(), global.emaProtocolCoAP.get(key).getStorage(),
					global.emaProtocolCoAP.get(key).getPower());

		}

		SummaryReport sr = new SummaryReport();
		sr.setDestEMA(name);
		sr.setRequestID("requestID");
		sr.setService("SummaryReport");
		sr.setSrcEMA(global.SYSTEM_ID);
		sr.setSummary(sm.getEventParams());
		sr.setSummaryType("SummaryReport");

		exchange.respond(ResponseCode.CONTENT, sr.toString(), MediaTypeRegistry.APPLICATION_JSON);
//
//		response.setPayload("Initial_Success");
//
//		exchange.respond(response);

	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

}
