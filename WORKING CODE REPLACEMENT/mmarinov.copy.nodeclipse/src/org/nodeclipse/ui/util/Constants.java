package org.nodeclipse.ui.util;

public class Constants {
    public static final String ICONS_PATH = "$nl$/icons/";
    //TODO find 3 good icons:
	public static final String MODULE_ICON = ICONS_PATH + "npm/npm.png";
    public static final String METHOD_ICON = ICONS_PATH + "contentassist/method.gif";
    public static final String CLASS_ICON = ICONS_PATH + "node.png";
	public static final String PROPERTY_ICON = ICONS_PATH + "contentassist/property.gif";
    public static final String COMPLETIONS_JSON = "org/nodeclipse/ui/contentassist/completions.json";
    public static final String COMPLETIONS_KEY = "completions";
    
    public static final String NPM_ICON = ICONS_PATH + "npm/npm.png";
    
    public static final String BLANK_STRING = "";
    public static final String KEY_FILE_PATH = "key_file_path";
    public static final String KEY_GOAL = "key_goal";

    public static final String NPM_LAUNCH_CONFIGURATION_TYPE_ID = "org.nodeclipse.ui.npm.LaunchConfigurationType";
    public static final String NPM_LAUNCH_CONFIGURATION_TABGROUP_ID = "org.eclipse.debug.ui.launchGroup.";
    public static final String NPM = "npm";
    public static final String NPM_CMD = "npm.cmd";
    public static final String NPM_PROCESS_MESSAGE = "NPM Process ";
    public static final String NPM_INSTALL = "install";
    
    public static final String FILE_LABEL = "File";
    public static final String SEARCH_LABEL = "Search...";
    public static final String SEARCH_TITLE = "Search File";
    public static final String GOAL_LABEL = "Goal";

    public static final String PACKAGE_JSON = "package.json";
    public static final String MODE_RUN = "run";
    
	/* on NodeProjectWizardPage. Value are folder names inside org.nodeclipse.ui\templates\ */
	public static final String TEMPLATE_HELLO_WORLD = "hello-world";
	public static final String TEMPLATE_HELLO_COFFEE = "hello-coffee";
	public static final String TEMPLATE_HELLO_TYPESCRIPT = "hello-typescript";
	public static final String TEMPLATE_HELLO_HTML = "hello-html";

	/**
     * see ExpressProjectWizardPage
     */
    public static final String TEMPLATE_ENGINE_EJS = "ejs";
	public static final String TEMPLATE_ENGINE_JSHTML = "jshtml";
	public static final String TEMPLATE_ENGINE_HOGAN = "hogan";
	public static final String STYLESHEET_LESS = "less";
	public static final String STYLESHEET_STYLUS = "stylus";
	public static final String STYLESHEET_CSS = BLANK_STRING;
}
