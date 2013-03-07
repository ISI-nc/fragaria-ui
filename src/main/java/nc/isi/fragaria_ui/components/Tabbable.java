package nc.isi.fragaria_ui.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ValueEncoderSource;

import com.google.common.base.Objects;

@Import(module = "bootstrap")
public class Tabbable<T> implements ClientElement {

	@Inject
	private ValueEncoderSource valueEncoderSource;

	@Component(id = "loop", publishParameters = "source, encoder")
	private Loop<T> loop;

	@Property
	private T tab;

	@Persist
	@Property
	private Boolean isTabSelected;

	@Persist
	private T selectedTabName;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String creationText;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String heroText;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "tabs-left")
	@Property
	private String orientation;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "nav-tabs")
	@Property
	private String tabStyle;

	@Parameter(defaultPrefix = BindingConstants.PROP)
	private T defaultSelected;

	@InjectComponent
	private Zone zone;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
	private String id;

	void setUpRender() {
		if (isTabSelected == null) {
			isTabSelected = defaultSelected != null;
			selectedTabName = defaultSelected;
		}
	}

	/**
	 * no matter T: tab is String (valueEncoder.clientValue())
	 * 
	 * @see getClassForTab() for consequences
	 * @param tab
	 */
	void onShowTab(T tab) {
		selectedTabName = tab;
		System.out.println(tab.getClass());
		System.out.println("select tab");
		System.out.println("selectedTabName : " + selectedTabName);
		System.out.println("tab : " + tab);
		isTabSelected = true;
	}

	void onCreate() {
		selectedTabName = null;
		isTabSelected = true;
	}

	public Zone getZone() {
		return zone;
	}

	public void reset() {
		selectedTabName = null;
		isTabSelected = false;
	}

	public void reset(T tabName) {
		selectedTabName = tabName;
		isTabSelected = true;
	}

	public boolean getIsCreationAllowed() {
		return creationText != null;
	}

	/**
	 * forced to use the valueEncoder trick because of onShowTab bug
	 * 
	 * @return
	 */
	public String getClassForTab() {
		if (selectedTabName == null || tab == null) {
			return null;
		}
		return Objects.equal(
				getValueEncoder(tab).toValue(selectedTabName.toString()), tab) ? "active"
				: null;
	}

	private ValueEncoder<?> getValueEncoder(T tab) {
		return valueEncoderSource.getValueEncoder(tab.getClass());
	}

	@Override
	public String getClientId() {
		return id;
	}
}
