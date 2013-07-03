package nc.isi.fragaria_ui.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.FormValidationControl;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

@SupportsInformalParameters
@Events(EventConstants.PREPARE)
public class BeanEditFormWOZone implements ClientElement, FormValidationControl {
	/**
	 * The object to be edited. This will be read when the component renders and
	 * updated when the form for the component is submitted. Typically, the
	 * container will listen for a "prepare" event, in order to ensure that a
	 * non-null value is ready to be read or updated. Often, the BeanEditForm
	 * can create the object as needed (assuming a public, no arguments
	 * constructor). The object property defaults to a property with the same
	 * name as the component id.
	 */
	@Parameter(required = true, autoconnect = true)
	@Property
	private Object object;
	/**
	 * A comma-separated list of property names to be retained from the
	 * {@link org.apache.tapestry5.beaneditor.BeanModel} (only used when a
	 * default model is created automatically). Only these properties will be
	 * retained, and the properties will also be reordered. The names are
	 * case-insensitive.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String include;
	/**
	 * A comma-separated list of property names to be removed from the
	 * {@link org.apache.tapestry5.beaneditor.BeanModel} (only used when a
	 * default model is created automatically). The names are case-insensitive.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String exclude;
	/**
	 * A comma-separated list of property names indicating the order in which
	 * the properties should be presented. The names are case insensitive. Any
	 * properties not indicated in the list will be appended to the end of the
	 * display orde. Only used when a default model is created automatically.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String reorder;
	/**
	 * A comma-separated list of property names to be added to the
	 * {@link org.apache.tapestry5.beaneditor.BeanModel} (only used when a
	 * default model is created automatically).
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String add;
	/**
	 * Specifies the CSS class attribute for the form; the factory default is
	 * "well".
	 */
	@Property
	@Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL, value = "message:core-components.beaneditform.class")
	private String className;
	
	@Component(parameters = "validationId=componentResources.id", publishParameters = "clientValidation,autofocus,zone")
	private Form form;
	
	@Parameter
	@Property
	private BeanModel<?> model;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String legend;
	
	@Inject
	private ComponentResources resources;

	@Inject
	private BeanModelSource beanModelSource;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
	private String id;
	
	private boolean cancel;
	
	boolean onPrepareFromForm() {
		resources.triggerEvent(EventConstants.PREPARE, null, null);

		if (model == null) {
			Class<?> beanType = resources.getBoundType("object");

			model = beanModelSource.createEditModel(beanType,
					resources.getContainerMessages());

			BeanModelUtils.modify(model, add, include, exclude, reorder);
		}

		return true;
	}

	/**
	 * Returns the client id of the embedded form.
	 */
	public String getClientId() {
		return id;
	}

	public void clearErrors() {
		form.clearErrors();
	}

	public boolean getHasErrors() {
		return form.getHasErrors();
	}

	public boolean isValid() {
		return form.isValid();
	}

	public void recordError(Field field, String errorMessage) {
		form.recordError(field, errorMessage);
	}

	public void recordError(String errorMessage) {
		form.recordError(errorMessage);
	}
	
	  void onSelectedFromSubmit() {
	    cancel = false;
	  }     
	  void onSelectedFromCancel() {
	     cancel = true;
	  }

	public boolean isCancel() {
		return cancel;
	}
}
