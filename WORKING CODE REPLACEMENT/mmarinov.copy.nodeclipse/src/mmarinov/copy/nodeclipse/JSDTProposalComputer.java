package mmarinov.copy.nodeclipse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;


/**
 * This is wrapper for NodeContentAssistant
 * as required by JSDT  
 * 
 * asked http://stackoverflow.com/questions/20738788/content-assist-for-eclipse-jsdt-based-editor
 * HELP http://help.eclipse.org/juno/index.jsp?topic=/org.eclipse.wst.jsdt.doc/reference/extension-points/org_eclipse_wst_jsdt_ui_javaCompletionProposalComputer.html
 * example 
 * HippieProposalComputer - http://svn.codespot.com/a/eclipselabs.org/mobile-web-development-with-phonegap/tags/r1.2/org.eclipse.wst.jsdt.ui/src/org/eclipse/wst/jsdt/internal/ui/text/java/HippieProposalComputer.java
 * HippieProposalProcessor - http://grepcode.com/file/repository.grepcode.com/java/eclipse.org/3.5.2/org.eclipse.ui.workbench/texteditor/3.5.1/org/eclipse/ui/texteditor/HippieProposalProcessor.java
 * 
 * @author Paul
 */
public class JSDTProposalComputer implements IJavaCompletionProposalComputer {
	
	IContentAssistProcessor assistant = new NodeContentAssistant();

	@Override
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {		 
		//List<ICompletionProposal> proposals = Arrays.asList(assistant.computeCompletionProposals(context.getViewer(), context.getInvocationOffset()));
		List<ICompletionProposal> proposals = MyCompletionProposal.getSampleProposals(context);
		return proposals;
	}

	@Override
	public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		return Arrays.asList(assistant.computeContextInformation(context.getViewer(), context.getInvocationOffset()));
//		return Collections.emptyList();
	}

	@Override
	public String getErrorMessage() {
		return assistant.getErrorMessage();
//		return null;
	}

	@Override
	public void sessionEnded() {	
	}

	@Override
	public void sessionStarted() {
	}
}

/*
 


public class JSDTProposalComputer implements IJavaCompletionProposalComputer {
	
	//IContentAssistProcessor assistant = new NodeContentAssistant();

	@Override
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {
//		ITextViewer v = context.getViewer(); 
//		context.getInvocationOffset();
		return null;//Arrays.asList(assistant.computeCompletionProposals(context.getViewer(), context.getInvocationOffset()));
	}

	@Override
	public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		return Collections.emptyList();//Arrays.asList(assistant.computeContextInformation(context.getViewer(), context.getInvocationOffset()));
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public void sessionEnded() {	
	}

	@Override
	public void sessionStarted() {
	}

}
*/
