package com.github.opencot.data;

import java.util.List;
import java.util.LinkedList;

public class DeviceData extends DataContainer {

	protected String address; // mqtt addr
	
	public DeviceData( DataExchanger parent, String name, DataType type, DataDirection direction, String addr) {
		super(parent,name, type, direction);
		address = addr;
	}

	public String getAddress() {
		return address;
	}
	
	@Override
	public void setStringValue( String newval ) {
		if (datatype == DataType.String) {
			stringval = newval;
			sendUpdates();
		}
		else
			throw new UnsupportedOperationException("Data is not string type");
	}
	@Override
	public void setValue( Number newval) {
		if (datatype == DataType.Value || datatype == DataType.Toggle) {
			numvalue = newval;
			//TODO if( datatype == DataType.Toggle ) toggled = newval>toggletreshold;
			sendUpdates();
		}
		else
			throw new UnsupportedOperationException("Data is not value type");
	}

	@Override
	public void invokeEvent() {
		if (datatype == DataType.Event) {
			eventCount++;
			sendUpdates();
            System.out.println("Device event: "+dataname);
		}
		else
			throw new UnsupportedOperationException("Data is not event type");
	}

	@Override
	public void sendUpdates() {
		for (DataContainer sub : subscribers) {
			switch (datatype) {
			case Event:
				sub.invokeEvent();
				break;
			case String:
				sub.setStringValue(stringval);
				break;
			case Toggle:
				sub.setValue(numvalue); // TODO
				break;
			case Value:
				sub.setValue(numvalue);
				break;
			}
		}
	}
}
