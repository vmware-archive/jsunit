package net.jsunit.plugin.eclipse.preference;

import java.io.File;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

public class ListFileFieldEditor extends ListEditor {

	protected ListFileFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}
	
	protected String createList(String[] items) {
		StringBuffer buffer = new StringBuffer();
		for (String item : items) {
			buffer.append(item);
			buffer.append(",");
		}
		return buffer.toString();
	}

	protected String getNewInputObject() {
		File file = getFile();
		return file == null ? null : file.getAbsolutePath();
	}
	
	private File getFile() {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] {"*.exe"});
        String file = dialog.open();
        if (file != null) {
            file = file.trim();
            if (file.length() > 0)
                return new File(file);
        }

        return null;
    }

	protected String[] parseString(String stringList) {
		return stringList!= null ? stringList.split(",") : new String[]{};
	}

}
