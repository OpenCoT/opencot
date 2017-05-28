package com.github.opencot.io.protocols;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.net.URISyntaxException;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import com.github.opencot.data.DeviceData;
import com.github.opencot.io.Gateway;
import com.github.opencot.io.GatewayState;

public class MqttGateway implements Gateway {
	
	public GatewayState state = GatewayState.STATE_DISABLED;
	
	protected MQTT mqtt;
	protected BlockingConnection connection;
	protected HashMap<String,List<DeviceData>> subscriptions;
	
	public MqttGateway() {
		state = GatewayState.STATE_INVALID;
		subscriptions = new HashMap<>();
	}
	
	@Override
	public int Init()
	{
		mqtt = new MQTT();
    	try {
    		mqtt.setHost("localhost", 1883);
    		mqtt.setClientId("OpenCoT");
    		mqtt.setUserName("admin");
    		mqtt.setPassword("admin");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			state = GatewayState.STATE_ERROR;
			return -1;
		}
    	state = GatewayState.STATE_INACTIVE;
    	return 0;
	}

	@Override
	public int Start() {
    	// Connect to a broker(server)
    	BlockingConnection connection = mqtt.blockingConnection();
    	try {
			connection.connect();
			connection.publish("example", "Hello".getBytes(), QoS.AT_LEAST_ONCE, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	// Subscribe to topics
    	Topic[] topics = {
    			//new Topic("led/tick", QoS.AT_LEAST_ONCE),
    			new Topic("sensor/LightIntensity/x", QoS.AT_LEAST_ONCE),
    			// TODO foreach sub : subscriptions do topics.add( new Topic(addr) )
    			};
    	try {
			byte[] qoses = connection.subscribe(topics);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	// (Blocking api way:) poll for received messages
    	while(true)
    	{
    		Message message;
			try {
				message = connection.receive();
	    		byte[] payload = message.getPayload();
	    		String msg = new String(payload);
	    		System.out.printf("Received @ \"%s\": \"%s\"\n", message.getTopic(), msg );
	    		
	    		// TODO parse only correct topics
	    		//if(  ) {
	    			Integer pwm = (int) (Float.parseFloat(msg)/1000.0*255);
	    			connection.publish("led/g", pwm.toString().getBytes(), QoS.AT_LEAST_ONCE, false);
	    		//}
	    		
	    		message.ack();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
    	}
		return 0;
	}
	
	@Override
	public int Stop()
	{
		if( connection != null )
		{
	    	try {
				connection.disconnect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}
		}
		return 0;
	}
	
	public boolean addSubscription(String addr) {
		//mqttclient.
		return true;
	}
	protected boolean subscribe( String topic ) { // called while running
		if( state != GatewayState.STATE_ACTIVE )
			return false;
		//connection.subscribe();
		return false;
	}

	@Override
	public void addReceiver(DeviceData devdata) {
		String addr = devdata.getAddress();
		List<DeviceData> subs;
		
		if( subscriptions.containsKey(addr) ) {
			subs = subscriptions.get(addr);
		} else {
			subs = new LinkedList<>();
			subscriptions.put(addr, subs);
			subscribe( addr );
		}
		subs.add(devdata);
	}
	@Override
	public void sendData(DeviceData devdata) {
		if( state != GatewayState.STATE_ACTIVE )
			return; // TODO invalid state, error out?
		// TODO //
	}
}
