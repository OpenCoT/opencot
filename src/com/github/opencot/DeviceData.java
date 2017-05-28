package com.github.opencot;

public class DeviceData {
	private String dataname;
	private String address;
	private DevDataType datatype;
	private String stringval;
	private Number value;
	//private Number toggletreshold;
	private int eventCount;
	private DevDataDir datadir;
	
	public DeviceData( String name, String addr, DevDataType type, DevDataDir direction) {
		dataname = name;
		address = addr;
		datatype = type;
		datadir = direction;
		eventCount = 0;
		value = 0;
		//stringval = "";
	}
	
	public String getName() {
		return dataname;
	}
	public String getAddress() {
		return address;
	}
	public DevDataType getType() {
		return datatype;
	}
	public DevDataDir getDirection() {
		return datadir;
	}
	
	public String getStringValue() {
		if (datatype == DevDataType.String)
			return stringval;
		else
			throw new UnsupportedOperationException("Data is not string type");
	}
	public void setStringValue( String newval ) {
		if (datatype == DevDataType.String)
			stringval = newval;
		else
			throw new UnsupportedOperationException("Data is not string type");
	}
	
	public Number getValue() {
		if (datatype == DevDataType.Value || datatype == DevDataType.Toggle)
			return value;
		else
			throw new UnsupportedOperationException("Data is not value type");
	}
	public void setValue( Number newval) {
		if (datatype == DevDataType.Value || datatype == DevDataType.Toggle)
			value = newval;
		else
			throw new UnsupportedOperationException("Data is not value type");
	}

	public boolean checkEvent() {
		if (datatype == DevDataType.Event)
			return eventCount > 0;
		else
			throw new UnsupportedOperationException("Data is not event type");
	}
	public void invokeEvent() {
		if (datatype == DevDataType.Event)
			eventCount++;
		else
			throw new UnsupportedOperationException("Data is not event type");
	}
	public void eventHandled() {
		if (datatype == DevDataType.Event)
			if( eventCount > 0)
				eventCount--;
		else
			throw new UnsupportedOperationException("Data is not event type");
	}
}
