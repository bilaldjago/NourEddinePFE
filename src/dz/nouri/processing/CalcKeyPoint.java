package dz.nouri.processing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Luxand.FSDK;
import Luxand.FSDK.FSDK_Features;
import Luxand.FSDK.HImage;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import dz.nouri.gui.LoadingDialog;

public class CalcKeyPoint {

	private static Dimension size = null;
	private static LoadingDialog dialog;

	public static void registration(LoadingDialog dialog) {
		CalcKeyPoint.dialog = dialog;
		CalcKeyPoint.dialog.setMessage("Calcule des points de contrôles");
		try {
			int r = FSDK
					.ActivateLibrary("fVYkJQQzaUgMkgBeOlDfysR6+5NF7uds5WyKxJCKnUJQ0xsBnt/B69yG0iCVKny2vawm53WuTvjmnYiM8jF3vdJ4zEtD+HJf1hjVYHFI8M9wfOCWbSfRlQT1hWrK+hfsfIBKY1lGKxpb/QbvCoIlh3C1I4Dq9Tb+SuDX5WvVIT8=");
			if (r != FSDK.FSDKE_OK) {
				JOptionPane
						.showMessageDialog(
								null,
								"Please run the License Key Wizard (Start - Luxand - FaceSDK - License Key Wizard)"
										+ "\n<html><b><font color=\"red\">Hint:</font></b> This license key is for 2013, change your date to that year!</html>",
								"Error activating FaceSDK",
								JOptionPane.ERROR_MESSAGE);
				System.exit(r);
			}
		} catch (java.lang.UnsatisfiedLinkError e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Link Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		FSDK.Initialize();
		FSDK.SetFaceDetectionParameters(false, true, 384);
	}

	public static void calcKeyPoint() {

		ArrayList<File> imageList = getImages();

		for (int x = 0; x < imageList.size(); x++) {
			dialog.setProgress(((x + 1) * 100) / imageList.size());
			File fileName = imageList.get(x);
			HImage imageHandle = new HImage();
			if (FSDK.LoadImageFromFileW(imageHandle, fileName.getAbsolutePath()) == FSDK.FSDKE_OK) {
				Image awtImage[] = new Image[1];
				if (FSDK.SaveImageToAWTImage(imageHandle, awtImage,
						FSDK.FSDK_IMAGEMODE.FSDK_IMAGE_COLOR_24BIT) != FSDK.FSDKE_OK) {
					JOptionPane.showMessageDialog(null,
							"Error displaying picture");
				} else {
					Image img = awtImage[0];
					BufferedImage bimg = null;

					// c'est ici qu'on détecte le visage ainsi que ses points.
					FSDK.TFacePosition.ByReference facePosition = new FSDK.TFacePosition.ByReference();

					if (FSDK.DetectFace(imageHandle, facePosition) != FSDK.FSDKE_OK) {
						JOptionPane.showMessageDialog(null, "No faces found");
					} else {
						size = new Dimension(img.getWidth(null),
								img.getHeight(null));
						bimg = new BufferedImage(img.getWidth(null),
								img.getHeight(null),
								BufferedImage.TYPE_INT_ARGB);
						Graphics g = bimg.getGraphics();
						g.drawImage(img, 0, 0, null);
						g.setColor(Color.green);

						int left = facePosition.xc - facePosition.w / 2;
						int top = facePosition.yc - facePosition.w / 2;
						g.drawRect(left, top, facePosition.w,
								facePosition.w + 40);

						FSDK_Features.ByReference facialFeatures = new FSDK_Features.ByReference();
						FSDK.DetectFacialFeaturesInRegion(imageHandle,
								(FSDK.TFacePosition) facePosition,
								facialFeatures);

						String coord = "";
						// La liste FSDK contient les coordonnées de tout les
						// points du notre visage.
						for (int i = 0; i < FSDK.FSDK_FACIAL_FEATURE_COUNT; ++i) {
							if ((i == 2) || (i == 3) || (i == 4) || (i == 11)
									|| (i == 12) || (i == 13) || (i == 14)
									|| (i == 15) /*|| (i == 16) || (i == 17) */
									|| (i == 23) || (i == 24) || (i == 25)
									|| (i == 26) /*|| (i == 27)*/ || (i == 28)
									/*|| (i == 31)*/ || (i == 32) ||/*(i == 50)
									|| (i == 51) || */(i == 54) || (i == 55)
									/*|| (i == 56) || (i == 57) || (i == 58)
									|| (i == 59)*/) {
								g.setColor(Color.blue);
								g.fillOval(facialFeatures.features[i].x,
										facialFeatures.features[i].y, 8, 8);
								coord += String.valueOf(facialFeatures.features[i].x)
										+ ","
										+ String.valueOf(facialFeatures.features[i].y)
										+ "," + i + "|";
								Graphics2D g2 = (Graphics2D) bimg.getGraphics();
								g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
								g2.setColor(Color.YELLOW);
								g2.setFont(new Font("Arial", Font.BOLD, 16));
								g2.drawString("(" + i + ")", facialFeatures.features[i].x, facialFeatures.features[i].y);
							}
//							else {
//								coord += String
//										.valueOf(facialFeatures.features[i].x)
//										+ ","
//										+ String.valueOf(facialFeatures.features[i].y)
//										+ "," + i + "|";
//							}

						}

						g.dispose();
						String name = fileName.getName().substring(0,
								fileName.getName().indexOf('.'));
						saveImage(bimg, name);
						saveFile(coord, name);
					}
				}
				FSDK.FreeImage(imageHandle);
			}
		}

	}

	private static void saveImage(Image image, String imageName) {

		BufferedImage buffer = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) buffer.getGraphics();
		g.drawImage(image, null, null);
		try (FileOutputStream out = new FileOutputStream("./imageKeyPoint/"
				+ imageName + ".jpg")) {
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(buffer);
			param.setQuality(.5f, false);
			encoder.setJPEGEncodeParam(param);

			encoder.encode(buffer);
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<File> getImages() {
		ArrayList<File> imageList = new ArrayList<>();
		File folder = new File("./images");
		for (File f : folder.listFiles())
			imageList.add(f);
		return imageList;

	}

	private static void saveFile(String data, String fileName) {
		try {
			PrintWriter writer = new PrintWriter(new File("./data/" + fileName
					+ ".nouri"));
			writer.println(data);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Dimension getVideoSize() {
		if (size != null)
			return size;
		return null;

	}

}
