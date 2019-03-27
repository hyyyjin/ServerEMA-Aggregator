package com.mir.smartgrid.simulator.profile.emap;

import java.util.Arrays;
import java.util.List;

public class TopologyInformation {
	private int emaCNT;
	
	private List<Topology> topology;
		
	public TopologyInformation() {

	}

	public TopologyInformation(int emaCNT, List<Topology> topology) {
		this.emaCNT = emaCNT;
		this.topology = topology;
	}


	public int getEmaCNT() {
		return emaCNT;
	}

	public void setEmaCNT(int emaCNT) {
		this.emaCNT = emaCNT;
	}

	public List<Topology> getTopology() {
		return topology;
	}

	public void setTopology(Topology[] topology) {
		this.topology = Arrays.asList(topology);
	}


	@Override
	public String toString() {
		return "{\"emaCNT\":" + emaCNT + ","
				+ "\"topology\":" + topology.toString() + ""
				+ "}";
	}
	
	

}
