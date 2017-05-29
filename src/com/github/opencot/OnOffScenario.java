package com.github.opencot;

import java.util.ArrayList;

import com.github.opencot.data.DataContainer;
import com.github.opencot.data.DataDirection;
import com.github.opencot.data.DataType;
import com.github.opencot.io.Gateway;

public class OnOffScenario extends Scenario{

	public OnOffScenario(int uid, String name, String description) {
		this.uid = uid;
		this.name = name;
		descr = description;
		dataentries = new ArrayList<>();
		valid = true;
	}

	@Override
	public void NotifyDataChange(String dataname) {
		DataContainer changed = getDataEndpoint(dataname);
		System.out.println("Changed: "+changed.getName());
		if( changed.getName().equals("Data1_val") ) {
			if( changed.getDirection() == DataDirection.IN
					|| changed.getDirection() == DataDirection.INOUT ) {
				//if( changed.getType() != DataType.Toggle)
					//return;
				Number numval = changed.getValue();
				DataContainer outpoint = getDataEndpoint("Data2_str");
				if( numval.intValue() >0 )
					outpoint.setStringValue("ON");
				else
					outpoint.setStringValue("OFF");
			}
		}
	}
}
