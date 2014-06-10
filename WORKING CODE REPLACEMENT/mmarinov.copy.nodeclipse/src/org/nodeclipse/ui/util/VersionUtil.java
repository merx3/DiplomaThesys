package org.nodeclipse.ui.util;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

/**
 * asked http://stackoverflow.com/questions/19261994/eclipse-plugin-dev-how-to-get-current-bundle-version
 * @author Paul Verest 
 * 
 * example from ErrorLog Event Detail Session Data
 * 
eclipse.buildId=4.3.0.I20130605-2000
java.version=1.7.0_40
java.vendor=Oracle Corporation
BootLoader constants: OS=win32, ARCH=x86, WS=win32, NL=zh_CN
 */
public class VersionUtil {

	public static String versionString = "UNDEFINED";
	public static String eclipseVersionString = "UNDEFINED";
	public static String javaVersionString = "UNDEFINED";
	public static String osVersionString = "UNDEFINED";
	
	static {
		if (Platform.isRunning()){
			Bundle bundle = Platform.getBundle("org.nodeclipse.ui");
			Version version = bundle.getVersion();
			versionString = version.toString(); 
			//""+version.getMajor()+" "+version.getMinor()+" "+version.getMicro()+" "+version.getQualifier();
			
			eclipseVersionString = Platform.getBundle("org.eclipse.platform").getVersion().toString();
			
			osVersionString = Platform.getOS()+','+Platform.getOSArch();
		}
		// http://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html
		javaVersionString = System.getProperty("java.version");
	}
	
	public static String getLongString(){
		return "Nodeclipse:"+VersionUtil.versionString+" Eclipse:"+VersionUtil.eclipseVersionString
				+" Java:"+VersionUtil.javaVersionString
	    		+" OS:"+VersionUtil.osVersionString+"\n";
	}
	
	public VersionUtil() {
	}
	
//	public static void main(String[] args){
//		System.out.println(getLongString());		
//	}
}
