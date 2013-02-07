package nc.isi.fragaria_ui.pages;

import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.services.PropertyEditContext;

public class BigDecimalPropertyEditBlocks {

	@Property
	@Environmental
	private PropertyEditContext context;

	@Component
	private TextField bigDecimalField;

	public FieldTranslator<?> getBigDecimalTranslator() {
		return context.getTranslator(bigDecimalField);
	}

	public FieldValidator<?> getBigDecimalValidator() {
		return context.getValidator(bigDecimalField);
	}

}
