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
import web.model.Device;
import web.websocket.DeviceSessionHandler;
import java.net.URISyntaxException;
import java.net.URI;
import com.github.opencot.io.protocols.MqttGateway;
import java.io.StringReader;
import java.net.URI;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.*;
@ClientEndpoint
public class Main {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws URISyntaxException {
    	
        
        List<Device> devices = new LinkedList<>();
    	// TODO: Deserialize devices from storage
    	Device dev1 = new Device(1, "TestDev1", "Test1");
    	Device dev2 = new Device(2, "TestDev2", "Test2");
    	Device dev3 = new Device(3, "TestDev3", "Test3");
    	dev1.addData(new DeviceData("Data1_val", "/test/1", DevDataType.Value));
    	dev1.addData(new DeviceData("Data2_str", "/test/2", DevDataType.String));
    	dev2.addData(new DeviceData("Data3_tgl", "/test/3", DevDataType.Toggle));
    	dev3.addData(new DeviceData("Data4_event", "/test/4", DevDataType.Event));
      
    	devices.add(dev1);
    	devices.add(dev2);
    	devices.add(dev3);
        
        
        
        
         WebSocketContainer container=null;//
     Session session=null;
  try{
   //Tyrus is plugged via ServiceLoader API. See notes above
   container = ContainerProvider.getWebSocketContainer(); 
//WS1 is the context-root of my web.app 
//ratesrv is the  path given in the ServerEndPoint annotation on server implementation
    session=container.connectToServer(Main.class, URI.create("ws://localhost:8080/WebHome/actions")); 


   } catch (Exception e) {
   e.printStackTrace();
  }
  finally{
   if(session!=null){
    try {
 session.close();
    } catch (Exception e) {     
     e.printStackTrace();
    }
   }         
  } 
                
    	
    	
    	//MqttGateway mqtt = new MqttGateway();
    	//mqtt.Init();
    	//mqtt.Run();
    
                 
}
}