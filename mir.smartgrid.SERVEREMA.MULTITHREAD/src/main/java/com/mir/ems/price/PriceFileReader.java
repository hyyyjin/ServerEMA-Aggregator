package com.mir.ems.price;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.mir.ems.globalVar.global;

public class PriceFileReader {

	private String fileName;

	public PriceFileReader(String fileName) {

		
		global.tableHasChanged = true;
		
		Iterator<String> it = global.emaProtocolCoAP.keySet().iterator();
		while(it.hasNext()){
			
			String key = it.next();
			global.emaProtocolCoAP.get(key).setTableChanged(true);
			
		}
		
		setFileName(fileName);
		String type = null;

		try {
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			StringTokenizer st;

			while ((type = bufferedReader.readLine()) != null) {

				if (type.equals("Industrial1")) {

					String temp;

					while (!(temp = bufferedReader.readLine()).equals("---")) {

						st = new StringTokenizer(temp, " ");

						String stage = st.nextToken();
						double price = Double.parseDouble(st.nextToken());
						double summer = Double.parseDouble(st.nextToken());
						double sprinFall = Double.parseDouble(st.nextToken());
						double winter = Double.parseDouble(st.nextToken());

						global.priceTable.add(new Industrial(type, stage, price, summer, sprinFall, winter));
					}

				} else if (type.matches("Industrial2|Industrial3")) {

					String temp;

					while (!(temp = bufferedReader.readLine()).equals("---")) {

						st = new StringTokenizer(temp, " ");

						String stage = st.nextToken();
						double price = Double.parseDouble(st.nextToken());
						double lightSummer = Double.parseDouble(st.nextToken());
						double lightSpringFall = Double.parseDouble(st.nextToken());
						double lightWinter = Double.parseDouble(st.nextToken());
						double midSummer = Double.parseDouble(st.nextToken());
						double midSpringFall = Double.parseDouble(st.nextToken());
						double midWinter = Double.parseDouble(st.nextToken());
						double maxSummer = Double.parseDouble(st.nextToken());
						double maxSpringFall = Double.parseDouble(st.nextToken());
						double maxWinter = Double.parseDouble(st.nextToken());

						global.priceTable.add(new Industrial(type, stage, price, lightSummer, lightSpringFall,
								lightWinter, midSummer, midSpringFall, midWinter, maxSummer, maxSpringFall, maxWinter));
					}
				} else if (type.matches("Season|Season-Winter")) {

					String temp;

					while (!(temp = bufferedReader.readLine()).equals("---")) {

						st = new StringTokenizer(temp, " ");

						String season = type;
						String stage = st.nextToken();
						String strTime;
						String endTime;

						int size = st.countTokens();

						if (st.countTokens() > 2) {

							for (int i = 0; i < (size / 2); i++) {

								strTime = st.nextToken();
								endTime = st.nextToken();

								global.priceTable
										.add(new Industrial(season, new SeasonTimetable(stage, strTime, endTime)));

							}

						} else {

							strTime = st.nextToken();
							endTime = st.nextToken();

							global.priceTable.add(new Industrial(season, new SeasonTimetable(stage, strTime, endTime)));
						}

					}

				}
			}

			
			global.createTableDate = new Date(System.currentTimeMillis()).toString();
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
