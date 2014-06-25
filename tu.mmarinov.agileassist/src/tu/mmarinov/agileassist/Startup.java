package tu.mmarinov.agileassist;

import java.util.ArrayList;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.ui.IStartup;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import tu.mmarinov.agileassist.internal.AgileTemplate;
import tu.mmarinov.agileassist.prefs.DefaultTemplatesSupplier;
import tu.mmarinov.agileassist.templatehandler.TemplateLoader;

public class Startup implements IStartup{	
	private static Preferences preferences;
	
	@Override
	public void earlyStartup() {
		if(preferences == null){
			preferences = ConfigurationScope.INSTANCE.getNode("tu.mmarinov.agileassist");
		}				  
		this.initialize();
	}

	private void initialize() {	
		if (!preferences.get("initialized", "").equals("true")) {
			ArrayList<AgileTemplate> templates = DefaultTemplatesSupplier.getDefaultTemplates();
			TemplateLoader.setTemplates(templates);
			populatePreferenceDefaultTemplates(templates);	
			preferences.put("initialized", "true");
			savePreferences();
			//System.out.println("LOADED DEFAULTS");
		}	
		else{
			ArrayList<AgileTemplate> templates = loadTemplatesFromPreferences();
			TemplateLoader.setTemplates(templates);
			//System.out.println("LOADED PREFERENCES");
		}
		//debug_printPreferences();
	}

	/*
	 *  The template preferences store which templates are added by the 
	 *  plugin and what default values the segments of that template has.
     *  The template's name is a preference, showing how many default value segments 
	 *  the template has. The template's description is stored in "templateDesc" node. 
	 *  The segment's default values are stored in node "templateDefaults" as 
	 *  preference with the key "<Template-name>_DEF#", for ex.: switch_DEF2
	 *  Each template has a proposal, which is displayed so the user can find its usage
	 */	
	private void populatePreferenceDefaultTemplates(ArrayList<AgileTemplate> templates) {
		Preferences prefTemplateDefaultsCount = preferences.node("templateDefaultsCount");
		Preferences prefTemplateDefaults = preferences.node("templateDefaults");
		Preferences prefTemplateDescriptions = preferences.node("templateDesc");
		Preferences prefTemplateProposals = preferences.node("templateProposals");
			
		for(AgileTemplate template : templates) {
			String templateName = template.getName();			
			if(prefTemplateDefaultsCount.get(templateName, "").isEmpty()){
				prefTemplateDescriptions.put(templateName, template.getDescription());
				String[] defaultValues = template.getDefaultValues();
				prefTemplateDefaultsCount.putInt(templateName, defaultValues.length);
				for (int i = 0; i < defaultValues.length; i++) {
					prefTemplateDefaults.put(String.format("%s_DEF%d", templateName, i), defaultValues[i]);
				}
				prefTemplateProposals.put(templateName + "_PName", template.getProposalName());
				prefTemplateProposals.put(templateName + "_PDesc", template.getProposalDescription());
			}
		}
	}
	
	private ArrayList<AgileTemplate> loadTemplatesFromPreferences() {
		Preferences prefTemplateDefaultsCount = preferences.node("templateDefaultsCount");
		Preferences prefTemplateDefaults = preferences.node("templateDefaults");
		Preferences prefTemplateDescriptions = preferences.node("templateDesc");
		Preferences prefTemplateProposals = preferences.node("templateProposals");
		
		ArrayList<AgileTemplate> templates = new ArrayList<AgileTemplate>();
		String[] keys;
		try {
			keys = prefTemplateDescriptions.keys();
			for (int i = 0; i < keys.length; i++) {
				String templateName = keys[i];
				String templateDescription = prefTemplateDescriptions.get(templateName, "");
				int templateDefaultsCount = prefTemplateDefaultsCount.getInt(templateName, 0);
				String[] defaultValues = new String[templateDefaultsCount];
				for (int j = 0; j < defaultValues.length; j++) {
					defaultValues[j] = prefTemplateDefaults.get(String.format("%s_DEF%d", templateName, j), "");
				}
				String proposalName = prefTemplateProposals.get(templateName + "_PName", "");
				String proposalDesc = prefTemplateProposals.get(templateName + "_PDesc", "");
				AgileTemplate t = new AgileTemplate(templateName, templateDescription, defaultValues, proposalName, proposalDesc);
				templates.add(t);
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		return templates;
	}
	
	private void savePreferences(){		
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void debug_printPreferences() {
		// TODO Auto-generated method stub
		Preferences preferences = ConfigurationScope.INSTANCE
				  .getNode("tu.mmarinov.agileassist");
		Preferences prefTemplateDefaultsCount = preferences.node("templateDefaultsCount");
		Preferences prefTemplateDefaults = preferences.node("templateDefaults");
		Preferences prefTemplateDescriptions = preferences.node("templateDesc");
		Preferences prefTemplateProposals = preferences.node("templateProposals");
		
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
