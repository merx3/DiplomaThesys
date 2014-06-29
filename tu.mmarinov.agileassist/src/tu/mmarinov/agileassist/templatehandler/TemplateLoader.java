package tu.mmarinov.agileassist.templatehandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

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
		HashSet<String> templateNames = new HashSet<String>();
		HashSet<String> proposalsNames = new HashSet<String>();
		for (AgileTemplate agileTemplate : templates) {
			String tName = agileTemplate.getName();
			String pName = agileTemplate.getProposalName();
			if(templateNames.contains(tName)){
				throw new IllegalArgumentException("Template name must be unique.");
			}
			if (proposalsNames.contains(pName)) {
				throw new IllegalArgumentException("Proposal names must be unique.");
			}
			templateNames.add(tName);
			proposalsNames.add(pName);
		}
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
	
	public static void applyTemplate(AgileTemplate template, AbstractTextEditor activeEditor){
		TemplateCodeInserter.deleteCurrentSelection();
		ISourceViewer sourceViewer = 
		        (ISourceViewer) activeEditor.getAdapter(ITextOperationTarget.class);
		Point range = sourceViewer.getSelectedRange();

		// You can generate template dynamically here!
		Template t = new Template("tempTemplate", 
		        "no description", 
		        "no-context", 
		        template.getDescription(), true);
		
		IRegion region = new Region(range.x, range.y);
		TemplateContextType contextType = new TemplateContextType("test");
		TemplateContext ctx =
		    new DocumentTemplateContext(contextType, 
		        sourceViewer.getDocument(), 
		        range.x, 
		        range.y);

		TemplateProposal proposal 
		    = new TemplateProposal(t, ctx, region, null);

		proposal.apply(sourceViewer, (char) 0, 0, region.getOffset());
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
