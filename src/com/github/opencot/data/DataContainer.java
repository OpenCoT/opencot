package com.github.opencot.data;

import java.util.List;
import java.util.LinkedList;

public class DataContainer {
	protected DataExchanger parent;
	protected String dataname;
	protected DataType datatype;
	protected String stringval;
	protected Number numvalue;
	//protected Number toggletreshold;
	//protected boolean toggled;
	protected int eventCount;
	protected DataDirection datadir;
	protected List<DataContainer> subscribers;

	public DataContainer( DataExchanger parent, String name, DataType type, DataDirection direction) {
		dataname = name;
		datatype = type;
		datadir = direction;
		eventCount = 0;
		numvalue = 0;
		//stringval = "";
		subscribers = new LinkedList<>();
	}

	public DataExchanger getparent() {
		return parent;
	}

	public String getName() {
		return dataname;
	}
	public DataType getType() {
		return datatype;
	}
	public DataDirection getDirection() {
		return datadir;
	}
	
	public String getStringValue() {
		if (datatype == DataType.String)
			return stringval;
		else
			throw new UnsupportedOperationException("Data is not string type");
	}
	public void setStringValue( String newval ) {
		if (datatype == DataType.String) {
			stringval = newval;
			sendUpdates();
		}
		else
			throw new UnsupportedOperationException("Data is not string type");
	}
	
	public Number getValue() {
		if (datatype == DataType.Value || datatype == DataType.Toggle)
			return numvalue;
		else
			throw new UnsupportedOperationException("Data is not value type");
	}
	public void setValue( Number newval) {
		if (datatype == DataType.Value || datatype == DataType.Toggle) {
			numvalue = newval;
			//TODO if( datatype == DataType.Toggle ) toggled = newval>toggletreshold;
			sendUpdates();
		}
		else
			throw new UnsupportedOperationException("Data is not value type");
	}

	public boolean checkEvent() {
		if (datatype == DataType.Event)
			return eventCount > 0;
		else
			throw new UnsupportedOperationException("Data is not event type");
	}
	public void invokeEvent() {
		if (datatype == DataType.Event) {
			eventCount++;
			sendUpdates();
		}
		else
			throw new UnsupportedOperationException("Data is not event type");
	}
	public void eventHandled() {
		if (datatype == DataType.Event)
			if( eventCount > 0)
				eventCount--;
		else
			throw new UnsupportedOperationException("Data is not event type");
	}
	
	public boolean subscribe(DataContainer client) {
		if( client == null )
			return false;
		if( client.datadir == DataDirection.OUT )
			throw new UnsupportedOperationException("Data directions not compatible");
		if( subscribers.contains(client) ) // Already subscribed
			return false;
		DataType subdatatype = client.getType();
		if( subdatatype != datatype ) {
			// Type mismatch exception: number<->toggle
			if( !((subdatatype==DataType.Toggle || subdatatype==DataType.Value) &&
					 (datatype==DataType.Toggle || datatype==DataType.Value)) )
				throw new UnsupportedOperationException("Data types not compatible");
		}
		subscribers.add(client);
		return true;
	}
	public boolean unsubscribe(DataContainer client) {
		if( client == null )
			return false;
		subscribers.remove(client);
		return true;
	}
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
