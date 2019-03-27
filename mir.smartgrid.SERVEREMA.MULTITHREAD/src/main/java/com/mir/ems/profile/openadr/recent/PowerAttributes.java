package com.mir.ems.profile.openadr.recent;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class PowerAttributes {

	private double hertz, voltage, ac;
	ArrayList<String> powerAttributesParam = new ArrayList<String>();

	public double getHertz() {
		return hertz;
	}

	public void setHertz(double hertz) {
		this.hertz = hertz;
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

	public double getAc() {
		return ac;
	}

	public void setAc(double ac) {
		this.ac = ac;
	}

	@Override
	public String toString() {

		JSONObject json = new JSONObject();

		try {
			json.put("hertz", getHertz());
			json.put("voltage", getVoltage());
			json.put("ac", getAc());

			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "wrong";
		}

	}

	public PowerAttributes addPowerAttributesParams(double hertz, double voltage, double ac) {

		setAc(ac);
		setHertz(hertz);
		setVoltage(voltage);

		this.powerAttributesParam.add(toString());

		return this;
	}

	public String getPowerAttributesParams() {

		return this.powerAttributesParam.toString();
	}

}
