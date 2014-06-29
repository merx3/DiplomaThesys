package tu.mmarinov.agileassist.templatehandler;

import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.persistence.TemplatePersistenceData;

//TODO: Add to documentation fix of: http://grepcode.com/file/repository.grepcode.com/java/eclipse.org/3.4.2/org.eclipse.jface/text/3.4.2/org/eclipse/jface/text/templates/persistence/TemplatePersistenceData.java#TemplatePersistenceData.isUserAdded%28%29
public class TemplatePersistentDataExtend extends TemplatePersistenceData {

	public TemplatePersistentDataExtend(Template template, boolean enabled,
			String id) {
		super(template, enabled, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isUserAdded() {
		return super.getId() != null;
	}
}
