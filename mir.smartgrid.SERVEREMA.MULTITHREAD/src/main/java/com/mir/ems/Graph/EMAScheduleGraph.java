package com.mir.ems.Graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;

import com.mir.ems.globalVar.global;
import com.mir.ems.schedule.ScheduleManager;

import scala.collection.Set;

/*
 * 2018.03.21 - Edited HYUNJIN PARK Two Dimension Object Array Size: 20x3 (
 * fixed, Given 20 EMA'S ) List Double & Date Size: 7 ( Based 7 Days )
 * ________________________________________ 
 * |
 *  |        | | | String | List Double |
 * List Date | | EMA ID | XAXIS Value | YXIS Value |
 * |_________|_______________|______________|
 * 
 */

@SuppressWarnings("serial")
public class EMAScheduleGraph extends JPanel implements ExampleChart<CategoryChart> {

	final int numberOfEMA = 20;

	public CategoryChart xyChart;
	Object[][] emaArray = new Object[numberOfEMA][3];

	@SuppressWarnings("unchecked")
	public EMAScheduleGraph() {
		final CategoryChart chart = getChart();

		setBounds(14, 60, 1467, 700);
		setLayout(null);

		@SuppressWarnings("rawtypes")
		XChartPanel chartPanel = new XChartPanel(chart);
		chartPanel.setBackground(Color.WHITE);
		chartPanel.setBounds(0, 0, 1467, 690);
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
		timer.scheduleAtFixedRate(chartUpdaterTask, 2000, 4000);

	}

	@SuppressWarnings("unchecked")
	@Override
	public CategoryChart getChart() {
		// TODO Auto-generated method stub

		xyChart = new CategoryChartBuilder().width(500).height(400).title("Demand Response Schedule Monitoring")
				.xAxisTitle("Date").yAxisTitle("Reserved Threshold").build();

		xyChart.getStyler().setLegendPosition(LegendPosition.OutsideE);
		xyChart.getStyler().setAvailableSpaceFill(.96);
		xyChart.getStyler().setDatePattern("yyyy-MM-dd");
		xyChart.getStyler().setOverlapped(true);
		xyChart.getStyler().setYAxisMax(1500.0);

		for (int i = 0; i < numberOfEMA; i++) {
			emaArray[i][0] = "ClientEMA" + (i + 1);
			emaArray[i][1] = getInitXAXIS();
			emaArray[i][2] = getInitYAXIS();

			xyChart.addSeries((String) emaArray[i][0], (List<Date>) emaArray[i][1], (List<Double>) emaArray[i][2]);

		}

		xyChart.getStyler().setLegendSeriesLineLength(300);

		return xyChart;

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void updateData() {

		Calendar calendar = Calendar.getInstance();
		Date currentDate;

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		currentDate = new Date((year - 1900), month, day);

		List<Date> list3;
		list3 = (List<Date>) emaArray[0][1];

		// 가장 최근 날짜(Index 0값이) 현재 날짜와 동일할 때 Update Table
		if (list3.get(1).equals(currentDate)) {

			for (int i = 0; i < 20; i++) {
				List<Double> YAXIS_list1 = new ArrayList<Double>(7);
				List<Date> XAXIS_list1 = new ArrayList<Date>(7);
				for (int j = 0; j < 7; j++) {
					YAXIS_list1.add(2000.0);

					Calendar calendar1 = Calendar.getInstance();

					int year1 = calendar1.get(Calendar.YEAR);
					int month1 = calendar1.get(Calendar.MONTH);
					int day1 = calendar1.get(Calendar.DATE) + (j + 1);
					XAXIS_list1.add(currentDate = new Date((year1 - 1900), month1, day1));

					new ScheduleManager().setAvailbleDate(new Date((year1 - 1900), month1, day1));

				}

				emaArray[i][1] = XAXIS_list1;
				emaArray[i][2] = YAXIS_list1;
			}

			for (int i = 0; i < 20; i++) {

				xyChart.updateCategorySeries((String) emaArray[i][0], (List<Date>) emaArray[i][1],
						(List<Double>) emaArray[i][2], null);

			}

		}

		// Periodical Update Table
		else {

			for (int i = 0; i < 20; i++) {
				List<Double> YAXIS_list1 = new ArrayList<Double>(7);
				List<Date> XAXIS_list1 = new ArrayList<Date>(7);
				for (int j = 0; j < 7; j++) {

					Calendar calendar1 = Calendar.getInstance();

					int year1 = calendar1.get(Calendar.YEAR);
					int month1 = calendar1.get(Calendar.MONTH);
					int day1 = calendar1.get(Calendar.DATE) + j;
					XAXIS_list1.add(currentDate = new Date((year1 - 1900), month1, day1));

					// System.out.println(currentDate);

					// System.out.println(global.);

					if (global.scheduleDate.get(emaArray[i][0].toString().toUpperCase()) != null) {

						if (global.scheduleDate.get(emaArray[i][0].toString().toUpperCase()).getTimeStamp()
								.equals(currentDate.toString())) {

							double power = global.scheduleDate.get(emaArray[i][0].toString().toUpperCase()).getPower();

							YAXIS_list1.add(power);

						} else {

							YAXIS_list1.add(0.0);

						}

					} else {
						YAXIS_list1.add(0.0);
					}

				}

				emaArray[i][1] = XAXIS_list1;
				emaArray[i][2] = YAXIS_list1;
			}

			for (int i = 1; i < 20; i++) {

				double power = 0;

				List<Double> YAXIS_list1 = new ArrayList<Double>(7);
				List<Double> YAXIS_list_previous = new ArrayList<Double>(7);

				YAXIS_list1 = (List<Double>) emaArray[i][2];
				YAXIS_list_previous = (List<Double>) emaArray[i - 1][2];

				for (int j = 0; j < 7; j++) {
					if (YAXIS_list1.get(j) > 0) {

						power = YAXIS_list1.get(j) + YAXIS_list_previous.get(j);

						YAXIS_list_previous.set(j, power);

					}

					// System.out.print(" "+YAXIS_list1.get(j));
				}

				// System.out.println("EMAID: "+emaArray[i][0]+"-->
				// "+YAXIS_list1);

				emaArray[i - 1][2] = YAXIS_list_previous;

			}

			for (int i = 0; i < 20; i++) {

				xyChart.updateCategorySeries((String) emaArray[i][0], (List<Date>) emaArray[i][1],
						(List<Double>) emaArray[i][2], null);

			}

		}

	}

	@SuppressWarnings("deprecation")
	public List<Date> getInitXAXIS() {
		List<Date> xAxis_init_list = new ArrayList<Date>(7);

		for (int j = 0; j < 7; j++) {

			Calendar calendar = Calendar.getInstance();

			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DATE) + (j + 1);
			xAxis_init_list.add(new Date((year - 1900), month, day));

			new ScheduleManager().setAvailbleDate(new Date((year - 1900), month, day));

		}

		return xAxis_init_list;
	}

	public List<Double> getInitYAXIS() {
		List<Double> yAxis_init_list = new ArrayList<Double>(7);

		for (int j = 0; j < 7; j++) {
			yAxis_init_list.add(1000.0);
		}

		return yAxis_init_list;
	}

}