package dz.nouri.gui;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class LoadingDialog extends JDialog {

	private static final long serialVersionUID = 8541743454498901610L;
	
	private int progress = 0;
	private String message = "";
	private JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
	private JLabel messageLbl = new JLabel("Processing");
	private JLabel progressLbl = new JLabel("0%");
	
	public LoadingDialog(JFrame owner, boolean modal) {
		super(owner, modal);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		Box vBox = Box.createVerticalBox();
		Box hBox = Box.createHorizontalBox();
		vBox.add(messageLbl);
		hBox.add(progressBar);
		hBox.add(progressLbl);
		vBox.add(hBox);
		this.add(vBox, BorderLayout.CENTER);
		setSize(280, 80);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		if(this.progress == 100)
			this.dispose();
		progressBar.setValue(getProgress());
		progressLbl.setText(getProgress() + "%");
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		messageLbl.setText(message);
	}

}
