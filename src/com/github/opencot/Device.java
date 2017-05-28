package com.github.opencot;

import java.util.ArrayList;
import java.util.List;

public class Device {
	protected boolean valid;
	protected int uid;
	protected String name;
	protected String descr;
	protected List<DeviceData> dataentries;

	public Device(int uid, String name, String description) {
		this.uid = uid;
		this.name = name;
		descr = description;
		dataentries = new ArrayList<>();
		valid = true;
	}

	public int getID() {
		return uid;
	}

	public boolean isValid() {
		return valid;
	}
	public void setInvalid() {
		valid = false;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return name;
	}
	public void setDescription(String description) {
		descr = description;
	}
	
	public void addData(DeviceData newdata) {
		dataentries.add(newdata);
	}
	/**
	 * Returns data object by the entry name
	 * @param dataname Name of data entry
	 * @return data object if found, null otherwise
	 */
	public DeviceData getData( String dataname ) {
		for (DeviceData dd : dataentries) {
			if (dd.getName().equalsIgnoreCase(dataname))
				return dd;
		}
		return null;
	}
}
