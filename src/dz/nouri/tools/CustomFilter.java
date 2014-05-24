package dz.nouri.tools;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class CustomFilter extends FileFilter {

	private String[] suffix;
	private String description;

	public CustomFilter(String[] subfix, String desc) {
		this.suffix = subfix;
		this.description = desc;
	}

	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		String suffix = null;
		String fileName = f.getName();
		int index = fileName.lastIndexOf('.');
		if (index > 0 && index < fileName.length() - 1)
			suffix = fileName.substring(index + 1).toLowerCase();
		return suffix != null && contains(suffix);
	}

	private boolean contains(String s) {
		for (String suf : suffix)
			if (suf.equals(s))
				return true;
		return false;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}
