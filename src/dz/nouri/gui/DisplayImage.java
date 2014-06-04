package dz.nouri.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class DisplayImage extends JInternalFrame {

	private JLabel lblImage;

	public DisplayImage(String filename) {
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		setBounds(5, 5, 400, 400);
		setTitle("Image initial");
		setResizable(false);
		
		try {
			BufferedImage bufImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
			Graphics g = bufImage.getGraphics();
			
			if (!"".equals(filename) && filename != null) {
				System.out.println("Good Path!");
				g.drawImage(ImageIO.read(new File(filename)), 0, 0, 400, 400, null);
				lblImage = new JLabel(new ImageIcon(bufImage));
				setContentPane(lblImage);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		setVisible(true);
	}

}
