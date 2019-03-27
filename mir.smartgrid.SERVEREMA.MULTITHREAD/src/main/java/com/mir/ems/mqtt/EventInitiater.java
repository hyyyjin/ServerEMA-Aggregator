package com.mir.ems.mqtt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EventInitiater {
	private List<PushEventListener> listeners = new ArrayList<PushEventListener>();
	private ConcurrentHashMap<String, String> dup = new ConcurrentHashMap<>();
	
	public void addListener(PushEventListener toAdd) {

		// if (listeners.contains(toAdd)) {
		// listeners.remove(toAdd);
		// listeners.add(toAdd);
		//
		// } else {
//		listeners.add(toAdd);
		// }
		
		EventResponder input = (EventResponder) toAdd;
		String inputID = input.getEmaID();
	
		
		if(!dup.containsKey(inputID)) {
			dup.put(inputID, inputID);
			listeners.add(toAdd);
		}
		
		
//		dup.put(inputID, inputID);
//		
//		Iterator<String> it = dup.keySet().iterator();
//		
//		System.out.println(dup.keySet().toString());
//		
//		while(it.hasNext()){
//			String key = it.next();
//			
//			if(!dup.containsKey(inputID)) {
//				System.err.println("추가");
//				listeners.add(toAdd);
//			}else{
//				System.out.println("뭐여");
//			}
//			
//		}
//		
		
		
//		listeners.put(inputID, inputID);
		

		
//		
//		for(int i=0; i<2000; i++){
//			EventResponder exist = (EventResponder) listeners.get(i);
//			
//			System.out.println(exist.getEmaID());
//			
//			if(inputID.equals(exist.getEmaID())){
//				break;
////				System.out.println("지움");
////				listeners.remove(i);
////				listeners.add(toAdd);
//			}else{
//				listeners.add(toAdd);
//
//			}
////			else{
////				System.out.println("추가");
////				listeners.add(toAdd);
////			}
//		}
//		
//		Iterator<PushEventListener> it = listeners.iterator();
//		
//		while(it.hasNext()){
//			
//			EventResponder exist = (EventResponder) it.next();
//			
//			System.out.println(inputID);
//			
//			if(inputID.equals(exist.getEmaID())){
//				System.out.println("지움");
//				listeners.remove(it);
//				listeners.add(toAdd);
//			}
//			
//		}
		
		
//		
//		System.out.println(listeners);
		
		

	}

	public void eventOccur(String emaID, int type, int strday, int strtime, int endday, int endtime, double value) {

		// Notify everybody that may be interested.

		for (PushEventListener hl : listeners) {
			
//			System.out.println("호출됨");
			hl.eventNotification(emaID, type, strday, strtime, endday, endtime, value);

		}
		
//		Iterator<String> it  = listeners.keySet().iterator();
//		
//		while(it.hasNext()){
//			
//			String
//			hl.eventNotification(emaID, type, strday, strtime, endday, endtime, value);
//
//		}

	}
}
