package com.mir.ems.coap.emap;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.beep.BeepSound;
import com.mir.ems.globalVar.global;
import com.mir.ven.EventACK_Client;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class EventACK extends CoapResource {

	public EventACK(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
	}

	@Override
	public void handlePOST(CoapExchange exchange) {
		exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
	}

	@Override
	public void handleDELETE(CoapExchange exchange) {
		exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
	}

	@Override
	public void handlePUT(CoapExchange exchange) {

		String text = exchange.getRequestText();
		
		String eventID = "";
		try {

			JSONObject jsonParse = new JSONObject(text);
			eventID = jsonParse.getString("eventID");

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		if(global.CLIENTOPTION){
			if(eventID.equals("EVENTID11543918743935")){
				new BeepSound().start();
			}
		}
		
		if (global.eventFromServer) {
//			global.eventFromServer = false;
		
			// CLIENT EMA _ COAP SEND
			if (global.CLIENTOPTION) {

				String pathSet = "coap://" + global.coapServerIP + ":" + global.coapServerPort + "/EMAP/"
						+ global.getParentnNodeID() + "/" + global.version + "/";

				String uri = pathSet + "EventACK";
				CoapClient client = new CoapClient();

				client = new CoapClient();
				client.setURI(uri);
				client.useNONs();

				JSONObject json = new JSONObject();
				try {

					json.put("SrcEMA", global.CHILD_ID);
					json.put("DestEMA", global.ParentnNodeID);
					json.put("service", "eventACK");
					json.put("eventID", eventID);

					client.put(new CoapHandler() {

						@Override
						public void onLoad(CoapResponse response) {
						}

						@Override
						public void onError() {
						}

					}, json.toString(), MediaTypeRegistry.APPLICATION_JSON);

					

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// SERVER EMA _ HTTP SEND
			else if (!global.CLIENTOPTION) {
				
				AsyncHttpClient client = new AsyncHttpClient();
				JSONObject json = new JSONObject();
				try {
					json.put("SrcEMA", global.HASHED_VEN_NAME);
					json.put("DestEMA", global.ParentnNodeID);
					json.put("service", "eventACK");
					json.put("eventID", eventID);

					client.preparePost("http://166.104.28.51:12345/EMAP/EMS1/1.0b/EventACK")
							.setBody(json.toString().getBytes()).setHeader("Content-Type", "application/json").execute();

					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {

				}

			}
		}



	}

}
