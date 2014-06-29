package tu.mmarinov.agileassist.internal;
import java.util.HashMap;

import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

public class SelectionParser {
	public static HashMap<String, String> parseSelection(String selection){
		HashMap<String, String> values = new HashMap<String, String>();
		int startInsert = 0;
		int endInsert = 0;
		int endSegmentPlacement = -1;
		startInsert = selection.indexOf('{', startInsert);
		while (startInsert != -1) {
			endInsert = getEndInsertionIndex(selection, startInsert + 1);
			if (!hasNoChars(selection.substring(endSegmentPlacement + 1, startInsert))
					|| endInsert == -1
					|| selection.charAt(endInsert + 1) != '[') {
				throw new IllegalArgumentException("The selected text does not follow the format \"{code-to-insert}[segNumber, segNumber2, ....]\"");
			}
			endSegmentPlacement = selection.indexOf(']', endInsert);
			if (endSegmentPlacement == -1) {
				throw new IllegalArgumentException("The selected text does not follow the format \"{code-to-insert}[segNumber, segNumber2, ....]\"");
			}
			String segKeys = selection.substring(endInsert + 2, endSegmentPlacement);
			String segmentValue = selection.substring(startInsert +1 , endInsert);
			String[] segmentKeys = segKeys.split(",");
			for (String key : segmentKeys) {
				if (key.trim().length() == 0) {
					continue;
				}
				values.put(key.trim(), segmentValue);
			}
			startInsert = selection.indexOf('{', endSegmentPlacement);
		}
		return values;
	}
	
	private static int getEndInsertionIndex(String selection, int startInsert) {
		int openBraces = 1;
		for (int i = startInsert; i < selection.length(); i++) {
			char c = selection.charAt(i);
			switch (c) {
			case '{':
				openBraces++;
				break;
			case '}':
				openBraces--;
				break;
			}
			if (openBraces == 0) {
				return i;
			}
		}
		return -1;
	}

	private static boolean hasNoChars(String s){
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			boolean hasNoChar = (c == ' ' || c == '\r' || c == '\n' || c == '\t');
			if(!hasNoChar){ return false;}
		}
		return true;
	}
}
