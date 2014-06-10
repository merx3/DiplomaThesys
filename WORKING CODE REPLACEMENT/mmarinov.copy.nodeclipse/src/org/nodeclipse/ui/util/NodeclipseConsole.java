package org.nodeclipse.ui.util;

import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;
import org.nodeclipse.ui.Activator;
import org.nodeclipse.ui.preferences.PreferenceConstants;

/**
 * Console util
 * for tracing node launch parameters and errors
 * 
 * @author pverest
 */
public class NodeclipseConsole {

	private static NodeclipseConsole instance = null;
	private static IOConsoleOutputStream stream = null;

	public NodeclipseConsole() {
		MessageConsole console = new MessageConsole("Nodeclipse Console", null);
		console.activate();
		ConsolePlugin.getDefault().getConsoleManager()
				.addConsoles(new IConsole[] { console });
		stream = console.newOutputStream();
	}
	
	static {
		getInstance();
		write(VersionUtil.getLongString());
		write("visit http://www.nodeclipse.org/\n\n");
	}

	private static NodeclipseConsole getInstance() {
		if (instance == null)
			instance = new NodeclipseConsole();
		return instance;
	}

	public static void write(String s) {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		boolean nodeclipseConsoleEnabled = preferenceStore.getBoolean(PreferenceConstants.NODECLIPSE_CONSOLE_ENABLED);//@since 0.7
		if (!nodeclipseConsoleEnabled)
			return;
		
		instance = getInstance();
		try {
			stream.write(s);
		} catch (IOException e) {
			//TODO how to show?
			//e.printStackTrace();
		}
	}

	@Override
	public void finalize() {
		try {
			stream.close();
		} catch (IOException e) {
			//TODO how to show?
			//e.printStackTrace();
		}
	}

}
