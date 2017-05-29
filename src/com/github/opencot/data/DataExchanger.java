package com.github.opencot.data;

import com.github.opencot.io.Gateway;

public interface DataExchanger {

	/**
	 * Adds data endpoint that can send or receive data
	 * @param newdata Name of data entry
	 */
	//public void addDataEndpoint(DataContainer newdata);
	/**
	 * Returns data container by the given name
	 * @param dataname Name of data entry
	 * @return data container if found, null otherwise
	 */
	public DataContainer getDataEndpoint( String dataname );
	
	public boolean isExternal();
	public Gateway getGateway();
	public void NotifyDataChange( String dataname );
}
