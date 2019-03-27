package com.mir.ems.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.mir.ems.globalVar.global;

public class testFileIO {

	public testFileIO() {

		FileReader fileReader;
		try {
			fileReader = new FileReader("EVENTID.txt");

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String type = null;

			while ((type = bufferedReader.readLine()) != null) {
				
				int seq = Integer.parseInt(type.split("=>")[0]);
				String eventID = type.split("/")[1];
				
				global.eventID.put(seq, eventID);
				
			}
			
			
			Iterator<Integer> it = global.eventID.keySet().iterator();
			
			while(it.hasNext()){
				
				int key = it.next();
				
				System.out.println("SEQ\t"+key);
				System.out.println("ID\t"+global.eventID.get(key).toString());
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args){
		
		new testFileIO();
	}

}
