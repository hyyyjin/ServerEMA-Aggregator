package com.mir.ems.Graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;


@SuppressWarnings("serial")
public class DRSchedulingGraph extends JPanel {

	public static Date d = new Date();
	public static GregorianCalendar gcalendar = new GregorianCalendar();
	public static TimeSeries seriesArray[] = new TimeSeries[21];
	public static TimeSeries currentTime;

	public static Calendar a = Calendar.getInstance();

	public static TimeSeries totalSeries;

	public DRSchedulingGraph() {
		setBackground(new Color(240, 240, 240));
		setPreferredSize(new Dimension(1003, 618));
		setMinimumSize(new Dimension(800, 500));
		setMaximumSize(new Dimension(800, 500));

		setLayout(new BorderLayout());

		JFreeChart g = ChartFactory.createTimeSeriesChart(null, "Time", "Energy Usage", createData(), true,
				true, true);

		XYPlot plot = (XYPlot) g.getPlot();
		// ����ɫ ͸����
		plot.setBackgroundAlpha(0f);
		// ǰ��ɫ ͸����
		plot.setForegroundAlpha(1f);		

		ChartPanel cp = new ChartPanel(g);			
		add(cp);
		validate();
	}

	@SuppressWarnings("deprecation")
	private TimeSeriesCollection createData() {


		TimeSeriesCollection data = new TimeSeriesCollection();
		Day day = new Day(a.get(Calendar.DATE), a.get(Calendar.MONTH) + 1, a.get(Calendar.YEAR));
		// Hour preHour = new Hour(gcalendar.get(Calendar.HOUR) - 1, day);
		// Hour currentHour = new Hour(gcalendar.get(Calendar.HOUR), day);

		Hour nextHour = new Hour(d.getHours() + 1, day);
		Hour currentHour = new Hour(d.getHours(), day);


		currentTime = new TimeSeries("Current Time");
		totalSeries = new TimeSeries("Total Energy Usage");

		currentTime.add(new Second(0, d.getMinutes(), d.getHours(), a.get(Calendar.DATE), a.get(Calendar.MONTH) + 1,
				a.get(Calendar.YEAR)), 0);
		currentTime.add(new Second(60, d.getMinutes(), d.getHours(), a.get(Calendar.DATE), a.get(Calendar.MONTH) + 1,
				a.get(Calendar.YEAR)), 100);
		data.addSeries(currentTime);

		for (int j = 0; j < 21; j++) {
			seriesArray[j] = new TimeSeries("EMA " + j);
			for (int i = 0; i < 60; i++) {

				seriesArray[j].addOrUpdate(new Minute(i, currentHour), 0);
				seriesArray[j].addOrUpdate(new Minute(i, nextHour), 0);
			}
			data.addSeries(seriesArray[j]);
		}

		for (int i = 0; i < 60; i++) {

			totalSeries.addOrUpdate(new Minute(i, nextHour), 0);
			totalSeries.addOrUpdate(new Minute(i, currentHour), 0);
		}

		data.addSeries(totalSeries);

		return data;
	}

	@SuppressWarnings("unused")
	public static void updateScheduling(int indexOfVen, Minute startTime, Minute endTime, int value) {

		int numberOfHour;
		int numberOfMinute;
		numberOfHour = endTime.getHourValue() - startTime.getHourValue();
		numberOfMinute = endTime.getMinute() - startTime.getMinute();

		if (numberOfHour > 0) {
			int i = startTime.getMinute();

			while (i != 60) {

				seriesArray[indexOfVen].addOrUpdate(new Minute(i++, startTime.getHour()), value);
				// update total

				int totalValue = 0;
				for (int k = 0; k < 21; k++) {
					totalValue = totalValue
							+ seriesArray[k].getValue(new Minute(i - 1, startTime.getHour())).intValue();

				}
				totalSeries.addOrUpdate(new Minute(i - 1, startTime.getHour()), totalValue);

			}

			int j = 0;
			n: while (j < numberOfHour) {
				i = 0;
				while (i != 60) {
					seriesArray[indexOfVen].addOrUpdate(
							new Minute(i++, startTime.getHourValue() + 1 + j, startTime.getDay().getDayOfMonth(),
									startTime.getDay().getMonth(), startTime.getDay().getYear()),
							value);

					// update total

					int totalValue = 0;
					for (int k = 0; k < 100; k++) {
						if (seriesArray[k].getValue(new Minute(i - 1, startTime.getHourValue() + 1 + j,
								a.get(Calendar.DATE), a.get(Calendar.MONTH) + 1, a.get(Calendar.YEAR))) == null) {
							seriesArray[k].addOrUpdate(new Minute(i - 1, startTime.getHourValue() + 1 + j,
									a.get(Calendar.DATE), a.get(Calendar.MONTH) + 1, a.get(Calendar.YEAR)), 0);
						}
						totalValue = totalValue
								+ seriesArray[k]
										.getValue(new Minute(i - 1, startTime.getHourValue() + 1 + j,
												a.get(Calendar.DATE), a.get(Calendar.MONTH) + 1, a.get(Calendar.YEAR)))
										.intValue();

					}
					totalSeries.addOrUpdate(new Minute(i - 1, startTime.getHourValue() + 1 + j, a.get(Calendar.DATE),
							a.get(Calendar.MONTH) + 1, a.get(Calendar.YEAR)), totalValue);

					if (startTime.getHourValue() + 1 + j == endTime.getHourValue() && i - 1 == endTime.getMinute()) {
						break n;
					}

				}
				j++;

			}

		} else {
			int count = 0;
			for (int i = startTime.getMinute(); i <= endTime.getMinute(); i++) {
				seriesArray[indexOfVen].addOrUpdate(new Minute(i, startTime.getHour()), value);
//				System.out.println("" + (count++));

				int totalValue = 0;
				for (int k = 0; k < 21; k++) {
					totalValue = totalValue + seriesArray[k].getValue(new Minute(i, startTime.getHour())).intValue();

				}
				totalSeries.addOrUpdate(new Minute(i, startTime.getHour()), totalValue);

			}

		}

	}

	@SuppressWarnings("unused")
	public static int getCurrentValue(int index, Minute time) {
		int value = 0;

		if (seriesArray[index].getValue(time) == null) {
			return 0;
		} else {
			return seriesArray[index].getValue(time).intValue();
		}

	}

	@SuppressWarnings("unused")
	public static int getTotalValue(Minute time) {
		int value = 0;

		if (totalSeries.getValue(time) == null) {
			return 0;
		} else {
			return totalSeries.getValue(time).intValue();
		}

	}

	public static int getTotalEnergy(int time) {

		// System.out.println("" + totalSeries.getDataItem(time -
		// 1).getYValue());

		// return (int) totalSeries.getDataItem(time - 1).getYValue();
		return 0;
	}

	public static int getNodeEnergy(int index, int time) {

		// System.out.println("" + seriesArray[index].getDataItem(time -
		// 1).getYValue());

		// return (int) seriesArray[index].getDataItem(time - 1).getYValue();
		return 0;
	}

}
