package nc.isi.fragaria_ui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import nc.isi.fragaria_adapter_rewrite.dao.Session;
import nc.isi.fragaria_adapter_rewrite.dao.SessionManager;
import nc.isi.fragaria_adapter_rewrite.entities.AbstractEntity;
import nc.isi.fragaria_ui.services.ElasticsearchConfig;
import nc.isi.fragaria_ui.services.ElasticsearchConfigProvider;
import nc.isi.fragaria_ui.utils.events.autocompletionsearch.ObjectSelectedFromAutocompletion;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

public class AutocompletionSearch<T extends AbstractEntity> {
	@Parameter(value = "3")
	@Property
	private int minChars;

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, allowNull = false)
	private String key;

	@Property
	@Parameter
	private Session session;

	@Parameter
	private EventBus busRecorder;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String placeholder;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String txtFieldClass;
	
	@Parameter(value="true",defaultPrefix = BindingConstants.LITERAL)
	@Property
	private Boolean btnSearch;
	
	@Persist
	@Property
	private String entry;

	@Persist
	private String prevEntry;

	@Persist
	private Map<String, T> map;

	@Persist
	private ElasticsearchConfig<T> autocompleteConfig;

	@Inject
	private SessionManager sessionManager;

	@Inject
	private ElasticsearchConfigProvider autocompleteConfigProvider;

	@Component(id = "searchForm")
	private Form form;

    @InjectComponent
    private Zone searchFormZone;
    	
	@BeginRender
	public void initialize() {
		if (session == null)
			session = sessionManager.create();
		map = Maps.newHashMap();
		autocompleteConfig = autocompleteConfigProvider
				.getAutocompleteConfig(key);
	}

	List<String> onProvideCompletionsFromEntry(String partial) {
		List<String> matches = new ArrayList<String>();
		if (prevEntry == null
				|| partial.length() == minChars
				|| partial.length() < prevEntry.length()
				|| (partial.length() > prevEntry.length() && map.values()
						.size() >= autocompleteConfig.getLimit())) {
			Collection<T> results = session.get(autocompleteConfig
					.getSearchQuery(partial));
			map.clear();
			map.putAll(autocompleteConfig.getResultsWithLabel(results));
		}
		matches.addAll(map.keySet());
		Collections.sort(matches);
		prevEntry = partial;
		return matches;
	}

	void onValidateFromSearchForm() {
		if (entry == null || entry.length() < minChars) {
			form.recordError("Tapez "+minChars+" caractères pour activer l'autocomplétion.");
			return;
		} else if (map.get(entry) == null) {
			form.recordError("Le patient \"" + entry + "\" n'existe pas.");
			return;
		}
	}
	
	Object onSuccess() {
        if(busRecorder!=null){
        	busRecorder.post(new ObjectSelectedFromAutocompletion(map.get(entry)));
        }
        return true;
    }
}
