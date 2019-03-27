package com.mir.ems.coap;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONException;
import org.json.JSONObject;
import com.mir.ems.coap.emap.Emap;
import com.mir.ems.coap.emap.OpenADR;
import com.mir.ems.database.item.DeviceClass;
import com.mir.ems.globalVar.global;
import com.mir.ven.EventACK_Client;

public class CoAPServer {

	public String gw;
	public String venID;
	public String vtnID = "MIR_VTN";
	public int requestID;
	public int version;
	public String Path;
	public String Text;

	public static int ven;
	public static int poll_seq = 0;
	public static InetAddress client_ip;

	public CoAPServer() {
		// TODO Auto-generated constructor stub
		CoapServer server = new CoapServer();
		CoapResource info = new CoapResource("info") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR " + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub
				exchange.respond("info");

				String payload = exchange.getRequestText();

				String device_id = payload.split("/")[3];
				String ema_id = payload.split("/")[1];

				int state = Integer.parseInt(payload.split("/")[4]);
				int dimming = Integer.parseInt(payload.split("/")[5]);
				double value = Integer.parseInt(payload.split("/")[6]);
				int priority = Integer.parseInt(payload.split("/")[7]);

				String ipAddr = exchange.getSourceAddress().toString();

				global.devManger.replace(device_id, new DeviceClass(1, device_id, ema_id, 1, device_id, state, dimming,
						priority, ipAddr, null, value));

			}
		};

		CoapResource devConnect = new CoapResource("DeviceConnect") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR " + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {

				System.out.println("여기");
				String payload = exchange.getRequestText();
				System.out.println(payload);

				String device_id = payload.split("/")[3];
				String ema_id = payload.split("/")[1];

				int state = Integer.parseInt(payload.split("/")[4]);
				int dimming = Integer.parseInt(payload.split("/")[5]);
				double value = Integer.parseInt(payload.split("/")[6]);
				int priority = Integer.parseInt(payload.split("/")[7]);

				String ipAddr = exchange.getSourceAddress().toString();

				global.devManger.put(device_id, new DeviceClass(1, device_id, ema_id, 1, device_id, state, dimming,
						priority, ipAddr, null, value));
				
			}
		};

		CoapResource rdr = new CoapResource("oadrRDR") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {

				try {
					JSONObject json = new JSONObject(exchange.getRequestText());

					String emaID = json.getString("SrcEMA");
					String devID = json.getString("devnumber");
					double requestThreshold = json.getDouble("value");

					json = new JSONObject();

					json.put("SrcEMA", global.SYSTEM_ID);
					json.put("devnumber", devID);
					json.put("responseDescription", "OK");
					json.put("responseCode", 200);
					exchange.respond(ResponseCode.CONTENT, json.toString(), MediaTypeRegistry.APPLICATION_JSON);

				} catch (JSONException e) {

				}		

			}
		};

		CoapResource devDisconnect = new CoapResource("DeviceDisconnect") {

			public void handleGET(CoapExchange exchange) {
				String text = exchange.getRequestOptions().getUriQuery().get(0);
				System.out.println(getName() + " called GET method.");
				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
			}

			@Override
			public void handlePOST(CoapExchange exchange) {
				String text = exchange.getRequestText();
				System.out.println(getName() + " called POST method.");
				exchange.respond("MIR" + text);
			}

			@Override
			public void handlePUT(CoapExchange exchange) {
				// TODO Auto-generated method stub

				String payload = exchange.getRequestText();

				String device_id = payload.split("/")[3];

				global.devManger.remove(device_id);
			}
		};
		
