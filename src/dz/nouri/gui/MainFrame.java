package dz.nouri.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import dz.nouri.processing.AnimationCalculation;
import dz.nouri.processing.CalcKeyPoint;
import dz.nouri.processing.ImagesToVideo;
import dz.nouri.processing.VideoToImages;
import dz.nouri.tools.CustomFilter;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static MainFrame mainFrame;

	private JDesktopPane desktop;
	private JMenuBar menubar;
	private JMenu file, processing;
	private JMenuItem open, quit, calcKeyPoint, genVideo, initialize, calcAnim, open3D;
	private JToolBar toolBar;
	private JButton openBtn, quitBtn, calcKeyPointBtn, genVideoBtn, initializeBtn, calcAnimBtn, open3DBtn;
	private JFileChooser fileChooser;

	private OpenAction openAction = new OpenAction("Ouvrir", new ImageIcon("./icons/open.png"), "Ouvrir une video", KeyEvent.VK_O);
	private QuitAction quitAction = new QuitAction("Quitter", new ImageIcon("./icons/quit.png"), "Quitter l'application", KeyEvent.VK_Q);
	private CalcKeyPointAction calcKeyPointAction = new CalcKeyPointAction("Calc. Point de contrôle", new ImageIcon("./icons/match.png"), "Calculer les points de contrôles", KeyEvent.VK_K);
	private GenerateVideoAction generateVideoAction = new GenerateVideoAction("Générer PDC vidéo", new ImageIcon("./icons/match.png"), "Générer une vidéo des images du PDC", KeyEvent.VK_G);
	private initializeAction initializeAction = new initializeAction("Initializer PDC & FDP", new ImageIcon("./icons/match.png"), "Initialize les points de controles 2D et les FDP 3D", KeyEvent.VK_I);
	private CalcAnimAction calcAnimAction = new CalcAnimAction("Calc.param.Animation", new ImageIcon("./icons/openAnim.png"), "Calculer les paramètres de l'animation", KeyEvent.VK_A);
	private Open3DAction open3DAction = new Open3DAction("Ouvrir scène 3D", new ImageIcon("./icons/open3D.png"), "Ouvrir la scène 3D", KeyEvent.VK_D);

	public static MainFrame createMainFrame() {
		if (mainFrame == null)
			mainFrame = new MainFrame();
		return mainFrame;
	}

	private MainFrame() {
		super("PFE 2014 - Bouhdid Nour eddine");
		init();
		initMenuBar();
		initToolBar();
	}

	private void init() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				quit();
			}
		});

		setSize(800, 600);
		setLocationRelativeTo(null);
		this.setIconImage(new ImageIcon("./icons/windowIcons.png").getImage());
		desktop = new JDesktopPane();
		desktop.setBackground(Color.DARK_GRAY);
		add(new JScrollPane(desktop, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
	}

	private void quit() {
		int choice = JOptionPane.showConfirmDialog(null, "Etes-vous sûr?", "Quitter", JOptionPane.YES_NO_OPTION);
		if (choice == 0)
			System.exit(0);
	}

	private void initMenuBar() {

		menubar = new JMenuBar();
		file = new JMenu("Fichier");
		file.setMnemonic(KeyEvent.VK_F);
		open = new JMenuItem(openAction);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.VK_ALT, true));

		quit = new JMenuItem(quitAction);

		file.add(open);
		file.add(quit);

		processing = new JMenu("Traitement");

		calcKeyPoint = new JMenuItem(calcKeyPointAction);
		calcKeyPoint.setEnabled(false);

		genVideo = new JMenuItem(generateVideoAction);
		genVideo.setEnabled(false);
		
		initialize = new JMenuItem(initializeAction);
		initialize.setEnabled(false);

		calcAnim = new JMenuItem(calcAnimAction);
		calcAnim.setEnabled(false);

		open3D = new JMenuItem(open3DAction);
		open3D.setEnabled(false);

		processing.add(calcKeyPoint);
		processing.add(genVideo);
		processing.add(initialize);
		processing.add(calcAnim);
		processing.add(open3D);

		menubar.add(file);
		menubar.add(processing);

		setJMenuBar(menubar);
	}

	private void initToolBar() {

		toolBar = new JToolBar(JToolBar.HORIZONTAL);
		toolBar.setFloatable(false);

		openBtn = new JButton(openAction);
		quitBtn = new JButton(quitAction);

		calcKeyPointBtn = new JButton(calcKeyPointAction);
		calcKeyPointBtn.setEnabled(false);

		genVideoBtn = new JButton(generateVideoAction);
		genVideoBtn.setEnabled(false);
		
		initializeBtn = new JButton(initializeAction);
		initializeBtn.setEnabled(false);

		calcAnimBtn = new JButton(calcAnimAction);
		calcAnimBtn.setEnabled(false);

		open3DBtn = new JButton(open3DAction);
		open3DBtn.setEnabled(false);

		toolBar.add(openBtn);
		toolBar.add(calcKeyPointBtn);
		toolBar.add(genVideoBtn);
		toolBar.add(calcAnimBtn);
		toolBar.add(open3DBtn);
		toolBar.add(quitBtn);

		add(toolBar, BorderLayout.NORTH);

	}

	class OpenAction extends AbstractAction {

		public OpenAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent event) {
			openVideo();
		}

		private void openVideo() {
			fileChooser = new JFileChooser(System.getProperty("user.dir"));
			fileChooser.setFileFilter(new CustomFilter(new String[] { "mov", "avi", "wmv" }, "Video mov - avi - wmv"));
			fileChooser.showOpenDialog(null);
			fileChooser.setVisible(true);
			File file = fileChooser.getSelectedFile();

			if (file != null) {
				System.out.println("Video charger avec succée!");
				VideoToImages.generateImagesFromVideo(file.getAbsolutePath(), "./images", new LoadingDialog(mainFrame, false));
				System.out.println("Video segmenter en images");
				desktop.removeAll();
				desktop.add(new VideoPlayer(fileChooser.getSelectedFile().getAbsolutePath(), 10, 10));
				calcKeyPointBtn.setEnabled(true);
				calcKeyPoint.setEnabled(true);
			}

		}

	}

	class QuitAction extends AbstractAction {
		public QuitAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			quit();
		}

	}

	class CalcKeyPointAction extends AbstractAction {

		public CalcKeyPointAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			new SwingWorker() {
				@Override
				protected Object doInBackground() throws Exception {
					CalcKeyPoint.registration(new LoadingDialog(mainFrame, false));
					CalcKeyPoint.calcKeyPoint();
					genVideo.setEnabled(true);
					genVideoBtn.setEnabled(true);
					return null;
				}

			}.execute();
		}

	}

	class GenerateVideoAction extends AbstractAction {

		public GenerateVideoAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			new SwingWorker() {
				protected Object doInBackground() throws Exception {
					Dimension size = CalcKeyPoint.getVideoSize();
					if (size != null) {
						ImagesToVideo.generateVideo(size);
					} else {
						ImagesToVideo.generateVideo(new Dimension(1024, 768));
					}
					desktop.add(new VideoPlayer("./generatedVideo/video.mov", 420, 10));
					calcAnimBtn.setEnabled(true);
					calcAnim.setEnabled(true);
					return null;
				}
			}.execute();
		}

	}

	class initializeAction extends AbstractAction {

		public initializeAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
	
	class CalcAnimAction extends AbstractAction {

		public CalcAnimAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AnimationCalculation.calculateAnimationSquence();
			open3D.setEnabled(true);
			open3DBtn.setEnabled(true);
		}

	}

	class Open3DAction extends AbstractAction {

		public Open3DAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			desktop.add(new Play3DScene("./resultatExpression/expression.nouri"));

		}

	}

}
