package nc.isi.fragaria_ui.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.corelib.components.Zone;

public class Tabbable<T> {

	@Component(id = "loop", publishParameters = "source, encoder")
	private Loop<T> loop;

	@Property
	private T tab;

	@Persist
	@Property
	private Boolean isTabSelected;

	@Persist
	private String selectedTabName;

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

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String defaultSelected;

	@InjectComponent
	private Zone zone;

	void setUpRender() {
		if (isTabSelected == null) {
			isTabSelected = defaultSelected != null;
			selectedTabName = defaultSelected;
		}
	}

	void onShowTab(T tab) {
		selectedTabName = tab.toString();
		System.out.println("selectedTabName : " + selectedTabName);
		isTabSelected = true;
	}

	void onCreate() {
		selectedTabName = "null";
		isTabSelected = true;
	}

	public Zone getZone() {
		return zone;
	}

	public void reset() {
		selectedTabName = null;
		isTabSelected = false;
	}
	
	public void reset(String tabName) {
		selectedTabName = tabName;
		isTabSelected = true;
	}

	public boolean getIsCreationAllowed() {
		return creationText != null;
	}

	public String getClassForTab() {
		return selectedTabName != null ? tab.toString() != null ? (selectedTabName
				.contains("\"" + tab.toString() + "\"") && selectedTabName
				.contains("_id"))
				|| selectedTabName.contains(tab.toString()) ? "active" : null
				: "active"
				: null;
	}
}
