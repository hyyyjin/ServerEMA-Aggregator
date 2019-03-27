package com.mir.ems.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;

import com.mir.ems.globalVar.global;
import com.mir.ems.price.PriceFileReader;

public class FileIo {

	public static void main(String[] args) {


		
		
		new PriceFileReader("ElectricityPrice.txt");
		
		
		System.out.println(global.priceTable.get(0).getPrice());
		
		
		
		
		
//		Vector<IndustrialRealTime> priceTable = new Vector<>();
//
//		String fileName = "RealTimePricetable.txt";
//		String type = null;
//
//		try {
//			FileReader fileReader = new FileReader(fileName);
//
//			BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//			StringTokenizer st;
//
//			while ((type = bufferedReader.readLine()) != null) {
//
//				st = new StringTokenizer(type, " ");
//
//				String strTime = st.nextToken();
//				String endTime = st.nextToken();
//				double price = Double.parseDouble(st.nextToken());
//				
//				priceTable.add(new IndustrialRealTime(strTime, endTime, price));
//				
//			}
//			
//			Calendar now = Calendar.getInstance();
//			int cHour= now.get(Calendar.HOUR_OF_DAY);
//			
//			System.out.println("@@@@"+cHour);
//			
//			for(int i=0; i<priceTable.size(); i++){
//				
//				if(Integer.parseInt(priceTable.get(i).strTime.split(":")[0])>=cHour){
//					System.out.println(priceTable.get(i).strTime);
//					System.out.println(priceTable.get(i).price);
//	
//				}
//				
//			}
//			
//		
//			bufferedReader.close();
//			
//		} catch (FileNotFoundException ex) {
//			
//			System.out.println("Unable to open file '" + fileName + "'");
//			
//		} catch (IOException ex) {
//			
//			System.out.println("Error reading file '" + fileName + "'");
//			
//		}

	}

}

class IndustrialRealTime{
	
	String strTime, endTime;
	double price;
	
	public IndustrialRealTime(String strTime, String endTime, double price){
		
		setStrTime(strTime);
		setEndTime(endTime);
		setPrice(price);
		
	}

	public String getStrTime() {
		return strTime;
	}

	public void setStrTime(String strTime) {
		this.strTime = strTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	
}


