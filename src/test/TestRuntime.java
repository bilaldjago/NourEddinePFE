package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Jama.Matrix;
import dz.nouri.process3d.OBJModel;
import dz.nouri.process3d.Vector2f;
import dz.nouri.process3d.Vector3f;
import dz.nouri.processing.Calculation;
import dz.nouri.tools.FDPMapPDC;
import dz.nouri.tools.FDPProcessor;
import dz.nouri.tools.PDCProcessor;

public class TestRuntime {

	public static void main(String[] args) {
		HashMap<String, Integer> map = FDPProcessor.getFDPVertexIndices("./expressions/source2.fdp");
		OBJModel model = new OBJModel("./expressions/face.obj");
		ArrayList<Vector3f> fdp = new ArrayList<>();

		for(Integer index : map.values()) {
			fdp.add(model.getPosition(index));
		}
		
		ArrayList<Vector3f> pdc = PDCProcessor.getPDCs("./data/image100.nouri");
		
		System.out.println("Nombre des sommets du Mesh: " + model.getPositions().size());
		System.out.println("*******************************");
		
		HashMap<String, Integer> theMap = FDPMapPDC.getTheMap(); // Map between PDC and FDP
		System.out.println("Liste de corespondance entre FDP & PDC");
		System.out.println("");
		for(Map.Entry<String, Integer> entry : theMap.entrySet())
			System.out.println(entry.getKey() + "->" + entry.getValue());
		
		ArrayList<Vector3f> weights = Calculation.calculateW(fdp, get2D(pdc)); 
		System.out.println("*******************************");
		System.out.println("\nListe des valeurs du vecteur W:");
		for(Vector3f vec : weights)
			System.out.println(vec);
		
		Matrix m = Calculation.calculateHMatrix(get2D(PDCProcessor.getPDCs("./data/image002.nouri")));
		System.out.println("*******************************");
		System.out.println("Element (0,0) du matrice H : " + m.get(0, 0));
		
		Vector3f t10 = Calculation.calculateTj(weights, get2D(pdc), 10); // evaluate the first Tj
		
		System.out.println("Premier Tj: " + t10);
		
		System.out.println(new Vector2f(3,4).norme());
		
		ArrayList<Vector2f> pdc2D = get2D(pdc);
		
		// TODO Normalization des coordonnées 2D & 3D (Calcule du ratio)
		
		System.out.println("Point1: " + pdc.get(0) + "\n Point2: " + pdc.get(1));
		System.out.println("Point1: " + pdc2D.get(0) + "\nPoint2: " + pdc2D.get(1));
		System.out.println("h(||Sj - Si||) = " + Calculation.calcH(pdc2D, 0, 1));
		System.out.println(fdp.get(10));
	}
	
	private static ArrayList<Vector2f> get2D(ArrayList<Vector3f> source) {
		ArrayList<Vector2f> result = new ArrayList<>();

		for(Vector3f vec : source) {
			result.add(vec.getXYPoint());
		}
		
		return result;
	}

}
