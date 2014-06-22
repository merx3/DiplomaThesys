package wwwwww.handlers;

import java.io.IOException;

import wwwwww.dialog.Settings;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.templates.Template;

import wwwwww.handlers.MyCompletionProposal;
import wwwwww.handlers.AddTemplate;

import org.eclipse.jface.text.templates.persistence.*;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public SampleHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		//Settings sett = new Settings(window.getShell());
		//sett.open();
		
		
		
		
		AddCodeTemplate.addTemplate();
		//MyCompletionProposal.computeCompletionProposals(event);
		
		
		
		/*
		AbstractTextEditor activeEditor = 
		        (AbstractTextEditor) HandlerUtil.getActiveEditor(event);
		ISourceViewer sourceViewer = 
		        (ISourceViewer) activeEditor.getAdapter(ITextOperationTarget.class);
		AddTemplate templateAdder = new AddTemplate(activeEditor, sourceViewer);
		Template template = new Template("sample", 
		        "sample description", 
		        "java-statements", 
		        "private void ${name}(){\r\n" + 
		        "\tSystem.out.println(\"${name}\")\r\n" + 
		        "${year} //<=== compute Completion"		
		        + "}\r\n", true);
		//templateAdder.insertTemplate(template, sourceViewer.getDocument());
		
		//TemplateStore codeTemplateStore = org.eclipse.ui.texteditor.templates.TemplatePreferencePage.//JavaPlugin.getDefault().getCodeTemplateStore();

		IWorkbench wb = PlatformUI.getWorkbench();
		   IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		   IWorkbenchPage page = win.getActivePage().getWorkbenchWindow().getActivePage().;
		   
		TemplatePersistenceData[] templateData = codeTemplateStore.getTemplateData(true);
		Template[] templates = codeTemplateStore.getTemplates();
		System.out.println("GOT");
		for(int i=0; i<templates.length;i++){
			System.out.println(templates[i].getName());
		}
	     
	       TemplatePersistenceData datas = new TemplatePersistenceData(template, true);
	       
	       //for (int i = 0; i < datas.length; i++) {
	            updateTemplate(datas, templateData);
	      // }
	       try {
			codeTemplateStore.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	       */
	       
	       
	       
	       
//		MessageDialog.openInformation(
//				window.getShell(),
//				"Wwwwww",
//				"Hello, Eclipse world");
		return null;
	}
	
	 private void updateTemplate(TemplatePersistenceData data, TemplatePersistenceData[] templateData) {
	       for (int i = 0; i < templateData.length; i++) {
	            String id = templateData[i].getId();
	            if (id != null && id.equals(data.getId())) {
	                 templateData[i].setTemplate(data.getTemplate());
	                 break;
	            }
	       }
	  } 
	 
}
