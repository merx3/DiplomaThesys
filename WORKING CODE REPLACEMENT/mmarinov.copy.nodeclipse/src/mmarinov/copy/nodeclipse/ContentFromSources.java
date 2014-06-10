package mmarinov.copy.nodeclipse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nodeclipse.ui.util.NodeclipseConsole;
import org.nodeclipse.ui.util.ProcessUtils;

/**
 * Node.js Context from Sources all.json.
 * May be multiple instances, defaultInstance uses bundled all.json file.
 * 
 * TODO Model population checking is very suitable for Unit Testing
 * 
 * Example from http.json
 * 
<pre> 
      "methods": [
        {
          "textRaw": "http.createServer([requestListener])",
          "type": "method",
          "name": "createServer",
          "desc": "<p>Returns a new web server object.\n\n</p>\n<p>The <code>requestListener</code> is a function which is automatically\nadded to the <code>&#39;request&#39;</code> event.\n\n</p>\n",
          "signatures": [
            {
              "params": [
                {
                  "name": "requestListener",
                  "optional": true
                }
              ]
            }
          ]
        },
</pre>
 * 
 * 
 * @author Paul Verest
 */
class ContentFromSources {
	
	static final String ALL_JSON = "mmarinov/copy/nodeclipse/all.json";
	
	//public static JSONArray METHODS;
	JSONObject NodejsContext;
	Model model;
	public static boolean checkProperties = true;
	
	static ContentFromSources defaultInstance = null; 
	static {
		defaultInstance = new ContentFromSources(ALL_JSON);
	}
	
	public static ContentFromSources getDefaultInstances() {
		if (defaultInstance == null) {
			defaultInstance = new ContentFromSources(ALL_JSON);
		}
		return defaultInstance;
	}

    public ContentFromSources(String sourcesAllJsonPath) {
        try {
        	// check if sources to use are selected 
        	//String sourcesAllJsonPath = ProcessUtils.getSourcesAllJsonPath();
        	if ("".equals(sourcesAllJsonPath)) {
        		sourcesAllJsonPath = ALL_JSON;
        	}
            InputStream is = ContentFromSources.class.getClassLoader().getResourceAsStream(sourcesAllJsonPath);
            NodejsContext = new JSONObject(inputStream2String(is));
        } catch (JSONException e) {
        	log(e.getLocalizedMessage()+"\n");
        } catch (IOException e) {
        	log(e.getLocalizedMessage()+"\n");
        }
//        if (defaultInstance == null){
//        	defaultInstance = this;
//        }
        populateModel();
    }
    
