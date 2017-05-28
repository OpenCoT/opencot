package com.github.opencot;
import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;

import java.util.LinkedList;
import java.util.List;
import web.model.Device;
import web.websocket.DeviceSessionHandler;
import java.net.URISyntaxException;
import java.net.URI;
import com.github.opencot.io.protocols.MqttGateway;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.*;


@ClientEndpoint
public class Client{
    Session session=null;
    public Client() throws URISyntaxException, DeploymentException, IOException{
        URI uri = new URI("ws://localhost:8080/WebHome/actions");
        ContainerProvider.getWebSocketContainer().connectToServer(this, uri);
    }
    
    @OnOpen
    public void processOpen(Session session){
        this.session=session;
    }
    @OnMessage
    public void processMessage(String message){
        System.out.println(Json.createReader(new StringReader(message)).readObject().getString("message"));
    }
            
            
    
   
}