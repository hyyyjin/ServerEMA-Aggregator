package com.mir.ems.mqtt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.database.item.DeviceClass_temp;
import com.mir.ems.database.item.EMAClass_temp;
import com.mir.ems.deviceProfile.EMA_tab_temp;
import com.mir.ems.globalVar.global;

public class HandleEnergyReport extends Thread {
	String topic;
	JSONObject payload;
	public EMAClass_temp emaClass_temp;
	public DeviceClass_temp deviceClass_temp;
	MqttClient client;

	public HandleEnergyReport(MqttClient client, String topic, JSONObject payload) {
		this.client = client;
		this.topic = topic;
		this.payload = payload;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		// TODO Auto-generated method stub

		String srcEMA, type, emaID;
		int emaCnt;
		double power, margin, generate, storage;

		String p_topic;
		String m_msg;

		String tempTime;

		Date time, maxTime_d, minTime_d;
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject drmsg = new JSONObject();

		if (topic.equals("Connect")) {

			try {
				if (payload.get("service").equals("ConnectRequest")) {

					/*--------------------------------------------------------d------
					 * emaID 할당해주는 부분, ReportType은 처음 ServerEMA를 작동할 때 정해준다.
					 */

					srcEMA = (String) payload.get("SrcEMA");
					emaID = srcEMA;
					// emaID = srcEMA + (int) (Math.random() * (100 - 1)) + 1;

					// emaID = DigestUtils.md2Hex(emaID);

					tempTime = (String) payload.get("time");

					try {
						time = transFormat.parse(tempTime);

						if (!global.emaHashMap.containsKey(emaID)) {
							emaClass_temp = new EMAClass_temp(emaID, "MQTT", 0.0, 0.0, 0.0, 0.0, 0, global.reportType,
									time);
							global.emaHashMap.put(emaID, emaClass_temp);
							// EMA_tab_temp.modify_EMA_table();
						} else {
							emaClass_temp = new EMAClass_temp(emaID, "MQTT", 0.0, 0.0, 0.0, 0.0, 0, global.reportType,
									time);
							global.emaHashMap.replace(emaID, emaClass_temp);
						}

						/*--------------------------------------------------------------*/

						drmsg.put("SrcEMA", global.SYSTEM_ID);
						drmsg.put("DestEMA", srcEMA);
						drmsg.put("service", "ConnectResponse");
						// drmsg.put("EMAID", emaID);
						drmsg.put("reportType", global.reportType);
						drmsg.put("time", time);

						p_topic = "CEMA/" + srcEMA + "/Response/ConnectACK";
						m_msg = drmsg.toString();
						sendMessage(p_topic, m_msg);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (topic.equals("Implicit")) {
			try {

				if (payload.get("service").equals("PeriodicalImplicit")) {

					srcEMA = (String) payload.get("SrcEMA");
					emaID = srcEMA;
					emaCnt = payload.getInt("emaCNT");
					power = payload.getDouble("power");
					margin = payload.getDouble("margin");
					generate = payload.getDouble("generate");
					storage = payload.getDouble("storage");

					double minValue = payload.getDouble("minValue");
					double maxValue = payload.getDouble("maxValue");
					double avgValue = payload.getDouble("avgValue");

					String maxTime = (String) payload.get("maxTime");
					String minTime = (String) payload.get("minTime");

					String state = (String) payload.get("state");
					int dimming = payload.getInt("dimming");

					tempTime = (String) payload.get("time");

					try {
						time = transFormat.parse(tempTime);
						maxTime_d = transFormat.parse(maxTime);
						minTime_d = transFormat.parse(minTime);

						if (global.emaHashMap.containsKey(emaID)) {
							emaClass_temp = new EMAClass_temp(emaID, "MQTT", power, margin, generate, storage, 0,
									global.reportType, time);
							global.emaHashMap.replace(emaID, emaClass_temp);
						}

						drmsg.put("SrcEMA", global.SYSTEM_ID);
						drmsg.put("DestEMA", srcEMA);
						drmsg.put("service", "PeriodicalImplicitACK");
						drmsg.put("emaCNT", emaCnt);
						drmsg.put("power", power);
						drmsg.put("minValue", minValue);
						drmsg.put("maxValue", maxValue);
						drmsg.put("avgValue", avgValue);
						drmsg.put("margin", margin);
						drmsg.put("generate", generate);
						drmsg.put("storage", storage);
						drmsg.put("time", time);
						drmsg.put("maxTime", maxTime_d);
						drmsg.put("minTime", minTime_d);
						drmsg.put("state", state);
						drmsg.put("dimming", dimming);

						p_topic = "CEMA/" + srcEMA + "/Periodical/ImplicitACK";
						m_msg = drmsg.toString();
						sendMessage(p_topic, m_msg);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (topic.equals("Explicit")) {
			String sort;

			try {

				if (payload.get("service").equals("PeriodicalExplicit")) {

					srcEMA = (String) payload.get("SrcEMA");
					emaID = srcEMA;
					emaCnt = payload.getInt("emaCNT");
					power = payload.getDouble("power");
					margin = payload.getDouble("margin");
					generate = payload.getDouble("generate");
					storage = payload.getDouble("storage");

					double minValue = payload.getDouble("minValue");
					double maxValue = payload.getDouble("maxValue");
					double avgValue = payload.getDouble("avgValue");

					String maxTime = (String) payload.get("maxTime");
					String minTime = (String) payload.get("minTime");

					String state = (String) payload.get("state");
					int dimming = payload.getInt("dimming");

					tempTime = (String) payload.get("time");

					try {
						time = transFormat.parse(tempTime);
						maxTime_d = transFormat.parse(maxTime);
						minTime_d = transFormat.parse(minTime);
						String explicitDetail = payload.get("Explicit").toString();

						try {
							JSONArray exArray = new JSONArray(explicitDetail);
							for (int i = 0; i < exArray.length(); i++) {

								JSONObject subPayload = exArray.getJSONObject(i);

								sort = (String) subPayload.get("deviceType");
								// if (emaID.equals("1")) {
								// System.out.println("1번일때만");
								devDataStore(emaID, sort, subPayload, time);
								// }

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (global.emaHashMap.containsKey(emaID)) {
							emaClass_temp = new EMAClass_temp(emaID, "MQTT", power, margin, generate, storage, 0,
									global.reportType, time);
							global.emaHashMap.replace(emaID, emaClass_temp);
							// EMA_tab_temp.modify_EMA_table();
						}
						// System.out.println(global.emaHashMap.get(emaID));

						drmsg.put("SrcEMA", global.SYSTEM_ID);
						drmsg.put("DestEMA", srcEMA);
						drmsg.put("service", "PeriodicalExplicitACK");
						drmsg.put("emaCNT", emaCnt);
						drmsg.put("power", power);
						drmsg.put("minValue", minValue);
						drmsg.put("maxValue", maxValue);
						drmsg.put("avgValue", avgValue);
						drmsg.put("margin", margin);
						drmsg.put("generate", generate);
						drmsg.put("storage", storage);
						drmsg.put("time", time);
						drmsg.put("maxTime", maxTime_d);
						drmsg.put("minTime", minTime_d);
						drmsg.put("state", state);
						drmsg.put("dimming", dimming);

						p_topic = "CEMA/" + srcEMA + "/Periodical/ExplicitACK";
						m_msg = drmsg.toString();
						sendMessage(p_topic, m_msg);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (payload.get("Type").equals("ExplicitResponse")) {

					srcEMA = (String) payload.get("SrcEMA");
					emaID = (String) payload.get("EMAID");
					emaCnt = (Integer) payload.get("EMANUM");
					power = (double) payload.get("Power");
					margin = (double) payload.get("Margin");
					generate = (double) payload.get("Generate");
					storage = (double) payload.get("Storage");
					tempTime = (String) payload.get("Time");

					try {
						time = transFormat.parse(tempTime);
						String explicitDetail = payload.get("Explicit").toString();

						try {
							JSONArray exArray = new JSONArray(explicitDetail);
							for (int i = 0; i < exArray.length(); i++) {

								JSONObject subPayload = exArray.getJSONObject(i);
								sort = (String) subPayload.get("Sort");
								devDataStore(emaID, sort, subPayload, time);

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (global.emaHashMap.containsKey(emaID)) {
							emaClass_temp = new EMAClass_temp(emaID, "MQTT", power, margin, generate, storage, 0,
									global.reportType, time);
							global.emaHashMap.replace(emaID, emaClass_temp);
							// EMA_tab_temp.modify_EMA_table();
						}

						// System.out.println(global.emaHashMap.get(emaID));

						drmsg.put("SrcEMA", global.SYSTEM_ID);
						drmsg.put("DestEMA", srcEMA);
						drmsg.put("service", "ExplicitResponseACK");
						drmsg.put("EMANUM", emaCnt);
						drmsg.put("EMAID", emaID);
						drmsg.put("Power", power);
						drmsg.put("Margin", margin);
						drmsg.put("Generate", generate);
						drmsg.put("Storage", storage);
						drmsg.put("Time", time);

						p_topic = "CEMA/" + srcEMA + "/ACK/ExplicitACK";
						m_msg = drmsg.toString();

						sendMessage(p_topic, m_msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(String topic, String payload) {

		new Publishing().publishThread(this.client, topic, global.qos, payload.getBytes());

	}

	public void devDataStore(String emaID, String sort, JSONObject subPayload, Date time) {
		String deviceEMAID = null, state = null;
		int priority = 0, dimming = 0, mode = 0;
		double devPower = 0, devGenerate = 0, capacity = 0, soc = 0, chargedEnergy = 0, volt = 0, hz = 0;

		if (sort.equals("LED")) {

			try {
				// System.out.println("@@");
				// System.out.println("들어왔다");
				// System.out.println("@@");
				deviceEMAID = (String) subPayload.get("deviceEMAID");
				devPower = (Double) subPayload.getDouble("power");
				state = (String) subPayload.get("state");
				dimming = (Integer) subPayload.getInt("dimming");
				priority = (Integer) subPayload.getInt("priority");

				if (!global.devHashMap.containsKey("SUB_DEVICE" + deviceEMAID)) {
					deviceClass_temp = new DeviceClass_temp("DEVICE" + emaID, deviceEMAID, state, sort, priority,
							dimming, mode, devPower, devGenerate, capacity, soc, chargedEnergy, volt, hz, time);
					global.devHashMap.put("SUB_DEVICE" + deviceEMAID, deviceClass_temp);
					// System.out.println("-----------LED------------");
					// System.out.println(global.devHashMap.get(deviceEMAID));
				} else {
					deviceClass_temp = new DeviceClass_temp("DEVICE" + emaID, deviceEMAID, state, sort, priority,
							dimming, mode, devPower, devGenerate, capacity, soc, chargedEnergy, volt, hz, time);
					global.devHashMap.replace("SUB_DEVICE" + deviceEMAID, deviceClass_temp);
					// System.out.println("-----------LED------------");
					// System.out.println(global.devHashMap.get(deviceEMAID));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (sort.equals("PV")) {

			try {

				deviceEMAID = (String) subPayload.get("deviceEMAID");
				devPower = (Double) subPayload.get("power");
				state = (String) subPayload.get("state");
				priority = (Integer) subPayload.get("priority");

				if (!global.devHashMap.containsKey(deviceEMAID)) {
					deviceClass_temp = new DeviceClass_temp("DEVICE" + emaID, deviceEMAID, state, sort, priority,
							dimming, mode, devPower, devGenerate, capacity, soc, chargedEnergy, volt, hz, time);
					global.devHashMap.put("SUB_DEVICE" + deviceEMAID, deviceClass_temp);
					// System.out.println("-----------PV------------");
					// System.out.println(global.devHashMap.get(deviceEMAID));
				} else {
					deviceClass_temp = new DeviceClass_temp("DEVICE" + emaID, deviceEMAID, state, sort, priority,
							dimming, mode, devPower, devGenerate, capacity, soc, chargedEnergy, volt, hz, time);
					global.devHashMap.replace("SUB_DEVICE" + deviceEMAID, deviceClass_temp);
					// System.out.println("-----------PV------------");
					// System.out.println(global.devHashMap.get(deviceEMAID));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (sort.equals("ESS")) {

			try {

				deviceEMAID = (String) subPayload.get("deviceEMAID");
				mode = (Integer) subPayload.get("mode");
				devPower = (Double) subPayload.get("power");
				state = (String) subPayload.get("state");

				capacity = (Double) subPayload.get("capacity");
				soc = (Double) subPayload.get("soc");
				volt = (Double) subPayload.get("volt");
				hz = (Double) subPayload.get("hz");
				chargedEnergy = (Double) subPayload.get("chargedEnergy");

				priority = (Integer) subPayload.get("priority");

				if (!global.devHashMap.containsKey(deviceEMAID)) {
					deviceClass_temp = new DeviceClass_temp("DEVICE" + emaID, deviceEMAID, state, sort, priority,
							dimming, mode, devPower, devGenerate, capacity, soc, chargedEnergy, volt, hz, time);
					global.devHashMap.put("SUB_DEVICE" + deviceEMAID, deviceClass_temp);
					// System.out.println("-----------ESS------------");
					// System.out.println(global.devHashMap.get(deviceEMAID));
				} else {
					deviceClass_temp = new DeviceClass_temp("DEVICE" + emaID, deviceEMAID, state, sort, priority,
							dimming, mode, devPower, devGenerate, capacity, soc, chargedEnergy, volt, hz, time);
					global.devHashMap.replace("SUB_DEVICE" + deviceEMAID, deviceClass_temp);
					// System.out.println("-----------ESS------------");
					// System.out.println(global.devHashMap.get(deviceEMAID));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (sort.equals("RECLOSER")) {

			try {

				deviceEMAID = (String) subPayload.get("deviceEMAID");
				state = (String) subPayload.get("state");
				devPower = (Double) subPayload.get("power");

				volt = (Double) subPayload.get("volt");
				hz = (Double) subPayload.get("hz");

				priority = (Integer) subPayload.get("priority");

				if (!global.devHashMap.containsKey(deviceEMAID)) {
					deviceClass_temp = new DeviceClass_temp("DEVICE" + emaID, deviceEMAID, state, sort, priority,
							dimming, mode, devPower, devGenerate, capacity, soc, chargedEnergy, volt, hz, time);
					global.devHashMap.put("SUB_DEVICE" + deviceEMAID, deviceClass_temp);
					// System.out.println("-----------Recloser------------");
					// System.out.println(global.devHashMap.get(deviceEMAID));
				} else {
					deviceClass_temp = new DeviceClass_temp("DEVICE" + emaID, deviceEMAID, state, sort, priority,
							dimming, mode, devPower, devGenerate, capacity, soc, chargedEnergy, volt, hz, time);
					global.devHashMap.replace("SUB_DEVICE" + deviceEMAID, deviceClass_temp);
					// System.out.println("-----------Recloser------------");
					// System.out.println(global.devHashMap.get(deviceEMAID));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (sort.equals("RESOURCE")) {

			try {

				deviceEMAID = (String) subPayload.get("deviceEMAID");
				state = (String) subPayload.get("state");
				devPower = (Double) subPayload.get("power");
				priority = (Integer) subPayload.get("priority");

				if (!global.devHashMap.containsKey(deviceEMAID)) {
					deviceClass_temp = new DeviceClass_temp("DEVICE" + emaID, deviceEMAID, state, sort, priority,
							dimming, mode, devPower, devGenerate, capacity, soc, chargedEnergy, volt, hz, time);
					global.devHashMap.put("SUB_DEVICE" + deviceEMAID, deviceClass_temp);
					// System.out.println("-----------Resource------------");
					// System.out.println(global.devHashMap.get(deviceEMAID));
				} else {
					deviceClass_temp = new DeviceClass_temp("DEVICE" + emaID, deviceEMAID, state, sort, priority,
							dimming, mode, devPower, devGenerate, capacity, soc, chargedEnergy, volt, hz, time);
					global.devHashMap.replace("SUB_DEVICE" + deviceEMAID, deviceClass_temp);
					// System.out.println("-----------Resource------------");
					// System.out.println(global.devHashMap.get(deviceEMAID));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