    public static String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }
    
    /**
     * Reading JSON file
     */
    private void populateModel(){
        // modules30: timers(m8), module, addons, util(m13), Events(c1), domain(m1)(c1), buffer(c2), stream(c4), crypto(m18)(c7), 
        // tls_(ssl)(m5)(c4), stringdecoder(c1), fs(m67)(c4), path(m7), net(m10)(c2), dgram(m1)(c1), dns(m10), http(m4)(c4), https(m3)(c2), 
        // url(m3), querystring(m2), punycode(m4), readline(m1)(c1), repl(m1), vm(m5)(c1), child_process(m4)(c1), assert(m11), tty(m2)(c2), zlib(m14)(c8), os(m13), cluster(m3)(c1)

    	
    	model = new Model();
        try {
        	JSONObject nodejsContextJSONObject = NodejsContext;
			JSONArray modules = nodejsContextJSONObject.getJSONArray("modules");
			log("modules"+modules.length()+" ");
			for (int i = 0; i < modules.length(); i++) {
				JSONObject module = (JSONObject) modules.get(i);
				String moduleName = module.getString("name");
				debug( ", "+moduleName);
				Module moduleObj = new Module(moduleName);
				model.addModule(moduleObj);
				
				String moduleDesc = formatedName(moduleName)+module.getString("desc");
				Entry moduleEntry = new Entry(moduleObj,EntryType.module,moduleName,moduleName,moduleDesc,null);
				model.addEntry(moduleEntry);	
				
				populateCheckProperties(module, moduleName, moduleObj, moduleEntry);

				if (module.has("methods")) {
					JSONArray methods = module.getJSONArray("methods");
					debug("(m"+methods.length()+")");
					for (int j = 0; j < methods.length(); j++) {
						JSONObject method = (JSONObject) methods.get(j);
						// example: "textRaw": "http.createServer([requestListener])","type": "method","name": "createServer",
						String trigger = method.getString("textRaw");
						String name = method.getString("name");
						String desc = formatedName(name,trigger)+method.getString("desc");

						Entry entry = new Entry(moduleObj,EntryType.method,name,trigger,desc,moduleEntry);
						model.addEntry(entry);	
					}
				}

				if (module.has("classes")){
	                JSONArray classes = module.getJSONArray("classes");
	                debug("(c"+classes.length()+")");
	                for (int j = 0; j < classes.length(); j++) {
	                    JSONObject clazz = (JSONObject) classes.get(j);
	                    // example: "textRaw": "Class: Domain","type": "class","name": "Domain"
	                    String trigger = clazz.getString("name");
	                    if (!trigger.startsWith(moduleName)) {
	                    	trigger=moduleName+'.'+trigger;
	                    }
	                    String desc = formatedName(trigger,clazz.getString("textRaw"))+clazz.getString("desc");
						Entry entry = new Entry(moduleObj,EntryType.clazz,trigger,trigger,desc,moduleEntry);
						model.addEntry(entry);	
						
						// Class may have properties, see http.IncomingMessage -> message.httpVersion
						populateCheckProperties(module, moduleName, moduleObj, entry);
						
	                }
                }
                

        	}
        } catch (JSONException e) {
        	log(e.getLocalizedMessage()+"\n"+e);
        }		
    	
    	
    }

	private void populateCheckProperties(JSONObject obj, String moduleName, Module moduleObj, Entry parent) throws JSONException {
		if (!checkProperties){
			return;
		}	
		if (obj.has("properties")) {
			JSONArray properties = obj.getJSONArray("properties");
			debug("("+moduleName+".p"+properties.length()+")");
			for (int j = 0; j < properties.length(); j++) {
				JSONObject property = (JSONObject) properties.get(j);
				// example: see http.json (2 cases)
		        String trigger = property.getString("name");
		        if (!trigger.startsWith(moduleName)) {
		        	trigger=moduleName+'.'+trigger;
		        }
				String name = property.getString("name");
				String desc = formatedName(name,trigger); //+property.getString("desc");
				if (property.has("desc")) {
					desc = desc+property.getString("desc");
				}else{
					debug("(p"+properties.length()+")");
				}

				Entry entry = new Entry(moduleObj,EntryType.property,name,trigger,desc, null);
				model.addEntry(entry);
				
				// property may have properties, see http.IncomingMessage -> message.httpVersion
				populateCheckProperties(property, trigger, moduleObj, entry);
				
				//TODO methods of properties: http.IncomingMessage -> message.setTimeout(msecs, callback)
			}					
		}
	}

	// href won't be rendered as link
	private String formatedModuleName(String moduleName) {
		String res = formatedName(moduleName)+"<a href=\"http://www.nodejs.org/api/"+moduleName+".html\">Node.js Manual & Documentation</a><br/>";
		return res;				
	}

	private String formatedName(String name) {
		return "<b>"+name+"</b><br/>";
	}
	private String formatedName(String name, String trigger) {
		return formatedName(name)+"<code>"+trigger+"</code><br/>";
	}


    private static void debug(String s){
    	//NodeclipseConsole.write(s);
    	System.out.print(s);
    }
    private static void log(String s){
    	//NodeclipseConsole.write(s);
    	System.out.print(s);
    }
    
    //TODO unit tests
    public static void main(String[] args){
    	//System.out.println(x);
    	ContentFromSources c = getDefaultInstances(); // new ContentFromSources(ALL_JSON);
    	//c.populateModel();
    	System.out.println();
    	System.out.println("- Matches http. :");
    	List<Entry> matches = c.model.findMatchingEntries("http.");
    	for(Entry entry: matches){
    		System.out.println(entry);
    	}
    	
//    	modules30 , timers(m8), module, addons, util(m13), Events(c1), domain(m1)(c1), buffer(c2), stream(c4), crypto(m18)(c7), tls_(ssl)(m5)(c4), stringdecoder(c1), fs(m67)(c4), path(m7), net(m10)(c2), dgram(m1)(c1), dns(m10), http(m4)(c4), https(m3)(c2), url(m3), querystring(m2), punycode(m4), readline(m1)(c1), repl(m1), vm(m5)(c1), child_process(m4)(c1), assert(m11), tty(m2)(c2), zlib(m14)(c8), os(m13), cluster(m3)(c1)modules30 , timers(m8), module, addons, util(m13), Events(c1), domain(m1)(c1), buffer(c2), stream(c4), crypto(m18)(c7), tls_(ssl)(m5)(c4), stringdecoder(c1), fs(m67)(c4), path(m7), net(m10)(c2), dgram(m1)(c1), dns(m10), http(m4)(c4), https(m3)(c2), url(m3), querystring(m2), punycode(m4), readline(m1)(c1), repl(m1), vm(m5)(c1), child_process(m4)(c1), assert(m11), tty(m2)(c2), zlib(m14)(c8), os(m13), cluster(m3)(c1)
    	
		// modules30 , timers(m8), module, addons, util(m13), Events(c1),
		// domain(m1)(c1), buffer(p1)(c2)(p1)(p1), stream(c4),
		// crypto(p1)(m18)(c7)(p1)(p1)(p1)(p1)(p1)(p1)(p1),
		// tls_(ssl)(p1)(m5)(c4)(p1)(p1)(p1)(p1), stringdecoder(c1),
		// fs(m67)(c4), path(p2)(m7), net(m10)(c2), dgram(m1)(c1), dns(m10),
		// http(p3)(p7)(m4)(c4)(p3)(p7)(p3)(p7)(p3)(p7)(p3)(p7),
		// https(p1)(m3)(c2)(p1)(p1), url(m3), querystring(p2)(m2),
		// punycode(p2)(m4), readline(m1)(c1), repl(m1), vm(m5)(c1),
		// child_process(m4)(c1), assert(m11), tty(m2)(c2), zlib(m14)(c8),
		// os(p1)(m13), cluster(p5)(m3)(c1)(p5)

//    	http.Agent
//    	http.ClientRequest
//    	http.Server
//    	http.ServerResponse
//    	http.createClient([port], [host])
//    	http.createServer([requestListener])
//    	http.get(options, callback)
//    	http.request(options, callback)
//    	https.Agent
//    	https.Server
//    	https.createServer(options, [requestListener])
//    	https.get(options, callback)
//    	https.request(options, callback)
    	
    	
    	//TODO this however is not is Editor content assist
    	// follows punycode(p2)(m4), readline(m1)(c1), repl(m1), vm(m5)(c1), child_process(m4)(c1), 
    	// assert(m11), tty(m2)(c2), zlib(m14)(c8), os(p1)(m13), cluster(p5)(m3)(c1)(p5)
    	
    	System.out.println("- Matches os. :");
    	List<Entry> matchesOs = c.model.findMatchingEntries("os.");
    	for(Entry entry: matchesOs){
    		System.out.println(entry);
    	}
    	
//    	Matches os. :
//    		os.EOL
//    		os.arch()
//    		os.cpus()
//    		os.endianness()
//    		os.freemem()
//    		os.hostname()
//    		os.loadavg()
//    		os.networkInterfaces()
//    		os.platform()
//    		os.release()
//    		os.tmpdir()
//    		os.totalmem()
//    		os.type()
//    		os.uptime()    	
    
    }
}

