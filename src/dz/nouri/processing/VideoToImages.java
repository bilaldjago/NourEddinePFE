package dz.nouri.processing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.Buffer;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.media.Time;
import javax.media.control.FrameGrabbingControl;
import javax.media.control.FramePositioningControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;

import dz.nouri.gui.LoadingDialog;

public class VideoToImages implements ControllerListener {

	private static VideoToImages instance = new VideoToImages();

	private static Buffer buffer;
	private static Image img;
	private static BufferToImage btm;
	private static Player player;
	private static String target;
	private static LoadingDialog dialog;

	public static void generateImagesFromVideo(String source, String target, LoadingDialog dialog) {
		try {
			VideoToImages.dialog = dialog;
			VideoToImages.dialog.setMessage("Image a partir du video");
			VideoToImages.target = target;
			System.out.println(source);
			player = Manager.createPlayer(new MediaLocator("file:" + source));
			player.addControllerListener(instance);
			player.realize();
		} catch (NoPlayerException | IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void controllerUpdate(ControllerEvent event) {
		if (event instanceof RealizeCompleteEvent) {
			int numImage = 1;

			FramePositioningControl fpc;
			FrameGrabbingControl fgc;

			Time duration = player.getDuration();
			int totalFrames = FramePositioningControl.FRAME_UNKNOWN;
			fpc = (FramePositioningControl) player.getControl("javax.media.control.FramePositioningControl");
			if (fpc == null) {
				System.err.println("Le média ne supporte pas les FramePositioningControl.");
				System.exit(0);
			} else {
				totalFrames = fpc.mapTimeToFrame(duration);
				System.out.println("Nombre total des images dans le média : " + totalFrames);

				
				
				while (numImage <= totalFrames) {
					dialog.setProgress((numImage * 100) / totalFrames);
					fpc.seek(numImage);
					fgc = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");
					buffer = fgc.grabFrame();
					btm = new BufferToImage((VideoFormat) buffer.getFormat());
					img = btm.createImage(buffer);

					Component cmp = player.getVisualComponent();

					BufferedImage tmp = new BufferedImage((int) cmp.getPreferredSize().getWidth(), (int) cmp.getPreferredSize().getHeight(), BufferedImage.TYPE_INT_RGB);

					Graphics g = tmp.getGraphics();

					g.drawImage(img, 0, 0, (int) cmp.getPreferredSize().getWidth(), (int) cmp.getPreferredSize().getHeight(), null);
					g.dispose();
					try {
						ImageIO.write(tmp, "jpeg", new File(target + "/" + ("image" + (numImage < 10 ? ("00" + numImage) : (numImage < 100 ? "0" + numImage : numImage))) + ".jpg"));
					} catch (IOException e) {
						e.printStackTrace();
						dialog.dispose();
					}

					numImage++;
				}
			}
		}

	}

}
