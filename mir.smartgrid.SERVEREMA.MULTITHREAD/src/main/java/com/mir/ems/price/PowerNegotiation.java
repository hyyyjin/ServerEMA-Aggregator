package com.mir.ems.price;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.globalVar.global;

public class PowerNegotiation extends Thread {

	MqttClient client;
	double requestPower;
	public PowerNegotiation(MqttClient client, double requestPower) {
		this.client = client;
		this.requestPower = requestPower;
	}

	@Override
	public void run() {

		String p_topic, m_msg;
		JSONObject drmsg = new JSONObject();
		Date currentTime = new Date();

		System.out.println(global.gatheredPrice.toString());
		try {
			sleep(2000);
			System.out.println("CALL");
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

//		this.sleep(2000);
		try {
//			System.out.println("여기니?");

			/*
			 * Find out MINIMUM Value
			 */
			
			long negoPrice = 99999999;
			System.out.println(negoPrice);
			
			Iterator<String> keys = global.gatheredPrice.keySet().iterator();
			while (keys.hasNext()) {
				System.out.println("rhrhrhrhrhrhrhrh");
				String key = keys.next();
				long price = global.gatheredPrice.get(key).longValue();
				System.out.println("@@");
				System.out.println(price);
				System.out.println("@@");
				if (negoPrice > price) {
					negoPrice = price;
				}
			}

			keys = global.gatheredPrice.keySet().iterator();

			while (keys.hasNext()) {
				String key = keys.next();
				long price = global.gatheredPrice.get(key).longValue();
				if (negoPrice == price) {
					System.out.println("여기드러옴?");
					drmsg.put("SrcEMA", global.getSYSTEM_ID());
					drmsg.put("DestEMA", key);
					drmsg.put("Type", "PowerNegotiationACK");
					drmsg.put("EMAID", key);
					drmsg.put("NegotiationInOut", "NegotiationIn");
					drmsg.put("Power", this.requestPower);					
					drmsg.put("Cost", negoPrice);					
					drmsg.put("Time", currentTime);

					p_topic = "SEMA/" + key + "/emergency/PowerNegotiationACK";
					m_msg = drmsg.toString();

					publishThread(p_topic, global.qos, m_msg.getBytes());

				} else {
					drmsg.put("SrcEMA", global.getSYSTEM_ID());
					drmsg.put("DestEMA", key);
					drmsg.put("Type", "PowerNegotiationACK");
					drmsg.put("EMAID", key);
					drmsg.put("NegotiationInOut", "NegotiationOut");
					drmsg.put("Power", this.requestPower);					
					drmsg.put("Time", currentTime);

					p_topic = "SEMA/" + key + "/emergency/PowerNegotiationACK";
					m_msg = drmsg.toString();

					publishThread(p_topic, global.qos, m_msg.getBytes());

				}
			}
			
			global.negotiationStatus = true;
			
		}  catch (JSONException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	public void publishThread(String topicName, int qos, byte[] payload) {

		new Thread(new Runnable() {

			public void run() {

				MqttMessage message = new MqttMessage(payload);

				message.setQos(qos);

				try {
					String time1 = new Timestamp(System.currentTimeMillis()).toString();

					System.out.println("Publishing *** Time:\t" + time1 + "Topic:\t" + topicName + " Message:\t"
							+ new String(message.getPayload()) + " QoS:\t" + message.getQos());

					client.publish(topicName, message);
				} catch (MqttException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}).start();
	}

}
