package mmarinov.copy.nodeclipse;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

public class MyCompletionProposal {
 	
	public static List<ICompletionProposal> getSampleProposals(ContentAssistInvocationContext context){
		List<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
	    IDocument doc = context.getViewer().getDocument();
	    int offset = context.getInvocationOffset();
		String input = getInputString(doc, offset);
		if ("asdf".startsWith(input)) {
			proposals.add(new CompletionProposal("OMFG BACON!", offset-input.length(), input.length(), "OMFG BACON!".length(), null, "OMFG MY BACON!!!", null, ""));
		}
		
		return proposals;
	}
	
	 public static String getInputString(IDocument doc, int offset) {
	        StringBuffer buf = new StringBuffer();
	        while (true) {
	            try {
	                char charOffset = doc.getChar(--offset);
	                if (Character.isWhitespace(charOffset))
	                    break;
	                buf.append(charOffset);
	            } catch (BadLocationException e) {
	                break;
	            }
	        }
	        return buf.reverse().toString();
	    }
}
