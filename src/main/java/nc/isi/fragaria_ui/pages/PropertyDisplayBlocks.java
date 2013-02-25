package nc.isi.fragaria_ui.pages;

import java.text.DecimalFormat;

import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.PropertyOutputContext;

public class PropertyDisplayBlocks {

	@Property
	@Environmental
	private PropertyOutputContext context;

	public DecimalFormat getDecimalFormat() {
		return new DecimalFormat("0.00");
	}

}
