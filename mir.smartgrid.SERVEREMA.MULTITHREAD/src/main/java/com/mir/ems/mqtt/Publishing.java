package com.mir.ems.mqtt;

import java.sql.Timestamp;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Publishing {

	public void publishThread(MqttClient client, String topicName, int qos, byte[] payload) {

		new Thread(new Runnable() {

			public void run() {

				MqttMessage message = new MqttMessage(payload);

				message.setQos(qos);

				try {
					String time1 = new Timestamp(System.currentTimeMillis()).toString();

					// if (topicName.contains("Event") && new
					// String(message.getPayload()).contains("DistributeEvent"))
					// {
//					if (topicName.contains("Opt")) {

//						System.err.println("Publishing *** Time:\t" + time1 + "Topic:\t" + topicName + " Message:\t"
//								+ new String(message.getPayload()) + " QoS:\t" + message.getQos());

//					}
					client.publish(topicName, message);
				} catch (MqttException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}).start();
	}
}
