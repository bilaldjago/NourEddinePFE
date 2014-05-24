package dz.nouri.processing;

import java.awt.Dimension;
import java.io.File;
import java.util.Vector;

import javax.media.MediaLocator;

public class ImagesToVideo {

	private static Dimension size;
	
	public static void generateVideo(Dimension size) {
		ImagesToVideo.size = size;
		File source = new File("./imageKeyPoint");
		Vector<String> input = new Vector<>();
		
		for (File f : source.listFiles())
			input.add(f.getAbsolutePath());
		
		MediaLocator mlc = new MediaLocator("file:"
				+ System.getProperty("user.dir") + File.separator + "generatedVideo\\video.mov");
		
		JpegImagesToMovie jtm = new JpegImagesToMovie();
		jtm.doIt(size.width, size.height, 30, input, mlc);
		
	}

}
