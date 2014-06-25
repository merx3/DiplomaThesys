package tu.mmarinov.agileassist.proposalhandler;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

import tu.mmarinov.agileassist.internal.AgileTemplate;
import tu.mmarinov.agileassist.templatehandler.TemplateLoader;

public class JDTProposalComputer implements IJavaCompletionProposalComputer{

	@Override
	public List<ICompletionProposal> computeCompletionProposals(
			ContentAssistInvocationContext context, IProgressMonitor monitor) {
		IDocument doc = context.getViewer().getDocument();
	    int offset = context.getInvocationOffset();
		String input = ProposalsAssistant.getInputString(doc, offset);
		if (TemplateLoader.hasMatchingTemplate(input)) {
			TemplateLoader.loadTemplate(input);
			return Collections.EMPTY_LIST;
		}
		List<ICompletionProposal> proposals = ProposalsAssistant.getProposals(input, offset);
		System.out.println(TemplateLoader.getTemplates().size());
		return proposals;
	}

	@Override
	public List<IContextInformation> computeContextInformation(
			ContentAssistInvocationContext arg0, IProgressMonitor arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sessionEnded() {
		TemplateLoader.unloadTemplate();
	}

	@Override
	public void sessionStarted() {
		// TODO Auto-generated method stub
	}

}
