package com.mir.ems.price;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.mir.ems.globalVar.global;

public class RealTimePriceFileReader {

	private String fileName;

	public RealTimePriceFileReader(String fileName) {

		setFileName(fileName);
		String type = null;

		global.realTimeTableHasChanged = true;

		Iterator<String> it = global.emaProtocolCoAP.keySet().iterator();

		while (it.hasNext()) {

			String key = it.next();
			if (!global.emaProtocolCoAP.get(key).isPullModel())
				global.emaProtocolCoAP.get(key).setRealTimetableChanged(true);

		}

		try {
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			StringTokenizer st;

			while ((type = bufferedReader.readLine()) != null) {

				st = new StringTokenizer(type, " ");

				String strTime = st.nextToken();
				String endTime = st.nextToken();
				double price = Double.parseDouble(st.nextToken());

				global.realTimePriceTable.add(new Industrial_RealTime(strTime, endTime, price));

			}

			// Calendar now = Calendar.getInstance();
			// int cHour = now.get(Calendar.HOUR_OF_DAY);
			//
			// System.out.println("@@@@" + cHour);
			//
			// for (int i = 0; i < priceTable.size(); i++) {
			//
			// if (Integer.parseInt(priceTable.get(i).strTime.split(":")[0]) >=
			// cHour) {
			// System.out.println(priceTable.get(i).strTime);
			// System.out.println(priceTable.get(i).price);
			//
			// }
			//
			// }

			bufferedReader.close();

		} catch (FileNotFoundException ex) {

			System.out.println("Unable to open file '" + fileName + "'");

		} catch (IOException ex) {

			System.out.println("Error reading file '" + fileName + "'");

		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
