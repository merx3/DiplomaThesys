package tu.mmarinov.agileassist.proposalhandler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.ui.PlatformUI;

import tu.mmarinov.agileassist.internal.AgileTemplate;
import tu.mmarinov.agileassist.templatehandler.TemplateLoader;

public class ProposalsAssistant {	
	public static List<ICompletionProposal> getProposals(String input, int offset){
		List<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
		
		ArrayList<AgileTemplate> templates = TemplateLoader.getTemplates();
		for (AgileTemplate template : templates) {
			String proposalName = template.getProposalName(); 
			if (proposalName.startsWith(input)) {
				String proposalDescription = template.getProposalDescription();
				proposals.add(new CompletionProposal(proposalName, offset-input.length(), input.length(), proposalName.length(), null, proposalName, null, proposalDescription));
			}
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
