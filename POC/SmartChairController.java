import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

public class SmartChairController {
	
	public static int pressure1 = 0;
	public static int pressure2 = 0;
	public static int pressure3 = 0;
	public static int pressure4 = 0;
	public static int distance1 = 0;
	public static int distance2 = 0;
	public static double angle = 0;
	public static int temperature = 0;
	public static void main(String[] args) {
		
		Random randomgenerator = new Random();
		while(true)
		{
			pressure1 = randomgenerator.nextInt(2);
			pressure2 = randomgenerator.nextInt(2);
			pressure3 = randomgenerator.nextInt(2);
			pressure4 = randomgenerator.nextInt(2);
			distance1 = randomgenerator.nextInt(2);
			distance2 = randomgenerator.nextInt(2);
			
			angle =(double) randomgenerator.nextInt(90);
			temperature = randomgenerator.nextInt(20);
			JSONObject dataToSend = new JSONObject();
			dataToSend.put("pressure1", pressure1) ;
			dataToSend.put("pressure2", pressure2) ;
			dataToSend.put("pressure3",pressure3) ;
			dataToSend.put("pressure4",pressure4);
			dataToSend.put("distance1",distance1) ;
			dataToSend.put("distance2",distance2);
			dataToSend.put("angle",angle);
			dataToSend.put("temperature", temperature);
			System.out.println("data to send" + dataToSend.toString());
			sendMessage("/smartchair/data",dataToSend.toString());
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void sendMessage(String topicName , String messageContent)
	{
		 	String topic        = topicName;
	        String content      = messageContent;
	        int qos             = 2;
	        String broker       = "tcp://10.250.33.173:2882";
	        String clientId     = "JavaSample";
	        MemoryPersistence persistence = new MemoryPersistence();

	        try {
	            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
	            MqttConnectOptions connOpts = new MqttConnectOptions();
	            connOpts.setCleanSession(true);
	            System.out.println("Connecting to broker: "+broker);
	            sampleClient.connect(connOpts);
	            System.out.println("Connected");
	            System.out.println("Publishing message: "+content);
	            MqttMessage message = new MqttMessage(content.getBytes());
	            message.setQos(qos);
	            
	            sampleClient.publish(topic, message);
	            System.out.println("Message published");
	            sampleClient.disconnect();
	            System.out.println("Disconnected");
	            //System.exit(0);
	        } catch(MqttException me) {
	            System.out.println("reason "+me.getReasonCode());
	            System.out.println("msg "+me.getMessage());
	            System.out.println("loc "+me.getLocalizedMessage());
	            System.out.println("cause "+me.getCause());
	            System.out.println("excep "+me);
	            me.printStackTrace();
	        }
	}


}
