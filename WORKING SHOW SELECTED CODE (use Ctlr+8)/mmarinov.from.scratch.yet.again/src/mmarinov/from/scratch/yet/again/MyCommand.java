package mmarinov.from.scratch.yet.again;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class MyCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ITextSelection selection = this.getCurrentSelection();
		String selectedText = selection.getText();
		String message = "This takes forever..." + 
						 "\n\n" + 
						 "Your currently selected text is: " + selectedText;
		setTemplate("test123", "nothing", "no-context", "The quick brown....");
		message += "\n\n Template added!";
		MessageDialog.openInformation(null, "Hello", message);
		return null;
	}

	private ITextSelection getCurrentSelection()
	{
	    IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
			getActivePage().getActiveEditor();
		if (part instanceof ITextEditor)
		{
			final ITextEditor editor = (ITextEditor) part;
			IDocumentProvider prov = editor.getDocumentProvider();
			IDocument doc = prov.getDocument(editor.getEditorInput());
			ISelection sel = editor.getSelectionProvider().getSelection();
			if (sel instanceof TextSelection)
				{
				ITextSelection textSel = (ITextSelection) sel;
					return textSel;
				}
		}
		return null;
	}
	
	private void setTemplate(String name, String description, String context, String pattern){
		/*AbstractTextEditor activeEditor = 
				(AbstractTextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().
				getActivePage().getActiveEditor();

		ISourceViewer sourceViewer = 
		        (ISourceViewer) activeEditor.getAdapter(ITextOperationTarget.class);

		Point range = sourceViewer.getSelectedRange();

		// You can generate template dynamically here!
		Template template = new Template(name, 
				description, 
				context,	//"no-context", 
				pattern, true);

		IRegion region = new Region(range.x, range.y);
		TemplateContextType contextType = new TemplateContextType("test");
		TemplateContext ctx =
		    new DocumentTemplateContext(contextType, 
		        sourceViewer.getDocument(), 
		        range.x, 
		        range.y);

		TemplateProposal proposal 
		    = new TemplateProposal(template, ctx, region, null);

		proposal.apply(sourceViewer, (char) 0, 0, 0);
		*/
	}
}

