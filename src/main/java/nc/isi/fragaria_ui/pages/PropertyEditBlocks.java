package nc.isi.fragaria_ui.pages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.services.PropertyEditContext;

public class PropertyEditBlocks {

	@Property
	@Environmental
	private PropertyEditContext context;

	@Component
	private TextField bigDecimalField;

	@Component
	private DateField dateTimeField;

	public DateFormat getDateInputFormat() {
		return new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	}

	public FieldTranslator<?> getDateTimeTranslator() {
		return context.getTranslator(dateTimeField);
	}

	public FieldValidator<?> getDateTimeValidator() {
		return context.getValidator(dateTimeField);
	}

	public FieldTranslator<?> getBigDecimalTranslator() {
		return context.getTranslator(bigDecimalField);
	}

	public FieldValidator<?> getBigDecimalValidator() {
		return context.getValidator(bigDecimalField);
	}

}
