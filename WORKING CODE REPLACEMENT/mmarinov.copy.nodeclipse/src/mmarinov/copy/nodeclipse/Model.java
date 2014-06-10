package mmarinov.copy.nodeclipse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/** Read-only model of Node.js Context
 * 
 * DONE sorted results (idea: sort when populating)
 * 
 * @author pverest
 */
public class Model implements IRepo {
	
	List<Module> modules = new ArrayList<Module>();
	//List<Entry> entries = new ArrayList<Entry>();
	TreeSet<Entry> entries = new TreeSet<Entry>();
	
	public Model(){
		//entries
	}

	@Override
	public void addModule(Module module) {
		modules.add(module);
	}

	@Override
	public void addEntry(Entry entry) {
		entries.add(entry);
	}

	@Override
	public List<Entry> findMatchingEntries(String input) {
		List<Entry> matches = new LinkedList<Entry>();
		if ( ( input == null) || ("".equals(input)) ){
			return matches;
		}	
		for(Entry entry: entries){
			if (entry.trigger.startsWith(input)){
				matches.add(entry);
			}				
		}
		return matches;
	}
}
