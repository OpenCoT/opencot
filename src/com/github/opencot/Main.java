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

//import com.github.opencot.io.protocols.MqttGateway;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	
    	List<Device> devices = new LinkedList<>();
    	// TODO: Deserialize devices from storage
    	Device dev1 = new Device(1, "TestDev1", "Test1");
    	Device dev2 = new Device(2, "TestDev2", "Test2");
    	Device dev3 = new Device(3, "TestDev3", "Test3");
    	dev1.addData(new DeviceData("Data1_val", "/test/1", DevDataType.Value, DevDataDir.IN));
    	dev1.addData(new DeviceData("Data2_str", "/test/2", DevDataType.String, DevDataDir.IN));
    	dev2.addData(new DeviceData("Data3_tgl", "/test/3", DevDataType.Toggle, DevDataDir.IN));
    	dev3.addData(new DeviceData("Data4_event", "/test/4", DevDataType.Event, DevDataDir.IN));
    	devices.add(dev1);
    	devices.add(dev2);
    	devices.add(dev3);
    	
    	//MqttGateway mqtt = new MqttGateway();
    	//mqtt.Init();
    	//mqtt.Run();
    }
    
}
