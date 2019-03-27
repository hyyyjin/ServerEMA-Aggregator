package com.mir.ems.coap.emap;

import org.eclipse.californium.core.CoapResource;

import com.mir.ems.coap.CoAPSummaryObserver;
import com.mir.ems.globalVar.global;


public class Version extends CoapResource {

	public Version(String name) {
		// TODO Auto-generated constructor stub
		super(name);


		//OpenADR
		add(new SessionSetup("EiRegisterParty"));
		add(new SessionSetup("EiReport"));
		add(new Opt("EiOpt"));

		add(new DemandResponseEvent("EiEvent"));
		add(new DemandResponseEvent("OadrPoll"));

		
		
		// EMAP 
		add(new SessionSetup("SessionSetup"));
		add(new Report("Report"));
		add(new Opt("Opt"));
		add(new DemandResponseEvent("Event"));
		add(new DemandResponseEvent("Poll"));

		
		//2018-12-14
		if(global.summaryReport){
			add(new CoAPSummaryObserver("Summary"));
		}
		
		add(new EventACK("EventACK"));
		add(new SummaryACK("SummaryACK"));
		
		
		
	}
}
