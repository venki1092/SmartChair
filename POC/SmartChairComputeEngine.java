import java.io.IOException;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class SmartChairComputeEngine implements MqttCallback {
	MqttClient client;
	public static void main(String[] args) {
		new SmartChairComputeEngine().doDemo();
		//new SmartChairComputeEngine().insertDataToMongoDB();
	}
	public void insertDataToMongoDB()
	{
		MongoClient mongoClient = new MongoClient("10.250.33.173");
		MongoDatabase db = mongoClient.getDatabase("smartchairdb");
		db.getCollection("smartchairdataset").insertOne( new Document()
                        .append("pressure1", "1")
                        .append("pressure2", "1")
                        .append("pressure3", "1")
                        .append("pressure4", "1")
                        .append("temperature", "1")
                        .append("angle", "1")
                        .append("condition", "1")
                        .append("date", "1")
                        .append("time", "1"));
		mongoClient.close();
	}
	public void doDemo() {
	    try {
	        client = new MqttClient("tcp://10.250.33.173:2882", "Sending");
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
