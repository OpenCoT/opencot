package com.github.opencot;

import java.util.ArrayList;
import java.util.List;

import com.github.opencot.data.DataContainer;
import com.github.opencot.data.DataDirection;
import com.github.opencot.data.DataExchanger;
import com.github.opencot.data.DeviceData;
import com.github.opencot.io.Gateway;

/**
 * Class representing physical or logical device that is
 * connected to the outside world.
 */
public class Device implements DataExchanger {
	private Gateway gateway;
	protected boolean valid;
	protected int uid;
	protected String name;
	protected String descr;
	protected List<DeviceData> dataentries;

	public Device(int uid, String name, String description, Gateway gateway) {
		this.gateway = gateway;
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
	
	public void addDataEndpoint(DeviceData dataendpoint) {
		dataentries.add(dataendpoint);
		// Link it to the outside world
		if( dataendpoint.getDirection() == DataDirection.IN || dataendpoint.getDirection() == DataDirection.INOUT ) {
			gateway.addReceiver(dataendpoint);
		}
	}
	@Override
	public DataContainer getDataEndpoint( String dataname ) {
		for (DataContainer dd : dataentries) {
			if (dd.getName().equalsIgnoreCase(dataname))
				return dd;
		}
		return null;
	}

	@Override
	public boolean isExternal() {
		return true;
	}
	@Override
	public Gateway getGateway() {
		return gateway;
	}

	@Override
	public void NotifyDataChange(String dataname) {
		//DataContainer changed = getDataEndpoint(dataname);
		//changed.sendUpdates();
	}
}
