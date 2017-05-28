package com.github.opencot.data.protocols;

import java.net.URISyntaxException;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

public class MqttGateway implements Gateway {
	
	public GatewayState state = GatewayState.STATE_DISABLED;
	
	protected MQTT client;
	protected BlockingConnection connection;
	
	public MqttGateway() {
		state = GatewayState.STATE_INVALID;
	}
	
	@Override
	public int Init()
	{
		client = new MQTT();
    	try {
    		client.setHost("localhost", 1883);
    		client.setClientId("Opencot");
    		client.setUserName("admin");
    		client.setPassword("admin");
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
	public int Run() {
    	// Connect to a broker(server)
    	BlockingConnection connection = client.blockingConnection();
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
}
