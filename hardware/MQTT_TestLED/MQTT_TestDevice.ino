
#include <ESP8266WiFi.h>
#include <PubSubClient.h>

#define PIN_LED D8

const char* wifi_ssid     = "DJ PC";
const char* wifi_password = "androidap";

const uint16_t port = 1883;
const char* host = "192.168.137.1"; // ip or dns

const char* mqtt_user = "user";
const char* mqtt_password = "pass";

#define topic_r "led/r"
#define topic_g "led/g"
#define topic_b "led/b"
#define topic_tick "led/tick"

WiFiClient espClient;
PubSubClient mqttclient( espClient );

void setup()
{
    pinMode(PIN_LED, OUTPUT);
    digitalWrite(PIN_LED, HIGH); // shine until Wifi connection is established
    
    Serial.begin(115200);
    delay(1000);
    
    setupWifi(); // blocks until connected
    digitalWrite(PIN_LED, LOW);

    mqttclient.setServer(host, port);
    mqttclient.setCallback(callback);
}

void setupWifi()
{
    Serial.print("Wait for WiFi... ");
    Serial.println();

    WiFi.begin(wifi_ssid, wifi_password);
  
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }

    Serial.println("");
    Serial.println("WiFi connected");
    Serial.print("IP address: ");
    Serial.println(WiFi.localIP());

    delay(500);
}
void reconnect()
{
    // Loop until we're reconnected
    while (!mqttclient.connected()) {
        Serial.print("Attempting MQTT connection...");
        // If you do not want to use a username and password, use
        // if (client.connect("ESP8266Client")) {
        if (mqttclient.connect("ESP8266Client", mqtt_user, mqtt_password)) {
            Serial.println("connected");
            mqttclient.subscribe(topic_g);
        } else {
            Serial.print("failed, rc=");
            Serial.print(mqttclient.state());
            Serial.println(" try again in 5 seconds");
            // Wait 5 seconds before retrying
            delay(1000);
        }
    }
}

void callback(char* topic, byte* payload, unsigned int length)
{
    Serial.print("Message arrived [");
    Serial.print(topic);
    Serial.print("] ");
    for (int i=0;i<length;i++) {
        Serial.print((char)payload[i]);
    }
    char *cstring = (char *) payload;
    // TODO: parse only correct topics
    int pwm = atoi(cstring);
    if( pwm > 255 ) pwm = 255;
    Serial.println(pwm);
    analogWrite(PIN_LED, pwm);
}


void loop()
{
    if( !mqttclient.connected() ) {
        reconnect();
    }
    mqttclient.loop();
    
    unsigned long curtime = millis();
    if (curtime - lastMsg > 1000) {
        lastMsg = curtime;
    
        int randint = random(255);
        //Serial.print("Tick");
        mqttclient.publish(topic_tick, String(randint).c_str(), true);
    }
}

