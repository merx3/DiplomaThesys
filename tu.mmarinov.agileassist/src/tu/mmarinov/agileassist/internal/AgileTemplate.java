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
		this.name = name;
		this.description = description;
		this.defaultValues = defaultValues;
		this.proposalName = proposalName;
		this.proposalDescription = proposalDescription;
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
		AgileTemplate iteratedTemplate = replaceInsertSegments(this, iterations);
		AgileTemplate finalTemplate = removeSegments(iteratedTemplate);	
		finalTemplate.setName(input);
		return finalTemplate;
	}

	private AgileTemplate replaceInsertSegments(AgileTemplate template, int iterations) {
		StringBuilder desc = new StringBuilder(template.getDescription());
		ArrayList<String> defaults = new ArrayList<String>(Arrays.asList(template.getDefaultValues()));
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
				int segmentsBeforeCount = getSegmentsCount(desc, startReplace);
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
		return new AgileTemplate(template.getName(), desc.toString(), defaults.toArray(new String[defaults.size()]), template.getProposalName(), template.getProposalDescription());
	}
	
	private int getSegmentsCount(StringBuilder sb, int endPos){
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
	
	private AgileTemplate removeSegments(AgileTemplate t) {
		/*   Segments (strings) to replace with default values
		 *   $[]
		 *   $[some-text]
		 */
		StringBuilder desc = new StringBuilder(t.getDescription());
		String[] defaults = t.getDefaultValues();
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
		return new AgileTemplate(t.getName(), desc.toString(), defaults, t.getProposalName(), t.getProposalDescription());
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

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		this.defaultValues = defaultValues;
	}
	public String getProposalName() {
		return proposalName;
	}
	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
	}
	public String getProposalDescription() {
		return proposalDescription;
	}
	public void setProposalDescription(String proposalDescription) {
		this.proposalDescription = proposalDescription;
	}
}
