package cems;

import java.io.BufferedReader;
import java.io.FileReader;

public class Setting {
	
	static FileReader fr = null;
	static BufferedReader br = null;
		
	public static String readFile(String file, String attr){
		String line = null;
		String[] Array;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			while ((line = br.readLine()) != null) {
				Array = line.split("=");
				if( attr.equals(Array[0].toString()) )
					 return Array[1].toString();
			}
			
		} catch (Exception e) {
			System.out.println("Not Found 'config' File");
		}
		return null;
	}
}
