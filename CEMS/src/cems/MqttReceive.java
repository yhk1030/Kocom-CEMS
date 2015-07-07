package cems;

import org.eclipse.paho.client.mqttv3.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class MqttReceive {
	protected static String HOST = Setting.readFile("config", "MQTT_Host");
	protected static String PORT = Setting.readFile("config", "MQTT_Port");
//	protected static String key = "g2PYYeRkm4XwNs5SkT%2BEm6ZWuLXQCBNLJ4jdEH43rTuU0WjKjo";
	
	protected static String clientID = MqttClient.generateClientId();
	protected static MqttClient mClient;
	static RConnect rc = new RConnect();
	static JSONParser parser=new JSONParser();
	/* Testing Field */
	
	public static void main(String[] args) {
		MQTTConnect(HOST, PORT);
//		Mongoconnect.ReceiveKEY();
		
		
		Receive(mongoConnect.receiveKey());
	}

	/************************************************** Connect MQTT Server **************************************************/
	private static void MQTTConnect(String HOST, String PORT) {
		try {
			mClient = new MqttClient("tcp://" + HOST + ":" + PORT, clientID);
			mClient.connect();
			
			mClient.setCallback(new MqttCallback() {
				public void deliveryComplete(IMqttDeliveryToken arg0) {
				}
				public void messageArrived(String topic, final MqttMessage rMessage)
						throws Exception {
					
					String str = rMessage.toString();
					JSONObject doc = (JSONObject) parser.parse(str); 
					System.out.println(doc.get("type").toString());
					
					 if (doc.get("type").toString().equals("sensordata")) {
						RConnect.start(str);
					}
				}
				public void connectionLost(Throwable arg0) {
					MQTTConnect(HOST, PORT);
				}
			});
		} catch (MqttException e) {
			System.out.println("MQTTConnect() Error");
		}
	}

	/************************************************** Subscribe **************************************************/
	private static void Receive(String KEY) {
		try {
			mClient.subscribe(KEY + "/TGdata");
			System.out.println(KEY + "/TGdata");
		} catch (MqttException e2) {
			System.out.println("MQTT Subscribe Error");
			e2.printStackTrace();
		}
	}
	
}
