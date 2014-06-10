package org.nodeclipse.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.nodeclipse.ui.Activator;
import org.nodeclipse.ui.util.VersionUtil;

/**
 * @author Tomoyuki Inagaki
 * @author Paul Verest
 */
public class NodePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private BooleanFieldEditor nodeclipseConsoleEnabled;

	private BooleanFieldEditor nodeJustNode;
    private FileFieldEditor nodePath;
    private StringFieldEditor nodeOptions;
    private StringFieldEditor nodeApplicationArguments;
    private BooleanFieldEditor nodeAllowMany;
    private DirectoryFieldEditor nodeSourcesPath;
    private FileFieldEditor completionsPath;
    private BooleanFieldEditor nodeDebugNoBreak;
    private IntegerFieldEditor nodeDebugPort;
    private FileFieldEditor nodeMonitorPath;
    private FileFieldEditor expressPath;
    private FileFieldEditor coffeePath;
    //private BooleanFieldEditor coffeeJustCoffee;
    private StringFieldEditor coffeeCompileOptions;
    private StringFieldEditor coffeeCompileOutputFolder;
    private FileFieldEditor typescriptCompilerPath;
    private StringFieldEditor typescriptCompilerOptions;
    
    private FileFieldEditor phanthomjsPath;
    private BooleanFieldEditor phanthomjsDebugAutorun;
    private IntegerFieldEditor phanthomjsDebugPort;
    
    private FileFieldEditor jjsPath;
    private BooleanFieldEditor jjsJustJJS;

    private FileFieldEditor mongoDBShellPath;
    private StringFieldEditor mongoDBShellOptions;
    
    
    public NodePreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription(
        	VersionUtil.getLongString()+
        	"\n"+
    		"Node.js, Express, CoffeeScript, TypeScript, PhantomJS, Java 8 Nashorn jjs and MongoDB Shell settings");
    }

	@Override
    public void init(IWorkbench workbench) {
    }

    @Override
    protected void createFieldEditors() {
        nodeclipseConsoleEnabled = new BooleanFieldEditor(PreferenceConstants.NODECLIPSE_CONSOLE_ENABLED, 
        		"enable Nodeclipse Console", getFieldEditorParent());
        addField(nodeclipseConsoleEnabled);

        nodeJustNode = new BooleanFieldEditor(PreferenceConstants.NODE_JUST_NODE, 
        		"find node on PATH. Otherwise use Node.js instance in location below", getFieldEditorParent());
        addField(nodeJustNode);

        nodePath = new FileFieldEditor(PreferenceConstants.NODE_PATH, "Node.js path:", getFieldEditorParent());
        addField(nodePath);
        
        nodeOptions = new StringFieldEditor(PreferenceConstants.NODE_OPTIONS, "Node options (node -h):", getFieldEditorParent());
        addField(nodeOptions);

        nodeApplicationArguments = new StringFieldEditor(PreferenceConstants.NODE_APPLICATION_ARGUMENTS, "Node Application arguments:", getFieldEditorParent());
        addField(nodeApplicationArguments);
        
        nodeAllowMany = new BooleanFieldEditor(PreferenceConstants.NODE_ALLOW_MANY, 
        		"allow many Node.js instances running", getFieldEditorParent());
        addField(nodeAllowMany);

        nodeSourcesPath = new DirectoryFieldEditor(PreferenceConstants.NODE_SOURCES_PATH, "Node sources directory path:", getFieldEditorParent());
        addField(nodeSourcesPath);

        completionsPath = new FileFieldEditor(PreferenceConstants.COMPLETIONS_JSON_PATH, "Alternative completions.json path:", getFieldEditorParent());
        addField(completionsPath);
    	
        // "Node debug no -break (disable interruption of Node.js app on first line, check debug Help)" would make dialog wider
        nodeDebugNoBreak = new BooleanFieldEditor(PreferenceConstants.NODE_DEBUG_NO_BREAK, 
        		"Node debug without -brk (disable interruption of Node.js app)", getFieldEditorParent());
        addField(nodeDebugNoBreak);

        nodeDebugPort = new IntegerFieldEditor(PreferenceConstants.NODE_DEBUG_PORT, "Node debug port:", getFieldEditorParent());
        addField(nodeDebugPort);

        nodeMonitorPath = new FileFieldEditor(PreferenceConstants.NODE_MONITOR_PATH, "Node monitor path:", getFieldEditorParent());
        addField(nodeMonitorPath);

        expressPath = new FileFieldEditor(PreferenceConstants.EXPRESS_PATH, "Express path:", getFieldEditorParent());
        addField(expressPath);

        coffeePath = new FileFieldEditor(PreferenceConstants.COFFEE_PATH, "Coffee path:", getFieldEditorParent());
        addField(coffeePath);

//        coffeeJustCoffee = new BooleanFieldEditor(PreferenceConstants.COFFEE_JUST_COFFEE, 
//        		"just coffee (let Node.js find coffee CLI)", getFieldEditorParent());
//        addField(coffeeJustCoffee);

        coffeeCompileOptions = new StringFieldEditor(PreferenceConstants.COFFEE_COMPILE_OPTIONS, "Coffee compile options:", getFieldEditorParent());
        addField(coffeeCompileOptions);

        coffeeCompileOutputFolder = new StringFieldEditor(PreferenceConstants.COFFEE_COMPILE_OUTPUT_FOLDER, "Coffee output folder Help! #76", getFieldEditorParent());
        addField(coffeeCompileOutputFolder);

        typescriptCompilerPath = new FileFieldEditor(PreferenceConstants.TYPESCRIPT_COMPILER_PATH, "TypeScript compiler path:", getFieldEditorParent());
        addField(typescriptCompilerPath);

        typescriptCompilerOptions = new StringFieldEditor(PreferenceConstants.TYPESCRIPT_COMPILER_OPTIONS, "TypeScript compiler options:", getFieldEditorParent());
        addField(typescriptCompilerOptions);

        phanthomjsPath = new FileFieldEditor(PreferenceConstants.PHANTOMJS_PATH, "PhanthomJS path:", getFieldEditorParent());
        addField(phanthomjsPath);

        phanthomjsDebugPort = new IntegerFieldEditor(PreferenceConstants.PHANTOMJS_DEBUG_PORT, "PhantomJS debug port:", getFieldEditorParent());
        addField(phanthomjsDebugPort);

        phanthomjsDebugAutorun = new BooleanFieldEditor(PreferenceConstants.PHANTOMJS_DEBUG_AUTORUN, "PhantomJS debug autorun", getFieldEditorParent());
        addField(phanthomjsDebugAutorun);
        
        
        jjsPath = new FileFieldEditor(PreferenceConstants.JJS_PATH, "`jjs` path:", getFieldEditorParent());
        addField(jjsPath);

        jjsJustJJS = new BooleanFieldEditor(PreferenceConstants.JJS_JUST_JJS, 
        		"just `jjs` (find `jjs` on PATH. Useful when there are 2 or more JDK 8 instances)", getFieldEditorParent());
        addField(jjsJustJJS);
        
        mongoDBShellPath = new FileFieldEditor(PreferenceConstants.MONGODB_SHELL_PATH, "MongoDB Shell path:", getFieldEditorParent());
        addField(mongoDBShellPath);
        
        mongoDBShellOptions = new StringFieldEditor(PreferenceConstants.MONGODB_SHELL_OPTIONS, "MongoDB Shell options:", getFieldEditorParent());
        addField(mongoDBShellOptions);
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

}