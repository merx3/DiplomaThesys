package tu.mmarinov.agileassist.ui.model;

public class TemplateEditorModel {
	public static String[] updateDefaultValues(String[] defaultValues, String str) {
		String findStr = "$[#";
		String findStr2 = "$[]";
		int lastIndex = 0;
		int count = 0;

		int li1, li2;
		while(lastIndex != -1){						
			li1 = str.indexOf(findStr,lastIndex);
			li2 = str.indexOf(findStr2,lastIndex);
			if (li1 >= 0 && (li1 < li2 || li2 == -1)) {
				lastIndex = li1;
			}
			else {
				lastIndex = li2;
			}
	        if( lastIndex != -1){
                count ++;
                lastIndex+=findStr.length();
	        }				      
		}
		if (count != defaultValues.length) {
			String[] newudv = new String[count];
			int oldCount = defaultValues.length;
			for (int i = 0; i < count; i++) {
				newudv[i] = i < oldCount? defaultValues[i] : "";
			}
			defaultValues = newudv;
		}
		return defaultValues;
	}
}
