package org.nodeclipse.ui.preferences;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * Dialogs used in .debug & .phantomjs
 * @author Paul Verest, Pushkar Gupte
 */
public class Dialogs {

	
	public static void showPreferencesDialog(final String message) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

				MessageDialog dialog = new MessageDialog(shell, "Nodeclipse", null, message, 
						MessageDialog.ERROR, new String[] { "Open Prefrences ...", "Cancel" }, 0);
				int result = dialog.open();
				if (result == 0) {
					PreferenceDialog pref = PreferencesUtil.createPreferenceDialogOn(shell,
						PreferenceConstants.PREFERENCES_PAGE, null, null);
					if (pref != null)
						pref.open();
				}
			}
		});
	}
	
	
}
