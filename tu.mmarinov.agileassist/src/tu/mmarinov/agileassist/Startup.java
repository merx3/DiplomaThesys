package tu.mmarinov.agileassist;

import java.util.ArrayList;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.ui.IStartup;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import tu.mmarinov.agileassist.internal.AgileTemplate;
import tu.mmarinov.agileassist.prefs.DefaultTemplatesSupplier;
import tu.mmarinov.agileassist.templatehandler.TemplateLoader;
import tu.mmarinov.agileassist.prefs.TemplatePreferencesEditor;

public class Startup implements IStartup{	
	private static Preferences preferences;
	
	@Override
	public void earlyStartup() {
		this.initialize();
	}

	private void initialize() {	
		if(preferences == null){
			preferences = ConfigurationScope.INSTANCE.getNode(TemplatePreferencesEditor.PREF_NODE);
		}				  
		if (!preferences.get("initialized", "").equals("true")) {
			ArrayList<AgileTemplate> templates = DefaultTemplatesSupplier.getDefaultTemplates();
			TemplateLoader.setTemplates(templates);
			TemplatePreferencesEditor.setPreferencesFromTemplates(templates);	
			preferences.put("initialized", "true");
			TemplatePreferencesEditor.savePreferences();
			//System.out.println("LOADED DEFAULTS");
		}	
		else{
			ArrayList<AgileTemplate> templates = TemplatePreferencesEditor.getTemplatesFromPreferences();
			TemplateLoader.setTemplates(templates);
			//System.out.println("LOADED PREFERENCES");
		}
	}
}
