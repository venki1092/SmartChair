#include <Ethernet.h>
#include <PubSubClient.h>

byte mac[]    = {  0x98, 0x4F, 0xEE, 0x05, 0x4F, 0xC1 };
IPAddress ip(192, 168, 1, 124);
IPAddress server(192, 168, 1, 2);
IPAddress gateway(192, 168, 1, 1);
IPAddress subnet(255, 255, 255, 0);

//Feel free to use this code.
//Please be respectful by acknowledging the author in the code if you use or modify it.
//Author: Bruce Allen
//Date: 23/07/09

//Digital pin 3 for reading in the pulse width from the MaxSonar device.
//This variable is a constant because the pin will not change throughout execution of this code.
const int pwPin = 3; 
//variables needed to store values
long pulse, inches, cm;

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i=0;i<length;i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();
}
EthernetClient ethClient;
PubSubClient client(ethClient);

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("arduinoClient")) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      client.publish("outTopic","hello world");
      // ... and resubscribe
      client.subscribe("inTopic");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
} 
void setup() {

  //This opens up a serial connection to shoot the results back to the PC console
  Serial.begin(9600);
  Ethernet.begin(mac, ip);
  client.setServer(server, 2880);
  client.setCallback(callback);

  
  // Allow the hardware to sort itself out
  delay(1500);

}

void loop() {

  pinMode(pwPin, INPUT);
    //Used to read in the pulse that is being sent by the MaxSonar device.
  //Pulse Width representation with a scale factor of 147 uS per Inch.

  pulse = pulseIn(pwPin, HIGH);
  //147uS per inch
  inches = pulse/147;
  //change inches to centimetres
  cm = inches * 2.54;
  char messageToSend[50];
  sprintf(messageToSend,"{distance1:%d},",(int)cm);
 // if (!client.connected()) {
 //   reconnect();
 // }
  client.publish("/smartchair/data/gallileo",messageToSend);
  client.loop();
  //Serial.print(inches);
  //Serial.print("in, ");
  Serial.print(cm);
  //Serial.print("cm");
  //Serial.println();
  delay(3000);
}  