//		CoapResource summaryACK = new CoapResource("/SummaryACK") {
//
//			public void handleGET(CoapExchange exchange) {
//				String text = exchange.getRequestOptions().getUriQuery().get(0);
//				System.out.println(getName() + " called GET method.");
//				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
//			}
//
//			@Override
//			public void handlePOST(CoapExchange exchange) {
//
//			}
//
//			@Override
//			public void handlePUT(CoapExchange exchange) {
//				// TODO Auto-generated method stub
//				String text = exchange.getRequestText();
////				System.out.println("SummaryACK");
//			}
//		};
//		
//		CoapResource eventACK = new CoapResource("EventACK") {
//
//			public void handleGET(CoapExchange exchange) {
//				String text = exchange.getRequestOptions().getUriQuery().get(0);
//				System.out.println(getName() + " called GET method.");
//				exchange.respond("MIR" + text.substring(text.indexOf("=") + 1));
//			}
//
//			@Override
//			public void handlePOST(CoapExchange exchange) {
//
//			}
//
//			@Override
//			public void handlePUT(CoapExchange exchange) {
//				// TODO Auto-generated method stub
//				String text = exchange.getRequestText();
//				String eventID ="";
//				try {
//					
//					JSONObject jsonParse = new JSONObject(text);
//					eventID = jsonParse.getString("eventID");
//
//				} catch (JSONException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				
//				//CLIENT EMA _ COAP SEND
//				if(global.CLIENTOPTION){
//					
//					String pathSet = "coap://" + global.coapServerIP + ":" + global.coapServerPort + "/EMAP/"
//							+ global.getParentnNodeID() + "/" + global.version + "/";
//					
//					String uri = pathSet + "EventACK";
//					CoapClient client = new CoapClient();
//
//					client = new CoapClient();
//					client.setURI(uri);
//
//					JSONObject json = new JSONObject();
//					try {
//						json.put("venID", global.HASHED_VEN_NAME);
//						json.put("service", "eventACK");
//						json.put("eventID", eventID);
//						
//						client.put(new CoapHandler() {
//							
//							@Override
//							public void onLoad(CoapResponse response) {}
//							@Override
//							public void onError() {}
//							
//						}, json.toString(), MediaTypeRegistry.APPLICATION_JSON);
//						
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//		
//				//SERVER EMA _ HTTP SEND
//				else if (!global.CLIENTOPTION){
//					
//					ExecutorService service = Executors.newFixedThreadPool(3);
//					Future<Boolean> retdouble = service.submit(new EventACK_Client(eventID));
////					boolean requestStatus;
//					try {
//						
//						retdouble.get();
//						service.shutdown();
//						
//					}catch (Exception e){
//						
//					}
//				}
//				
//			}
//		};
		
		
//		server.add(summaryACK);
//		server.add(eventACK);

		server.add(rdr);
		server.add(info);
		server.add(devConnect);
		server.add(devDisconnect);
	
		// Profile
		server.add(new Emap("EMAP"));
		server.add(new OpenADR("OpenADR"));

		// Observer
		server.add(new CoAPObserver("OpenADR2.0b"));
		server.add(new CoAPObserver("EMAP1.0b"));
		
//		if(global.summaryReport){
//			server.add(new CoAPSummaryObserver("Summary"));
//		}
		
		setNetworkConfiguration();
		server.addEndpoint(new CoapEndpoint(5683, setNetworkConfiguration()));

		server.start();

	}

	public int poll_sate() {
		int state = 0;
		return state;
	}


	private NetworkConfig setNetworkConfiguration() {

		return NetworkConfig.createStandardWithoutFile()
				.setString(NetworkConfig.Keys.DEDUPLICATOR, NetworkConfig.Keys.NO_DEDUPLICATOR)
				.setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE, 60000)
				.setInt(NetworkConfig.Keys.UDP_CONNECTOR_DATAGRAM_SIZE, 60000)
				.setInt(NetworkConfig.Keys.UDP_CONNECTOR_SEND_BUFFER, 60000)
				.setInt(NetworkConfig.Keys.UDP_CONNECTOR_RECEIVE_BUFFER, 60000)
				.setInt(NetworkConfig.Keys.MAX_MESSAGE_SIZE, 60000).setInt(NetworkConfig.Keys.EXCHANGE_LIFETIME, 1500);
	}
	
	public static void main(String[] args){
		
		CoAPServer coapServer = new CoAPServer();

		
	}

}
