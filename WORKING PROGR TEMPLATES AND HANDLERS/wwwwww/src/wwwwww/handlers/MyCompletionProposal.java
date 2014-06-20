package wwwwww.handlers;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class MyCompletionProposal {


	public static void computeCompletionProposals(ExecutionEvent event) {

		//ArrayList<CompletionProposal> proposals = new ArrayList<CompletionProposal>();

		//proposals.add(new CompletionProposal("codeandme.blogspot.com", context.getInvocationOffset(), 0, "codeandme.blogspot.com".length()));
		//proposals.add(new CompletionProposal("<your proposal here>", context.getInvocationOffset(), 0, "<your proposal here>".length()));

		//return proposals;
		
		AbstractTextEditor activeEditor = 
		        (AbstractTextEditor) HandlerUtil.getActiveEditor(event);

		ISourceViewer sourceViewer = 
		        (ISourceViewer) activeEditor.getAdapter(ITextOperationTarget.class);

		Point range = sourceViewer.getSelectedRange();

		// You can generate template dynamically here!
		Template template = new Template("sample", 
		        "sample description", 
		        "no-context", 
		        "private void ${name}(){\r\n" + 
		        "\tSystem.out.println(\"${name}\")\r\n" + 
		        "${year} //<=== Selection"		
		        + "}\r\n", true);

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
	}
}
