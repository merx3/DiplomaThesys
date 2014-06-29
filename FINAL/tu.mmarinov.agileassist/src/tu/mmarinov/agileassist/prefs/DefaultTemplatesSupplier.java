package tu.mmarinov.agileassist.prefs;

import java.util.ArrayList;

import tu.mmarinov.agileassist.internal.AgileTemplate;

public class DefaultTemplatesSupplier {
	private static ArrayList<AgileTemplate> defaultTemplates;

	public static ArrayList<AgileTemplate> getDefaultTemplates(){
		return defaultTemplates;
	}
	
	static
    {
		defaultTemplates = new ArrayList<AgileTemplate>();
		
		defaultTemplates.add(new AgileTemplate(
				"for", 
				"$[]for($[] $[] $[]){\n" +
				"    $[#for]" +
				"}\n$[]",
				new String[]{"", "int ${i}=0;","${i}<${arr}.length;", "${i}++", "\n", ""},  
				"for#",
				"The agile 'for' template can be used to get nested for cycles by typing 'for2','for3', etc."));
		defaultTemplates.add(new AgileTemplate(
				"ifelse", 
				"$[]if($[]){\n" +
				"    $[]\n" +
				"}\n" +
				"$[#elseif]" +
				"else {\n" +
				"    $[]\n" +
				"}\n$[]",
				new String[]{"", "${clause}", "", "", "", ""},  
				"ifelse#",
				"The agile 'ifelse' template can be used to get many if-else clauses by typing 'ifelse2','ifelse3', etc."));
		defaultTemplates.add(new AgileTemplate(
				"elseif", 
				"else if($[]){\n" +
				"    $[]\n" +
				"}\n" +
				"$[#elseif]",
				new String[]{"${clause}", "", ""},  
				"elseif#",
				"The agile 'elseif' template can be used to get else-if statements by typing 'elseif2','elseif3', etc."));
    }
}
