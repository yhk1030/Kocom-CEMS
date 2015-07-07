package mqtt;

import org.eclipse.paho.client.mqttv3.*;

public class MqttSend {
	public static void main(String[] args) {}
	
/****************************************MQTT Publish****************************************/
	public static void SEND(String HOST, String PORT, String TOPIC, String MESSAGE) {
		try {
			MqttClient mClient = new MqttClient("tcp://" + HOST + ":" + PORT, "CEMS Analysis");
			mClient.connect();
			MqttMessage message = new MqttMessage(MESSAGE.getBytes());
			message.setQos(2);;
			mClient.publish(TOPIC, message);
			System.out.println("****************************************");
			System.out.println("Send URI - " + mClient.getServerURI());
			System.out.println("****************************************");

			mClient.disconnect();			
		} catch (Exception me) {
			System.out.println("MQTT Server Off");
		}
	}
}
