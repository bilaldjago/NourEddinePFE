package test;

import java.util.ArrayList;

import dz.nouri.process3d.OBJModel;
import dz.nouri.process3d.Vector3f;
import dz.nouri.processing.CalcParameter;
import dz.nouri.tools.PDCProcessor;

public class TestRuntime {

	public static void main(String[] args) {

		System.out.println(new OBJModel().getFDPs());
		
		System.out.println("******************************************");
		
		System.out.println(PDCProcessor.getInitPDCs());
		
		System.out.println("******************************************");
		
		ArrayList<Vector3f> tj = CalcParameter.getNewFDPs(new OBJModel().getFDPs(), PDCProcessor.getInitPDCs());
		
		System.out.println(tj);
		
	}

}
