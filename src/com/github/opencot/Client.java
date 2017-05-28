package com.github.opencot;
import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;

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
public class Client{

    private WebSocketContainer container;
    
    public void onInit(){
           this.container = ContainerProvider.getWebSocketContainer();
    }
    
    public void testConnection(){
     
    }
}