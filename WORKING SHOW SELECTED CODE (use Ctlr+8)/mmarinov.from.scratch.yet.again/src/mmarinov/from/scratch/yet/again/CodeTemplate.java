package mmarinov.from.scratch.yet.again;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.text.templates.persistence.TemplatePersistenceData;
import org.eclipse.jface.text.templates.persistence.TemplateReaderWriter;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.*;
/**
* Programmatically apply code templates via eclipse pde
*
* @author deniz.turan (http://denizstij.blogspot.com/)
* Oct-2009
*
*/
public class CodeTemplate {
  private String CODE_TEMPLATE_FILE_REL_PATH = "configs/CodeTemplate.xml";
  private TemplatePersistenceData[] templateData;
  private TemplateStore codeTemplateStore;
  private static CodeTemplate instance = new CodeTemplate();
  private CodeTemplate() {
       init();
  }
  public static CodeTemplate getInstance() {
       return instance;
  }
  public void applyCodeTemplates() throws Exception {
       importCodeTemplate();
  }
  private void init() {
	  //	ContributionTemplateStore ts = new ContributionTemplateStore(getCode, key);
       codeTemplateStore = JavaPlugin.getDefault().getCodeTemplateStore();
       templateData = codeTemplateStore.getTemplateData(true);
  }
  private void importCodeTemplate() throws Exception {        
       InputStream input = CodeTemplate.class.getResourceAsStream(CODE_TEMPLATE_FILE_REL_PATH) ;
       if (input == null) {
            return;
       }
       TemplateReaderWriter reader = new TemplateReaderWriter();
       TemplatePersistenceData[] datas = reader.read(input, null);
       for (int i = 0; i < datas.length; i++) {
            updateTemplate(datas[i]);
       }
       codeTemplateStore.save();
  }
  private void updateTemplate(TemplatePersistenceData data) {
       for (int i = 0; i < templateData.length; i++) {
            String id = templateData[i].getId();
            if (id != null && id.equals(data.getId())) {
                 templateData[i].setTemplate(data.getTemplate());
                 break;
            }
       }
  }
}