package org.nodeclipse.ui.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.nodeclipse.ui.Activator;
import org.nodeclipse.ui.preferences.PreferenceConstants;
import org.osgi.framework.Bundle;

/**
 * @author ?
 * @author Tomoyuki Inagaki
 * @author Paul Verest
 */
public class ProcessUtils {
	public static String getNodePath() {
		return Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.NODE_PATH);
	}

	public static String getNodeFolder() {
		String nodePath = getNodePath();
		return nodePath.substring(0, nodePath.lastIndexOf(File.separator));
	}

	public static String getNpmPath() {
		String nodePath = Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.NODE_PATH);
		String npmPath = nodePath.substring(0,
				nodePath.lastIndexOf(File.separator) + 1);
		if (OSUtils.isWindows()) {
			return npmPath + Constants.NPM_CMD;
		} else {
			return npmPath + Constants.NPM;
		}
	}

	public static String getExpressPath() {
		return Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.EXPRESS_PATH);
	}

	public static String getExpressVersion() {
		return Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.EXPRESS_VERSION);
	}
	
	public static String getCompletionsJsonPath() {
		return Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.COMPLETIONS_JSON_PATH);
	}
	
	/**
	 * @return existing lib folder in Node.js sources, "" otherwise
	 */
	public static String getSourcesLibPath() {
		String path = Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.NODE_SOURCES_PATH);
		if (! "".equals(path)){
			path += "/lib/".replace('/', File.separatorChar);
		}
		// TODO //if (workspace.validateLinkLocation(location).isOK()) {
		return path;
	}

	public static String getSourcesAllJsonPath() {
		String path = Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.NODE_SOURCES_PATH);
		if (! "".equals(path)){
			path += "/doc/api/all.json".replace('/', File.separatorChar);
		}
		// TODO check if exists
		return path;
	}
	
	public static int getExpressMajorVersion() {
		String ver = getExpressVersion();
		int idx = ver.indexOf('.');
		if(idx < 0) {
			return 3;
		}
		ver = ver.substring(0, idx);
		int ret = Integer.parseInt(ver);
		return ret;
	}
	
	public static String getBundledExpressPath() {
		try {
			Class clazz = Class.forName("org.nodeclipse.bundle.express.BundlePath");
			Method m = clazz.getMethod("getPath");
			String path = (String)m.invoke(clazz, null);
			return path;
		} catch (Exception e) {
		}
		return "";
//		return getBundledPath("node_modules/express/bin/express");
	}
	
	
	
	
	public static String getBundledCoffeePath() {
		try {
			Class clazz = Class.forName("org.nodeclipse.bundle.coffee.BundlePath");
			Method m = clazz.getMethod("getPath");
			String path = (String)m.invoke(clazz, null);
			return path;
		} catch (Exception e) {
		}
		return "";
//		return getBundledPath("node_modules/coffee-script/bin/coffee");
	}
	
	public static String getBundledPath(String path) {
		Bundle bundle = Activator.getDefault().getBundle();
		if (bundle == null) {
			LogUtil.info("getBundlePath(" + path + " bundle is null");
			return "";
		}
		try {
			URL location = FileLocator.toFileURL(bundle.getEntry("/"));
			File file = new File(location.getPath(), path);
			LogUtil.info("BundledPath: " + file.getAbsolutePath());
			
			return file.getAbsolutePath();
		} catch(Exception ex) {
			LogUtil.error(ex);
			return "";
		}
	}
	public static boolean npmInstall(String name) {
		List<String> cmdLine = new ArrayList<String>();
		cmdLine.add("sudo");
		cmdLine.add(getNpmPath());
		cmdLine.add("install");
		cmdLine.add("-g");
		cmdLine.add(name);
		try {
			exec(cmdLine, null);
		} catch(InvocationTargetException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static String exec(List<String> cmdLine, File dir)
			throws InvocationTargetException {
		String[] cmds = {};
		cmds = cmdLine.toArray(cmds);
		ProcessBuilder builder = new ProcessBuilder(cmds);
		if (dir != null) {
			builder.directory(dir);
		}

		if(OSUtils.isMacOS()) {
			Map<String, String> env = builder.environment();
			env.put("PATH", getNodeFolder());
		}
		
		StringBuilder sb = new StringBuilder();
		try {
			Process p = builder.start();
			String line;
			BufferedReader bri = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			BufferedReader bre = new BufferedReader(
					new InputStreamReader(p.getErrorStream()));
			while ((line = bri.readLine()) != null) {
				sb.append(line);
				System.out.println(line);
			}
			bri.close();
			while ((line = bre.readLine()) != null) {
				sb.append(line);
				System.out.println(line);
			}
			bre.close();
			p.waitFor();
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}

		return sb.toString();	
	}
}
