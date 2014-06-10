package mmarinov.copy.nodeclipse;

import java.util.List;

public interface IRepo {
	
	void addModule(Module module);
	
	void addEntry(Entry entry);
	
	List<Entry> findMatchingEntries(String input);

}
