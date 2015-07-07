package cems;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;

public class RConnect {
	static FileReader fr = null;
	static BufferedReader br = null;
	static StringBuilder sb = new StringBuilder();
	public static int count = 0;
	
	/************************************************** R Master Parameter **************************************************/
	static String HOST = Setting.readFile("config", "R_Host");
	static int PORT = Integer.parseInt(Setting.readFile("config", "R_Port"));
	
	static RConnection MASTER = RConnect(HOST, PORT);

	/* Testing Field */
//	static String str = "{\"tg_id\":\"TG123\",\"datetime\":\"201501142101\",\"sensors_data\":[{\"sensor1\":\"9\"},{\"sensor2\":\"1\"},{\"sensor3\":\"15\"}]}";
//	static String str = "{\"tg_id\":\"TG123\",\"datetime\":\"201501142101\",\"sensors_data\":[{\"MODE\":\"7\"},{\"DEVICE\":\"01\"}]}";
//	static String str = "{\"tg_id\":\"TG123\",\"datetime\":\"201501142101\",\"sensors_data\":[{\"sensor1\":\"9\"},{\"sensor2\":\"1\"},{\"sensor3\":\"15\"},{\"MODE\":\"7\"},{\"DEVICE\":\"01\"}]}";
//	static String str = "{\"tg_id\":\"TG01\",\"sensors_data\":[{\"light\":\"1\"}]}";
//	public static void main(String[] args) throws REXPMismatchException {
//		start(str);
//	}
	
	
	/************************************************** Message from MQTT to R Master **************************************************/
	public static void start(String data) throws REXPMismatchException {
		System.out.println("START:" + data);
		
		
		RInit(MASTER);
		RQuery(MASTER, readNodelist());
		RQuery(MASTER, "CEMS::cens.startRMaster('" + data + "')");
	}

	/************************************************** Connect R Master **************************************************/
	private static RConnection RConnect(String HOST, int PORT) {
		try {
			return new RConnection(HOST, PORT);
		} catch (RserveException e) {
			System.out.println("R Master Connect Fail");
		}
		return null;
	}

	/************************************************** Query to R Connection **************************************************/
	private static REXP RQuery(RConnection RC, String QUERY) {
		if (RC.isConnected()) {
			try {
				return RC.eval(QUERY);
			} catch (RserveException e) {
				System.out.println("R Evaluation Fail");
			}
		}
		return null;
	}

	/************************************************** Initialize R Environment **************************************************/
	private static void RInit(RConnection RC) {
		if (RC.isConnected()) {
			try {
				RC.eval("require(CEMS)");
				RC.eval("CEMS::checkpkg('RSclient')");
				RSetting(RC);
			} catch (RserveException e) {
				System.out.println("R Master Initialization Fail");
			}
		}
	}

	/************************************************** Reading Setting File **************************************************/
	private static void RSetting(RConnection RC) {
		FileReader fr = null;
		BufferedReader br = null;
		String line = null;
		String[] Array;
		try {
			fr = new FileReader("Rconfig");
			br = new BufferedReader(fr);
			
			while ((line = br.readLine()) != null) {
				Array = line.split("=");
				RC.assign(Array[0].toString(), Array[1].toString());
			}
			RC.assign("Key", mongoConnect.receiveKey());
		} catch (Exception e) {
			System.out.println("Not Found 'Rconfig' File");
		}
	}
	/************************************************** Reading Node List **************************************************/
	private static String readNodelist() {
		count++;
		if(count==9999) count = 0;
		String line = null;
		int len = countLine("nodelist");
		int i = 0;
		
		try {
			fr = new FileReader("nodelist");
			br = new BufferedReader(fr);
			sb.append("nodelist <- list(");
			while ((line = br.readLine()) != null) {
				if(i == count%len) {
					sb.append("\"");
					sb.append(line);
					sb.append("\"");
				}
				i++;
			}
			sb.append(")");
		} catch (Exception e) {
			System.out.println("Not Found 'nodelist' File");
		}
		return sb.toString();
	}
	/************************************************** Count File Line **************************************************/
	private static int countLine(String Filename) {
		int len = 0;
		
		try {
			fr = new FileReader(Filename);
			br = new BufferedReader(fr);
			while (br.readLine() != null) {
				len++;
			}			
		} catch (Exception e) {
			System.out.println("");
		}
		return len;
	}
}