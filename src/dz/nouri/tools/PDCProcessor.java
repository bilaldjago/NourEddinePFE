package dz.nouri.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dz.nouri.process3d.Vector3f;

public class PDCProcessor {

	public static ArrayList<Vector3f> getPDCs(String file) {
		ArrayList<Vector3f> pdcs = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String[] tokens = reader.readLine().split("\\|");
			for (String point : tokens) {
				String[] data = point.split("\\,");
				pdcs.add(new Vector3f(Float.parseFloat(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2])));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pdcs;
	}

}
