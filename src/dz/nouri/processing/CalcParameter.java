package dz.nouri.processing;

import java.util.ArrayList;

import dz.nouri.process3d.Vector3f;

public class CalcParameter {

	public static ArrayList<Vector3f> getNewFDPs(ArrayList<Vector3f> initFDP, ArrayList<Vector3f> PDC) {
		ArrayList<Vector3f> result = new ArrayList<>();
		for (int i = 0; i < 16; i++) {
			result.add(Calculation.calculateTj(initFDP, PDC, (int) PDC.get(i).getZ()));
		}
		return result;
	}
}