package wwwwww.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class Settings extends Dialog {
	private Text text;
	private Text text_1;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public Settings(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 3;
		new Label(container, SWT.NONE);
		
		Composite composite = new Composite(container, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_composite.heightHint = 85;
		gd_composite.widthHint = 243;
		composite.setLayoutData(gd_composite);
		
		text = new Text(composite, SWT.BORDER);
		text.setBounds(10, 10, 78, 26);		
		
		Label lblLastEntered = new Label(composite, SWT.NONE);
		lblLastEntered.setBounds(18, 55, 90, 20);
		lblLastEntered.setText("Last entered:");

		Preferences preferences = ConfigurationScope.INSTANCE
		            .getNode("de.vogella.preferences.test");
        Preferences entries = preferences.node("lastEntered");
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setEditable(false);
		text_1.setText(entries.get("last", ""));
		text_1.setBounds(114, 55, 102, 26);
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Preferences preferences = ConfigurationScope.INSTANCE
			            .getNode("de.vogella.preferences.test");
				Preferences entries = preferences.node("lastEntered");
				String entry = text.getText();
				text_1.setText(entry);
				entries.put("last", entry);
			}
		});
		btnSave.setBounds(143, 6, 90, 30);
		btnSave.setText("Save");

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	@Override
	public boolean close(){
		super.close();
		Preferences preferences = ConfigurationScope.INSTANCE
	            .getNode("de.vogella.preferences.test");
		try {
			  // forces the application to save the preferences
			preferences.flush();
	    } catch (BackingStoreException e) {
	        e.printStackTrace();
	    }
		return true;		
	}
}
