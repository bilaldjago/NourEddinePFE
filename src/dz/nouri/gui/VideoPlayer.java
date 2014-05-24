package dz.nouri.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

@SuppressWarnings("serial")
public class VideoPlayer extends JInternalFrame {

	private JPanel thePanel;
	private Player player;

	public VideoPlayer(String path, int x, int y) {

		if (new File(path).exists()) {
			addInternalFrameListener(new InternalFrameAdapter() {
				@Override
				public void internalFrameClosing(InternalFrameEvent e) {
					player.stop();
					player.deallocate();
					player.close();
					setVisible(true);
				}
			});
			
			setBounds(x, y, 400, 400);
			setTitle("Video Player");
			thePanel = (JPanel) getContentPane();
			thePanel.setLayout(new BorderLayout());
			try {
				player = Manager.createRealizedPlayer(new MediaLocator("file:"
						+ path));
				if (player.getVisualComponent() != null)
					thePanel.add(player.getVisualComponent(),
							BorderLayout.CENTER);
				if (player.getControlPanelComponent() != null)
					thePanel.add(player.getControlPanelComponent(),
							BorderLayout.SOUTH);
				player.start();
			} catch (NoPlayerException | CannotRealizeException | IOException e1) {
				e1.printStackTrace();
			}
			setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Fichier video introuvable!", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		
	}

}
