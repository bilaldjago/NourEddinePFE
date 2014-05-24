package dz.nouri.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class FDPMapPDC {

	public static HashMap<String, Integer> getTheMap() {
		HashMap<String, Integer> theMap = new HashMap<>();
		
		String mapFile = "./source/themap.txt";
		try(BufferedReader reader = new BufferedReader(new FileReader(mapFile))) {
			String line = reader.readLine();
			String[] token1 = line.split("\\|");
			for(String str: token1) {
				String[] token2 = str.split("\\,");
				theMap.put(token2[0], Integer.parseInt(token2[1].trim()));
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return theMap;
	}
	
}
