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
		if(preferences == null){
			preferences = ConfigurationScope.INSTANCE.getNode(TemplatePreferencesEditor.PREF_NODE);
		}				  
		this.initialize();
	}

	private void initialize() {	
		if (!preferences.get("initialized", "").equals("true")) {
			ArrayList<AgileTemplate> templates = DefaultTemplatesSupplier.getDefaultTemplates();
			TemplateLoader.setTemplates(templates);
			TemplatePreferencesEditor.populatePreferenceDefaultTemplates(templates);	
			preferences.put("initialized", "true");
			TemplatePreferencesEditor.savePreferences();
			//System.out.println("LOADED DEFAULTS");
		}	
		else{
			ArrayList<AgileTemplate> templates = TemplatePreferencesEditor.loadTemplatesFromPreferences();
			TemplateLoader.setTemplates(templates);
			//System.out.println("LOADED PREFERENCES");
		}
		//debug_printPreferences();
	}

	private void debug_printPreferences() {
		// TODO Auto-generated method stub
		Preferences preferences = ConfigurationScope.INSTANCE
				  .getNode(TemplatePreferencesEditor.PREF_NODE);
		Preferences prefTemplateDefaultsCount = preferences.node(TemplatePreferencesEditor.PREF_DEFAULTS_COUNT);
		Preferences prefTemplateDefaults = preferences.node(TemplatePreferencesEditor.PREF_DEFAULTS);
		Preferences prefTemplateDescriptions = preferences.node(TemplatePreferencesEditor.PREF_TEMPL_DESC);
		Preferences prefTemplateProposals = preferences.node(TemplatePreferencesEditor.PREF_PROPOSALS);
		
		try {
			String[] templateDefaultsCount = prefTemplateDefaultsCount.keys();
			String[] templateDefaults = prefTemplateDefaults.keys();
			String[] templateDescriptions = prefTemplateDescriptions.keys();
			String[] templateProposals  = prefTemplateProposals.keys();
			
			System.out.println("TEMPLATE DEFAULTS COUNT============");
			for (String string : templateDefaultsCount) {
				System.out.println(string);
				System.out.println(" -> " +prefTemplateDefaultsCount.getInt(string, -1));
			}
			System.out.println("TEMPLATE DEFAULTS============");
			for (String string : templateDefaults) {
				System.out.println(string);
				System.out.println(" -> " +prefTemplateDefaults.get(string, "ERROR"));
			}
			System.out.println("TEMPLATE DESCRIPTIONS============");
			for (String string : templateDescriptions) {
				System.out.println(string);
				System.out.println(" -> " +prefTemplateDescriptions.get(string, "ERROR"));
			}
			System.out.println("TEMPLATE PROPOSALS============");
			for (String string : templateProposals) {
				System.out.println(string);
				System.out.println(" -> " +prefTemplateProposals.get(string, "ERROR"));
			}
			System.out.println("=====================");
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
