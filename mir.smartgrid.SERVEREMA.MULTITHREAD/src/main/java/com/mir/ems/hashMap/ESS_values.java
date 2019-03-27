package com.mir.ems.hashMap;

public class ESS_values {
	public int protocol, storage_id, mode, state,  capacity;
	public double power, soc, changedEnergy, volt, hz;
	public String name, gateway_id;

	public ESS_values(int protocol, int gateway_id, int storage_id, String name, double power, int mode, int state,
			double changedEnergy, int capacity, double soc, double volt, double hz) {
		this.protocol= protocol;
		this.gateway_id = "Gateway"+gateway_id;
		this.storage_id = storage_id;
		this.name = name;
		this.power = power;
		this.mode = mode; //대기중 0, 충전중1, 사용중2
		this.state = state; //
		this.changedEnergy = changedEnergy;
		this.capacity = capacity;
		this.soc = soc;
		this.volt = volt;
		this.hz = hz;

	}

	public String DevName() {
		return name;
	}

	@Override
	public String toString() {
		return storage_id + "/" + mode + "/" + state + "/" + changedEnergy + "/" + capacity + "/" + power + "/" + soc
				+ "/" + name + "/" + gateway_id;
	}
}
