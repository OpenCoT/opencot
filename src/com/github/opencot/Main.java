/**
*    Copyright (c) 2017 by Lukas Jonyla, Martynas Mitka, Saulius Juškevičius
*   
*    This file is part of OpenCoT.
*
*    OpenCoT is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Lesser General Public License as published by
*    the Free Software Foundation, version 3 of the License.
*
*    OpenCoT is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Lesser General Public License for more details.
*
*    You should have received a copy of the GNU Lesser General Public License
*    along with OpenIoT.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.github.opencot;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.opencot.data.DataDirection;
import com.github.opencot.data.DataType;
import com.github.opencot.data.DeviceData;
import com.github.opencot.io.protocols.MqttGateway;

public class Main {

	static List<Device> devices;
	static List<Scenario> scenarios;
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

    	MqttGateway mqtt = new MqttGateway();
    	mqtt.Init();
    	
    	devices = new LinkedList<>();
    	scenarios = new LinkedList<>();
    	
    	// TODO: Deserialize devices from storage
    	Device dev1 = new Device(1, "TestDev1", "Test1", mqtt);
    	Device dev2 = new Device(2, "TestDev2", "Test2", mqtt);
    	Device dev3 = new Device(3, "TestDev3", "Test3", mqtt);
    	dev1.addDataEndpoint(new DeviceData(dev1, "Data1_val", DataType.Value, DataDirection.IN, "test/1"));
    	dev1.addDataEndpoint(new DeviceData(dev1, "Data2_str", DataType.String, DataDirection.IN, "test/2"));
    	dev2.addDataEndpoint(new DeviceData(dev2, "Data3_tgl", DataType.Toggle, DataDirection.IN, "test/3"));
    	dev3.addDataEndpoint(new DeviceData(dev3, "Data4_event", DataType.Event, DataDirection.IN, "test/4"));
    	devices.add(dev1);
    	devices.add(dev2);
    	devices.add(dev3);
    	Scenario scen1 = new OnOffScenario(1, "TestScenario1", "TestScen1 desc");
    	scenarios.add(scen1);

    	mqtt.Start();
    	while(true)
    	try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	//mqtt.Stop();
    }
    
}
