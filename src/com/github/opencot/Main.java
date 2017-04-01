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

import com.github.opencot.data.protocols.MqttGateway;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	
    	MqttGateway mqtt = new MqttGateway();
    	mqtt.Init();
    	mqtt.Run();
    }
    
}
