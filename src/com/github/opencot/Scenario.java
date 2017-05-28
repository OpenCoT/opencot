package com.github.opencot;

import com.github.opencot.data.DataContainer;
import com.github.opencot.data.DataExchanger;
import com.github.opencot.io.Gateway;

public class Scenario implements DataExchanger {
	Device[] devices;

	@Override
	public DataContainer getDataEndpoint(String dataname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isExternal() {
		return false;
	}
	@Override
	public Gateway getGateway() {
		throw new UnsupportedOperationException("Scenarios are only internal");
	}
}
