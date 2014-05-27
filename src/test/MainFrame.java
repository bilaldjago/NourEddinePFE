package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class MainFrame extends JFrame {

	private JDesktopPane desktop = new JDesktopPane();
	
	private int x = 0, y = 0, id = 1;
	
	public MainFrame() {
		super("Tests");
		setDefaultCloseOperation(3);
		setSize(320, 240);
		setLocationRelativeTo(null);
		desktop.setBackground(Color.DARK_GRAY);
		add(desktop, BorderLayout.CENTER);
		
		JPanel thePanel = new JPanel();
		JButton add = new JButton("Add new Frame");
		
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addInternelFrame();
			}
		});
		
		thePanel.add(add);
		
		add(thePanel, BorderLayout.SOUTH);
		
	}
	
	private void addInternelFrame() {
		
		JInternalFrame frame = new JInternalFrame("Frame " + id++);
		frame.addInternalFrameListener(new InternalFrameAdapter() {

			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				dispose();
			}
		});
		
		frame.setBackground(Color.GRAY);
		frame.setBounds(x, y, 100, 100);
		frame.setResizable(true);
		frame.setVisible(true);
		x += 10;
		y += 10;
		desktop.add(frame);
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
		
	}
	
}
