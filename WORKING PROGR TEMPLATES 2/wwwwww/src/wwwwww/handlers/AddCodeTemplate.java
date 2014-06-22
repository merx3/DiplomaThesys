package wwwwww.handlers;

import java.io.IOException;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.persistence.TemplatePersistenceData;
import org.eclipse.jface.text.templates.persistence.TemplateStore;

public class AddCodeTemplate {
	private static TemplatePersistentDataExtend tpd;
	
	public static TemplateStore getCodeTemplateStore() {
	    return JavaPlugin.getDefault().getTemplateStore();
	}
	
	public static void addTemplate(){
		TemplateStore codeTemplateStore = getCodeTemplateStore();

		Template template = new Template("_sample", 
		        "sample description", 
		        "java-statements", 
		        "private void ${name}(){\r\n" + 
		        "\tSystem.out.println(\"${name}\")\r\n" + 
		        "${year} //<=== compute Completion"		
		        + "}\r\n", true);
		tpd = new TemplatePersistentDataExtend(template, true, "org.eclipse.mmarinov.dynamictemplates");
		//codeTemplateStore.add(tpd);
		System.out.println(tpd.getTemplate().getName() + " --->>> " + tpd.isUserAdded());
		System.out.println(tpd.getTemplate().getName() + " --->>> " + tpd.getId());
		
		
		TemplatePersistenceData[] codeTemp = codeTemplateStore.getTemplateData(true);
		
		for (int i = 0; i < codeTemp.length; i++) {
			System.out.println(codeTemp[i].getTemplate().getName() + " ---> " + codeTemp[i].getId());
		}

		//codeTemplateStore.delete(codeTemplateStore.getTemplateData(tpd.getId()));
		try {
			codeTemplateStore.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
