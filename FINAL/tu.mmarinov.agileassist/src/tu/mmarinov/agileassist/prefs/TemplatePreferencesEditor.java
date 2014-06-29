package tu.mmarinov.agileassist.prefs;

import java.util.ArrayList;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import tu.mmarinov.agileassist.internal.AgileTemplate;

public class TemplatePreferencesEditor {
	public static final String PREF_NODE = "tu.mmarinov.agileassist";
	public static final String PREF_DEFAULTS_COUNT = "templateDefaultsCount";
	public static final String PREF_DEFAULTS = "templateDefaults";
	public static final String PREF_TEMPL_DESC = "templateDesc";
	public static final String PREF_PROPOSALS = "templateProposals";
	
	private static Preferences preferences = ConfigurationScope.INSTANCE
			  .getNode(TemplatePreferencesEditor.PREF_NODE);
	private static Preferences prefTemplateDefaultsCount = preferences.node(TemplatePreferencesEditor.PREF_DEFAULTS_COUNT);
	private static Preferences prefTemplateDefaults = preferences.node(TemplatePreferencesEditor.PREF_DEFAULTS);
	private static Preferences prefTemplateDescriptions = preferences.node(TemplatePreferencesEditor.PREF_TEMPL_DESC);
	private static Preferences prefTemplateProposals = preferences.node(TemplatePreferencesEditor.PREF_PROPOSALS);
	
	
	/*
	 *  The template preferences store which templates are added by the 
	 *  plugin and what default values the segments of that template has.
     *  The template's name is a preference, showing how many default value segments 
	 *  the template has. The template's description is stored in "templateDesc" node. 
	 *  The segment's default values are stored in node "templateDefaults" as 
	 *  preference with the key "<Template-name>_DEF#", for ex.: switch_DEF2
	 *  Each template has a proposal, which is displayed so the user can find its usage
	 */	
	public static void setPreferencesFromTemplates(ArrayList<AgileTemplate> templates) {
		try {
			prefTemplateDefaultsCount.clear();
			prefTemplateDefaults.clear();
			prefTemplateDescriptions.clear();
			prefTemplateProposals.clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
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
	
	public static ArrayList<AgileTemplate> getTemplatesFromPreferences() {
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
	
	public static void deleteTemplateFromPreferences(String templateName) {
		if(prefTemplateDefaultsCount.getInt(templateName, -1) != -1){
			prefTemplateDescriptions.remove(templateName);
			int defaultValuesCount = prefTemplateDefaultsCount.getInt(templateName, 0);
			for (int i = 0; i < defaultValuesCount; i++) {
				prefTemplateDefaults.remove(String.format("%s_DEF%d", templateName, i));
			}
			prefTemplateDefaultsCount.remove(templateName);
			prefTemplateProposals.remove(templateName + "_PName");
			prefTemplateProposals.remove(templateName + "_PDesc");
		}
	}

	// update is just Delete + Add
	//public static void updateTemplateInPreferences(String oldTemplateName, AgileTemplate newTemplate) {
	//	
	//}
	
	public static void addNewTemplateToPreferences(AgileTemplate newTemplate) {
		String templateName = newTemplate.getName();
		if(prefTemplateDefaultsCount.get(templateName, "").isEmpty()){
			prefTemplateDescriptions.put(templateName, newTemplate.getDescription());
			String[] defaultValues = newTemplate.getDefaultValues();
			prefTemplateDefaultsCount.putInt(templateName, defaultValues.length);
			for (int i = 0; i < defaultValues.length; i++) {
				prefTemplateDefaults.put(String.format("%s_DEF%d", templateName, i), defaultValues[i]);
			}
			prefTemplateProposals.put(templateName + "_PName", newTemplate.getProposalName());
			prefTemplateProposals.put(templateName + "_PDesc", newTemplate.getProposalDescription());
		}		
	}

	
	public static void savePreferences(){		
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
