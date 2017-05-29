package com.github.opencot.io.protocols;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.fusesource.mqtt.client.MQTT;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection ;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.Topic;
import org.fusesource.mqtt.client.Tracer;
import org.fusesource.mqtt.codec.MQTTFrame;
import org.fusesource.mqtt.client.Promise;
import org.fusesource.mqtt.client.QoS;

import com.github.opencot.data.DataContainer;
import com.github.opencot.data.DeviceData;
import com.github.opencot.io.Gateway;
import com.github.opencot.io.GatewayState;

public class MqttGateway implements Gateway {
	
	public GatewayState state = GatewayState.STATE_DISABLED;
	
	protected MQTT mqtt;
	protected CallbackConnection connection;
	protected HashMap<String,List<DeviceData>> subscriptions;
    final Promise<Buffer> result = new Promise<Buffer>();
	
	public MqttGateway() {
		state = GatewayState.STATE_INVALID;
		subscriptions = new HashMap<>();
		mqtt = new MQTT();
    	try {
    		mqtt.setHost("broker.mqttdashboard.com", 1883);
    		mqtt.setClientId("OpenCoT");
    		//mqtt.setUserName("admin");
    		//mqtt.setPassword("admin");
            mqtt.setTracer(new Tracer(){
                @Override
                public void onReceive(MQTTFrame frame) {
                    //System.out.println("MQTT recv: "+frame);
                }

                @Override
                public void onSend(MQTTFrame frame) {
                    System.out.println("MQTT send: "+frame);
                }

                @Override
                public void debug(String message, Object... args) {
                    //System.out.println(String.format("MQTT debug: "+message, args));
                }
            });
		} catch (URISyntaxException e) {
			e.printStackTrace();
			state = GatewayState.STATE_ERROR;
		}
	}
	
	@Override
	public int Init()
	{
		connection = mqtt.callbackConnection();

        connection.listener(new Listener() {
            public void onConnected() {
                System.out.println("MQTT connected");
            }

            public void onDisconnected() {
                System.out.println("MQTT disconnected");
            }

            public void onPublish(UTF8Buffer topic, Buffer payload, Runnable onComplete) {
                System.out.printf("MQTT onpublish @\"%s\" \"%s\"\n",topic,payload.utf8().toString());
                if( subscriptions.containsKey(topic.toString()) ) {
                	List<DeviceData> subs = subscriptions.get(topic.toString());
					Number numvalue = null;
                	for (DataContainer dat : subs) {
                		
						switch (dat.getType()) {
						case String:
							dat.setStringValue(payload.toString());
							break;
						case Value:
						case Toggle:
							if( numvalue == null ) {
								String msgstr = null;
								try {
									msgstr = new String(payload.toByteArray(), "UTF-8");
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								try {
									numvalue = (Float.parseFloat(msgstr));
								} catch (NumberFormatException nfe) {
									// Not a float
					                System.out.printf("MQTT: Invalid value received for \"%s\": \"%s\"\n",
					                		topic.toString(), msgstr);
					                continue;
								}
							}
	                        System.out.println("aaa"+dat.getName());
							dat.setValue(numvalue);
							break;
						case Event:
							dat.invokeEvent();
							break;
						}
					}
                }
                onComplete.run();
            }

            public void onFailure(Throwable value) {
                System.out.println("MQTT failure: "+value);
                connection.disconnect(null);
            }
        });
		
    	state = GatewayState.STATE_INACTIVE;
    	return 0;
	}

	@Override
	public int Start() {
    	// Connect to a broker(server)
    	state = GatewayState.STATE_INACTIVE;
        connection.connect(new Callback<Void>() {
            public void onSuccess(Void v) {
                System.out.println("Connect success");
                Topic[] topics = new Topic[subscriptions.keySet().size()];
                int i=0;
                for (String addr : subscriptions.keySet()) {
					topics[i] = new Topic(Buffer.utf8(addr), QoS.AT_LEAST_ONCE);
                	i++;
				}
                connection.subscribe(topics, new Callback<byte[]>() {
                    public void onSuccess(byte[] value) {
            			state = GatewayState.STATE_ACTIVE;
                        System.out.println("Subbed");
                    }
                    public void onFailure(Throwable value) {
            			state = GatewayState.STATE_ERROR;
                        System.out.println("Sub failed");
                        result.onFailure(value);
                        connection.disconnect(null);
                    }
                });

            }

            public void onFailure(Throwable value) {
    			state = GatewayState.STATE_ERROR;
                System.out.println("Connect failure: "+value);
                result.onFailure(value);
            }
        });
        
        // FIXME make non-blocking
        while(state == GatewayState.STATE_INACTIVE);
        return (state == GatewayState.STATE_ACTIVE) ? 0 : 1;
	}
	
	@Override
	public int Stop()
	{
		if( connection != null )
		{
			connection.disconnect(null);
		}
		return 0;
	}

	protected boolean subscribe( String topic ) { // called during runtime, not setup
		if( state != GatewayState.STATE_ACTIVE )
			return false;
		//connection.subscribe(); // TODO
		return false;
	}

	@Override
	public void addReceiver(DeviceData devdata) {
		String addr = devdata.getAddress();
		List<DeviceData> subs;
        System.out.println("addReceiver: "+addr);
		
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
		byte[] payload = null;
		switch( devdata.getType() ) {
		case String:
			payload = devdata.getStringValue().getBytes();
			break;
		case Value:
			payload = devdata.getValue().toString().getBytes();
			break;
		case Toggle:
			payload = devdata.getValue().toString().getBytes(); // TODO?
			break;
		case Event:
			payload = "".getBytes(); // TODO non-empty messages
			break;
		}
		connection.publish(devdata.getAddress(), payload, QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
            public void onSuccess(Void v) {
                // the publish operation completed successfully.
              }
              public void onFailure(Throwable value) {
                  //connection.disconnect(null); // publish failed.
              }
          });
	}
}
