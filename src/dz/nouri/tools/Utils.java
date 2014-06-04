package dz.nouri.tools;

import java.util.ArrayList;

public class Utils {

	public static String[] removeEmptyString(String[] data) {
		ArrayList<String> result = new ArrayList<>();
		
		for(int i = 0; i < data.length; i++) {
			if(!data.equals(""))
				result.add(data[i]);
		}
		
		String[] res = new String[result.size()];
		result.toArray(res);
		return res;
	}
	
	public static int[] toIntArray(ArrayList<Integer> indices) {
		int[] res = new int[indices.size()];
		
		for(int i = 0; i < indices.size(); i++)
			res[i] = indices.get(i).intValue();
		return res;
	}
	
}
