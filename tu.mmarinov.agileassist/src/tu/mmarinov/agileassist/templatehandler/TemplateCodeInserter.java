package tu.mmarinov.agileassist.templatehandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import tu.mmarinov.agileassist.internal.AgileTemplate;
import tu.mmarinov.agileassist.internal.SelectionParser;

public class TemplateCodeInserter {
	public static AgileTemplate generateTemplateFromSelection(AgileTemplate template, int iterations, String selection){
		HashMap<String, String> defaultValues = SelectionParser.parseSelection(selection);
		String[] oldDef = template.getDefaultValues().clone();
		AgileTemplate prepTemplate = new AgileTemplate(template.getName(), template.getDescription(), oldDef, template.getProposalName(), template.getProposalDescription());
		AgileTemplate iteratedTemplate = prepTemplate.replaceInsertSegments(iterations);		
		Iterator it = defaultValues.entrySet().iterator();	    
	    while (it.hasNext()) {
	        Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
			String key = pairs.getKey();
	        try {
				int keyAsInt = Integer.parseInt(key);
				if (keyAsInt < iteratedTemplate.getDefaultValues().length) {
					iteratedTemplate.getDefaultValues()[keyAsInt] = pairs.getValue();					
				}
			} catch (NumberFormatException e) {
				String desc = iteratedTemplate.getDescription();
				desc = desc.replaceAll("$[" + key + "]", pairs.getValue());
				iteratedTemplate.setDescription(desc);
			}
	    }
		AgileTemplate finalTemplate = iteratedTemplate.removeSegments();	
		String indentation = getIdentationBeforeSelection();
		String finalDesc = finalTemplate.getDescription().replaceAll("\n", "\n" + indentation);
		finalTemplate.setDescription(finalDesc);
	    return finalTemplate;
	}
	
	public static ITextSelection getCurrentSelection()
	{
	    IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
			getActivePage().getActiveEditor();
		if (part instanceof ITextEditor)
		{
			final ITextEditor editor = (ITextEditor) part;
			IDocumentProvider prov = editor.getDocumentProvider();
			IDocument doc = prov.getDocument(editor.getEditorInput());
			ISelection sel = editor.getSelectionProvider().getSelection();
			if (sel instanceof TextSelection){
				ITextSelection textSel = (ITextSelection) sel;
				return textSel;
			}
		}
		return null;
	}
	
	public static void deleteCurrentSelection(){

	    IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
			getActivePage().getActiveEditor();
		if (part instanceof ITextEditor)
		{
			final ITextEditor editor = (ITextEditor) part;
			IDocumentProvider prov = editor.getDocumentProvider();
			IDocument doc = prov.getDocument(editor.getEditorInput());
			ISelection sel = editor.getSelectionProvider().getSelection();
			if (sel instanceof TextSelection){
				ITextSelection textSel = (ITextSelection) sel;
				try {
					doc.replace(textSel.getOffset(), textSel.getLength(), "");
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getIdentationBeforeSelection(){
		 IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
					getActivePage().getActiveEditor();
		 StringBuilder identation = new StringBuilder();
		if (part instanceof ITextEditor)
		{
			final ITextEditor editor = (ITextEditor) part;
			IDocumentProvider prov = editor.getDocumentProvider();
			IDocument doc = prov.getDocument(editor.getEditorInput());
			ISelection sel = editor.getSelectionProvider().getSelection();
			if (sel instanceof TextSelection){
				ITextSelection textSel = (ITextSelection) sel;
				if (sel instanceof TextSelection){
					int startOffset = textSel.getOffset();
					char c;
					try {
						int offset = startOffset - 1;
						c = doc.getChar(offset);
						while (c == ' ' || c == '\t') {
							identation.append(c);
							offset--;
							c = doc.getChar(offset);
						}
					} catch (BadLocationException e) {}
					
				}
			}
		}
		return identation.reverse().toString();
	}
}
