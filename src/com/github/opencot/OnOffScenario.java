package com.github.opencot;

import java.util.ArrayList;

import com.github.opencot.data.DataContainer;
import com.github.opencot.io.Gateway;

public class OnOffScenario extends Scenario{

	public OnOffScenario(int uid, String name, String description) {
		this.uid = uid;
		this.name = name;
		descr = description;
		dataentries = new ArrayList<>();
		valid = true;
	}



}
