package mmarinov.copy.nodeclipse;

/**
 * immutable Model Entry:
 * it may represent module, method, class or property
 * 
 * 
 * @author Paul Verest
 */
public class Entry implements Comparable<Entry>{
	
	final Module module;
	final EntryType type; 
	final String name;
	final String trigger;
	final String desc;
	final Entry parent;

//	@Deprecated
//	public Entry(Module module,EntryType type, String name, String trigger, String desc){
//		this.module = module;
//		this.type = type;
//		this.name=name;
//		this.trigger=trigger;
//		this.desc=desc;	
//		parent = null;
//	}

	public Entry(Module module,EntryType type, String name, String trigger, String desc, Entry parent){
		this.module = module;
		this.type = type;
		this.name=name;
		this.trigger=trigger;
		this.desc=desc;		
		this.parent = parent;
	}
	
	@Override
	public int compareTo(Entry o) {
		return trigger.compareTo(o.trigger);
	}

	@Override
	public String toString(){
		return trigger;
	}
}
