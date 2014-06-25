package tu.mmarinov.agileassist.templatehandler;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.persistence.TemplateStore;

import tu.mmarinov.agileassist.internal.AgileTemplate;

@SuppressWarnings("restriction")
public class TemplateLoader {
	private static ArrayList<AgileTemplate> templates;
	private static boolean isTemplateLoaded = false;
	private static final String loadedTemplateId = "tu.mmarinov.agileassist";

	public static boolean hasMatchingTemplate(String input){
		String inputTemplateName = input.replaceAll("\\d+$", "");
		int number; 
	    try  
        {  
           number = Integer.parseInt(input.substring(inputTemplateName.length()));
        } 
	    catch(NumberFormatException nfe) {  
           number = -1;
        }	
		if (number >= 1) {
			if (getTemplate(inputTemplateName)!=null) {
				return true;
			}
		}
		return false;
	}
	
	public static AgileTemplate getTemplate(String name) {			
		for (AgileTemplate template : templates) {
			if (name.equals(template.getName())) {
				return template;
			}
		}
		return null;
	}
	
	public static ArrayList<AgileTemplate> getTemplates() {
		return templates;
	}
	
	public static void setTemplates(ArrayList<AgileTemplate> templates) {
		TemplateLoader.templates = templates;
	}

	public static void loadTemplate(String input){
		String inputTemplateName = input.replaceAll("\\d+$", "");
		AgileTemplate templateDraft = TemplateLoader.getTemplate(inputTemplateName);
		AgileTemplate readyTemplate = templateDraft.applyInput(input);
		
		TemplateStore codeTemplateStore = JavaPlugin.getDefault().getTemplateStore();
		Template templateToApply = new Template(readyTemplate.getName(), 
				readyTemplate.getProposalDescription(), 
		        "java-statements", 
		        readyTemplate.getDescription(), true);
		TemplatePersistentDataExtend tpd = new TemplatePersistentDataExtend(templateToApply, true, loadedTemplateId);
		codeTemplateStore.add(tpd);
		try {
			codeTemplateStore.save();
			isTemplateLoaded = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void unloadTemplate(){
		if (isTemplateLoaded) {
			TemplateStore codeTemplateStore = JavaPlugin.getDefault().getTemplateStore();
			codeTemplateStore.delete(codeTemplateStore.getTemplateData(loadedTemplateId));
			try {
				codeTemplateStore.save();
				isTemplateLoaded = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
