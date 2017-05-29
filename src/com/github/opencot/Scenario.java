package com.github.opencot;

import java.util.List;

import com.github.opencot.data.DataContainer;
import com.github.opencot.data.DataExchanger;
import com.github.opencot.data.DeviceData;
import com.github.opencot.io.Gateway;

public abstract class Scenario implements DataExchanger {
	//Device[] devices;
	protected boolean valid;
	protected int uid;
	protected String name;
	protected String descr;
	protected List<DeviceData> dataentries;

	@Override
	public boolean isExternal() {
		return false;
	}
	@Override
	public Gateway getGateway() {
		throw new UnsupportedOperationException("Scenarios are only internal");
	}
	
	@Override
	public DataContainer getDataEndpoint( String dataname ) {
		for (DataContainer dd : dataentries) {
			if (dd.getName().equalsIgnoreCase(dataname))
				return dd;
		}
		return null;
	}
}
