import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class SmartChairComputeEngine implements MqttCallback {
	MqttClient client;
	Calculations calculations;
	int harmfulCounter = 0;
	String MosquittoBrokerUrl = "tcp://54.186.96.23:2882";
	String MongoDaemonIp = "54.186.96.23";
	int MongoPort = 27018;
	public static void main(String[] args) {
			
			new SmartChairComputeEngine().doDemo();
			//db.collection.find().limit(1).sort({$natural:-1})
			//new SmartChairComputeEngine().insertDataToMongoDB();
			
	}
	public void insertDataToMongoDB(int pressure1, int pressure2, int pressure3 , int pressure4, int temperature , int angle, int isHarmful, String position ,String suggestion)
	{
		//MongoClient mongoClient = new MongoClient("54.191.239.166",27018);
		MongoClient mongoClient = new MongoClient(MongoDaemonIp,MongoPort);
		MongoDatabase db = mongoClient.getDatabase("smartchairdb");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date time = new Date();

		String currentDate = dateFormat.format(date); //2014/08/06 15:59:48
		String currentTime = timeFormat.format(time);
		Document d = new Document()
				.append("pressure1", pressure1)
                .append("pressure2", pressure2)
                .append("pressure3", pressure3)
                .append("pressure4", pressure4)
                .append("temperature", temperature)
                .append("angle", angle)
                .append("isHarmful", isHarmful)
                .append("suggestion", suggestion)
                .append("position", position)
                .append("time",currentTime );
		Date now = new Date();

		BasicDBObject timeNow = new BasicDBObject("date", time);
		
		d.putAll(timeNow);
		db.getCollection("smartchairdataset").insertOne(d);
		mongoClient.close();
	}
	public void insertNotificationToMongoDB(String Position, String Suggestion)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date time = new Date();

		String currentDate = dateFormat.format(date); //2014/08/06 15:59:48
		String currentTime = timeFormat.format(time);
		
		MongoClient mongoClient = new MongoClient(MongoDaemonIp,MongoPort);
		MongoDatabase db = mongoClient.getDatabase("smartchairdb");
		
		Document d = new Document()
				.append("Position", Position)
                .append("Suggestion", Suggestion)	
                .append("Date", currentDate)
                .append("Time", currentTime);
			
		Date now = new Date();

		BasicDBObject timeNow = new BasicDBObject("date", time);
		d.putAll(timeNow);

		db.getCollection("smartnotification").insertOne(d);
		mongoClient.close();
		
	}	
	public void doDemo() {
	    try {
	    	calculations = new Calculations();
	        client = new MqttClient(MosquittoBrokerUrl, "Sending");
	        client.connect();
	        client.setCallback(this);	
	        client.subscribe("/smartchair/data");
	        System.in.read();
	        client.disconnect();
	        client.close();
	    } catch (MqttException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void connectionLost(Throwable cause) {
	    // TODO Auto-generated method stub

	}
	public void sendMessage(String topicName , String messageContent)
	{
		 	String topic        = topicName;
	        String content      = messageContent;
	        int qos             = 2;
	        String clientId     = "smartchaircomputeengine";
	        MemoryPersistence persistence = new MemoryPersistence();
	        try {
	            MqttClient sampleClient = new MqttClient(MosquittoBrokerUrl, clientId, persistence);
	            MqttConnectOptions connOpts = new MqttConnectOptions();
	            connOpts.setCleanSession(true);
	            System.out.println("Connecting to broker: "+MosquittoBrokerUrl);
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
	@Override
	public void messageArrived(String topic, MqttMessage message)
	        throws Exception {	
		System.out.println(topic);
		System.out.println(message);   
		try{
		JSONObject obj = new JSONObject(message.toString());
		Integer pressure1 = obj.getInt("pressure1");
		Integer pressure2 = obj.getInt("pressure2");
		Integer pressure3 = obj.getInt("pressure3");
		Integer pressure4 = obj.getInt("pressure4");
		Integer distance1 = obj.getInt("distance1");
		Integer distance2 = obj.getInt("distance2");
		Integer angle = obj.getInt("angle");
		Integer temperature = obj.getInt("temperature");
		System.out.println(pressure1 + " " + pressure2 + " " + pressure3 + " " + pressure4 + " "+ distance1 + " " + distance2 + " "+ angle +" " + temperature );
		//implment business logic 
		System.out.println("[1]");
		if(pressure1 != 0 || pressure2 != 0 || pressure3 != 0 || pressure4 != 0)
		{
			Positions p = calculations.evaluate(pressure1, pressure2, pressure3, pressure4, distance1, distance2, angle);
			System.out.println("[2]");
			insertDataToMongoDB(pressure1, pressure2, pressure3, pressure4, temperature, angle, (p.isHarmful()) ? 1 : 0, p.getPosition(), p.getSuggestion());
			if(p.isHarmful())
				harmfulCounter++;
			else
				harmfulCounter = 0;
			
			if(harmfulCounter == 1)
			{
				JSONObject dataToSend = new JSONObject();
				dataToSend.put("isHarmful", p.isHarmful());
				dataToSend.put("position", p.getPosition());
				dataToSend.put("suggestion",p.getSuggestion());
				insertNotificationToMongoDB(p.getPosition(),p.getSuggestion());
				sendMessage("/smartchair/alert",dataToSend.toString());
			}
		}
		else
			harmfulCounter = 0;
		System.out.println("[3]");
		System.out.println(obj.toString());
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	    // TODO Auto-generated method stub

	}
}
