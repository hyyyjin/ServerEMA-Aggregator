package com.mir.ems.Graph;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

import com.mir.ems.deviceProfile.EMA_tab_temp;
import com.mir.ems.globalVar.global;

import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public class EMARealTimeGraph extends JPanel implements ExampleChart<XYChart> {
	public XYChart xyChart;

	public List<Date> xData = new ArrayList<Date>();

	public List<Double> yData = new ArrayList<Double>();


	Object[][] arr = new Object[20][2];

	public List<Double> zData1 = new ArrayList<Double>();

	public HashMap<String, List<Double>> yVal = new HashMap<String, List<Double>>();

	public final int emaNum= 20;
	public static final String SERIES_NAME = "EMA1";
	public static final String SERIES_NAME2 = "EMA2";
	public static final String SERIES_NAME3 = "EMA3";
	public static int emaCnt = 0;

	public EMARealTimeGraph() {

		setBounds(14, 60, 1467, 700);

		final XYChart chart = getChart();
		setLayout(null);
		XChartPanel chartPanel = new XChartPanel(chart);
		chartPanel.setBackground(Color.WHITE);
		chartPanel.setBounds(0, 0, 1467, 700);
		add(chartPanel);

		TimerTask chartUpdaterTask = new TimerTask() {

			@Override
			public void run() {

				updateData();

				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						repaint();
					}
				});
			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 2000, 2000);
//		timer.
	}

	@SuppressWarnings("unchecked")
	public XYChart getChart() {

		// Create Chart
		xyChart = new XYChartBuilder().width(500).height(400).theme(ChartTheme.Matlab)
				.title("[Real-time] EACH EMA Energy Usage Value Monitoring").build();

		xyChart.getStyler().setChartTitlePadding(15);
		
		
		xyChart.getStyler().setXAxisLabelRotation(60);
		xyChart.getStyler().setDatePattern("MM-dd HH:mm:ss");
		xyChart.setXAxisTitle("Time");
		xyChart.setYAxisTitle("Energy Usage Value");

		
		xData = getCurrentTime();

		for (int i = 0; i < 20; i++) {
			arr[i][0] = "CLIENT_EMA" + (i+1);
			arr[i][1] = init();
			xyChart.addSeries((String) arr[i][0], xData, (List<Double>)arr[i][1]);
		}



		return xyChart;
	}

	@SuppressWarnings("unchecked")
	public void updateData() {
		List<Date> newXdata = getCurrentTime();
		xData.addAll(newXdata);
		int emaListSize;
		emaListSize = global.emaProtocolCoAP.size();


		for (int i = 0; i < emaListSize; i++) {
			arr[i][1] = getRandomData(arr[i][0].toString(), (List<Double>) arr[i][1], i);

		}
		
		for(int i=emaListSize;i<20; i++){
			arr[i][1] = getRandomData(arr[i][0].toString(), (List<Double>) arr[i][1], i);
		}
//		
		for(int i=0; i<20; i++){
			xyChart.updateXYSeries((String)arr[i][0], xData, (List<Double>)arr[i][1], null);
		}

	}

	private static List<Double> getRandomData(String emaID, List<Double> v, int gwNum) {
		List<Double> data = new CopyOnWriteArrayList<Double>();

		try{
//			double powerVal = Double.parseDouble(EMA_tab_temp.ema_table_model.getValueAt(gwNum, 5).toString());

			
			double powerVal = 0;
			if(global.emaProtocolCoAP.keySet().size()>0){
				
				if(global.emaProtocolCoAP.keySet().contains(emaID)){
					powerVal = Double.parseDouble(global.emaProtocolCoAP.get(emaID).getPower()+"");
				}
				
//				powerVal = Double.parseDouble(global.emaProtocolCoAP.get("CLIENT_EMA"+(gwNum+1)).getPower()+"");
				
			}

			v.add(powerVal);
		
		}catch(IndexOutOfBoundsException e){

			v.add(0.0);

		}
		
		data.addAll(v);
		return data;
	}
	
	private static List<Double> init() {
		List<Double> data = new CopyOnWriteArrayList<Double>();

		data.add(0.0);
		return data;
	}
	
	private List<Date> getCurrentTime() {
		List<Date> data = new ArrayList<Date>();

		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);

		DateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		Date date = null;

		try {
			date = sdf.parse(month + "-" + day + " " + hour + ":" + min + ":" + sec);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.add(date);

		return data;
	}
}
