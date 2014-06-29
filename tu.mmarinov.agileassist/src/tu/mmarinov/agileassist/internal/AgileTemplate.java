package tu.mmarinov.agileassist.internal;

import java.util.ArrayList;
import java.util.Arrays;

import tu.mmarinov.agileassist.templatehandler.TemplateLoader;

public class AgileTemplate{
	private String name;
	private String description;
	private String[] defaultValues;
	private String proposalName;
	private String proposalDescription;
	
	public AgileTemplate(String name, String description, String[] defaultValues,
			String proposalName, String proposalDescription){
		this.setName(name);
		this.setDescription(description);
		this.setDefaultValues(defaultValues);
		this.setProposalName(proposalName);
		this.setProposalDescription(proposalDescription);
	}
	
	@Override
	public boolean equals(Object t) {
		// TODO Auto-generated method stub
		if (t instanceof AgileTemplate) {
			AgileTemplate compareTo = (AgileTemplate)t;
			return compareTo.getName().equals(this.name);
		}
		return super.equals(t);
	}

	public AgileTemplate applyInput(String input){
		int iterations = Integer.parseInt(input.substring(this.getName().length(), input.length()));
		AgileTemplate iteratedTemplate = replaceInsertSegments(iterations);
		AgileTemplate finalTemplate = iteratedTemplate.removeSegments();	
		finalTemplate.setName(input);
		return finalTemplate;
	}

	public AgileTemplate replaceInsertSegments(int iterations) {
		StringBuilder desc = new StringBuilder(this.getDescription());
		ArrayList<String> defaults = new ArrayList<String>(Arrays.asList(this.getDefaultValues()));
		for (int i = 1; i < iterations; i++) {
			int startReplace = desc.indexOf("$[#");
			while (startReplace >= 0) {
				int endReplace = desc.indexOf("]", startReplace);
				String whitespacesBeforeSegment = getWhitespacesBeforeSegment(desc, startReplace);
				String replaceTemplateName = desc.substring(startReplace + 3, endReplace);
				AgileTemplate replaceTemplate = TemplateLoader.getTemplate(replaceTemplateName);
				// add whitespaces after each new line
				String replacementDescription = replaceTemplate.getDescription().replaceAll("\n", "\n" + whitespacesBeforeSegment);
				desc = desc.replace(startReplace, endReplace + 1, replacementDescription);
				// add the defaults of the inserted template
				int segmentsBeforeCount = getDefaultSegmentsCount(desc, startReplace);
				String[] defaultsToInsert = replaceTemplate.getDefaultValues();
				defaults.remove(segmentsBeforeCount);
				for (int j = 0; j < defaultsToInsert.length; j++) {
					defaults.add(segmentsBeforeCount + j, defaultsToInsert[j]);
				}
				startReplace = desc.indexOf("$[#", startReplace + replacementDescription.length());
			}
		}
		//after replacement, all left named insertion segments are replaced with anonymous
		int startReplace = desc.indexOf("$[#");
		while (startReplace >= 0) {
			int endReplace = desc.indexOf("]", startReplace);
			desc = desc.replace(startReplace, endReplace + 1, "$[]");
			startReplace = desc.indexOf("$[#");
		}
		return new AgileTemplate(this.getName(), desc.toString(), defaults.toArray(new String[defaults.size()]), this.getProposalName(), this.getProposalDescription());
	}
	
	private int getDefaultSegmentsCount(StringBuilder sb, int endPos){
		int startSearch = 0;
		int count = 0;
		while(startSearch >= 0){
			int segIndex = sb.indexOf("$[", startSearch);
			if (segIndex >= endPos || segIndex < 0) {
				return count;
			}
			char nextChar = sb.charAt(segIndex + 2);
			if (nextChar == ']' || nextChar == '#') {
				count++;
			}
			startSearch = segIndex + 1;
		}
		return count;
	}
	
	public AgileTemplate removeSegments() {
		/*   Segments (strings) to replace with default values
		 *   $[]
		 *   $[some-text]
		 */
		StringBuilder desc = new StringBuilder(this.getDescription());
		String[] defaults = this.getDefaultValues();
		int defaultValueIndex = 0;
		int segmentStart = desc.indexOf("$[");
		while (segmentStart >= 0) {
			int segmentEnd = desc.indexOf("]",  segmentStart);
			if (segmentEnd - segmentStart == 2) {
				desc.delete(segmentStart, segmentEnd+1);
				desc.insert(segmentStart, defaults[defaultValueIndex]);
				defaultValueIndex++;
			}
			else{
				desc.delete(segmentStart, segmentStart + 2);
				desc.insert(segmentStart, "${");
				desc.delete(segmentEnd, segmentEnd + 1);
				desc.insert(segmentEnd, "}");				
			}
			segmentStart = desc.indexOf("$[");
		}
		return new AgileTemplate(this.getName(), desc.toString(), new String[0], this.getProposalName(), this.getProposalDescription());
	}

	private String getWhitespacesBeforeSegment(StringBuilder desc,
			int segmentPos) {
		int whitespacesCount = 0;
		int currentPos = segmentPos - 1;
		while (currentPos >= 0 && desc.charAt(currentPos) == ' ') {
			whitespacesCount++;
			currentPos--;
		}
		char[] chars = new char[whitespacesCount];
		Arrays.fill(chars, ' ');
		String result = new String(chars);
		return result;
	}

	// getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		String nameWithoutSpaces = name.replace(" ", "");
		boolean isNameValid = 
				name.length() > 0
				&& nameWithoutSpaces.length() == name.length(); 
		if (isNameValid) {
			this.name = name;	
		}
		else{
			throw new IllegalArgumentException("Template name must be without spaces and not empty.");
		}
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getDefaultValues() {
		return defaultValues;
	}
	public void setDefaultValues(String[] defaultValues) {
		StringBuilder templateDescription = new StringBuilder(this.description);
		if (getDefaultSegmentsCount(templateDescription, this.description.length()) != defaultValues.length) {
			throw new IllegalArgumentException("Template default values must be the same number as the default segments in the template's description. Only segments with $[] and $[#some_name] count as default.");
		}
		this.defaultValues = defaultValues;
	}
	public String getProposalName() {
		return proposalName;
	}
	public void setProposalName(String proposalName) {
		String nameWithoutSpaces = proposalName.replace(" ", "");
		if (proposalName.length() > 0 && nameWithoutSpaces.length() == proposalName.length()) {
			this.proposalName = proposalName;
		}
		else{
			throw new IllegalArgumentException("Proposal name must be without spaces and not empty.");
		}
	}
	public String getProposalDescription() {
		return proposalDescription;
	}
	public void setProposalDescription(String proposalDescription) {
		this.proposalDescription = proposalDescription;
	}
}
