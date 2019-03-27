package com.mir.ems.mqtt.emap;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.mir.ems.globalVar.global;
import com.mir.ems.main.Connection;
import com.mir.ems.mqtt.Publishing;
import com.mir.ems.profile.emap.v2.Summary;
import com.mir.ems.profile.emap.v2.SummaryReport;

public class PeriodicalSummaryReport extends Thread {

	private Connection connection;
	
	public PeriodicalSummaryReport(Connection connection) {
		// TODO Auto-generated constructor stub
		setConnection(connection);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Timer timer = new Timer();
		timer.schedule(new UpdateTask(), 5000, 5000);

	}

	private class UpdateTask extends TimerTask {

		public void run() {


			Iterator<String> topIt = global.emaProtocolCoAP.keySet().iterator();

			while (topIt.hasNext()) {
				
				String topKey = topIt.next();
				
				Iterator<String> it = global.emaProtocolCoAP.keySet().iterator();
				Summary sm = new Summary();

				while (it.hasNext()) {

					String key = it.next();

					sm.addsummaryParam(key, global.emaProtocolCoAP.get(key).getMargin(),
							global.emaProtocolCoAP.get(key).getAvgValue(),
							global.emaProtocolCoAP.get(key).getMaxValue(),
							global.emaProtocolCoAP.get(key).getGenerate(), global.emaProtocolCoAP.get(key).getStorage(),
							global.emaProtocolCoAP.get(key).getPower());

				}

				SummaryReport sr = new SummaryReport();
				sr.setDestEMA(topKey);
				sr.setRequestID("requestID");
				sr.setService("SummaryReport");
				sr.setSrcEMA(global.SYSTEM_ID);
				sr.setSummary(sm.getEventParams());
				sr.setSummaryType("SummaryReport");
				
				String topic = "/EMAP/" + topKey + "/1.0b/Summary";
				String setPayload = sr.toString();
				new Publishing().publishThread(connection.getEmapmqttclient().getMqttClient(), topic, 0, setPayload.getBytes());
				
			}
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	

}
