import java.util.Random;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

public class SmartChairController implements MqttCallback {
	
	public Boolean spark = false;
	public Boolean gallileo = false;
	public Boolean androidAngle = false;
	public int pressure1 = 0;
	public int pressure2 = 0;
	public int pressure3 = 0;
	public int pressure4 = 0;
	public int distance1 = 0;
	public int distance2 = 0;
	public double angle = 0;
	public int temperature = 0;
	String MosquittoBrokerUrl = "tcp://54.186.96.23:2882";
	String MongoDaemonIp = "54.186.96.23";
	int MongoPort = 27018;
	MqttClient client;
	public static void main(String[] args) {
		new SmartChairController().startSmartChairController();
	}
	public void startSmartChairController()
	{
		Random randomgenerator = new Random();
		try{
			//client = new MqttClient("tcp://54.187.143.223:2882", "Sending");
			client = new MqttClient(MosquittoBrokerUrl, "Sendingdata");
			client.connect();
			client.setCallback(this);	
			String[] topicFilter = {"/smartchair/data/spark","/smartchair/data/gallileo","/smartchair/data/angle"};
			client.subscribe(topicFilter);
			while(true)
			{
				if(!spark){
					pressure1 = randomgenerator.nextInt(2);
					pressure2 = randomgenerator.nextInt(2);
					pressure3 = randomgenerator.nextInt(2);
					pressure4 = randomgenerator.nextInt(2);
					temperature = randomgenerator.nextInt(20);
				}
				if(!gallileo){
					distance1 = randomgenerator.nextInt(2);
					distance2 = randomgenerator.nextInt(2);	
				}
				if(!androidAngle){
					angle =(double) randomgenerator.nextInt(90);
				}
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
					Thread.sleep(4000); // should be read from mongo !!!!
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
	@Override
	public void connectionLost(Throwable cause) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void messageArrived(String topic, MqttMessage message)
	        throws Exception {
		try{
			String s[] = topic.split("/");
			if(s[3].equals("spark")){
				JSONObject jsonDoc = new JSONObject(message.toString());
				int pressure1 = jsonDoc.getInt("pressure1");
				int pressure2 = jsonDoc.getInt("pressure2");
				int pressure3 = jsonDoc.getInt("pressure3");
				int pressure4 = jsonDoc.getInt("pressure4");
				int temperature = jsonDoc.getInt("temperature");
				this.pressure1 = pressure1;
				this.pressure2 = pressure2;
				this.pressure3 = pressure3;
				this.pressure4 = pressure4;
				this.temperature = temperature;
			}
			else if(s[3].equals("gallileo")){
				JSONObject jsonDoc = new JSONObject(message.toString());
				int distance1 = jsonDoc.getInt("distance1");
				int distance2 = jsonDoc.getInt("distance2");
				this.distance1 = distance1;
				this.distance2 = distance2;
			}
			else if(s[3].equals("angle")){
				this.angle = angle;
			}
			
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
		//todo update mongo
	}
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	    // TODO Auto-generated method stub
		
	}
	public void sendMessage(String topicName , String messageContent)
	{
		 	String topic        = topicName;
	        String content      = messageContent;
	        int qos             = 2;
	        String clientId     = "smartchaircontroller";
	        MemoryPersistence persistence = new MemoryPersistence();
	        try {
	            MqttClient sampleClient = new MqttClient(MosquittoBrokerUrl, clientId, persistence);
	            MqttConnectOptions connOpts = new MqttConnectOptions();
	            connOpts.setCleanSession(true);
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
