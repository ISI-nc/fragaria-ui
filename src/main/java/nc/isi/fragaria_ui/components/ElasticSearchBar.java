package nc.isi.fragaria_ui.components;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import nc.isi.fragaria_adapter_rewrite.dao.SearchQuery;
import nc.isi.fragaria_adapter_rewrite.dao.Session;
import nc.isi.fragaria_adapter_rewrite.dao.SessionManager;
import nc.isi.fragaria_adapter_rewrite.entities.AbstractEntity;
import nc.isi.fragaria_ui.utils.events.modalbeaneditform.DisplayEvent;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

public class ElasticSearchBar<T extends AbstractEntity> {
	public final static String heroText = "";
	
	@Parameter(value="3")
	@Property
	private int minChars;
	
	@Parameter(value="10")
	private int limit;
	
	@Parameter(required=true,allowNull=false)
	private Class<T> type;
	
	@Parameter(required=true,allowNull=false)
	private List<String> propertiesToSearchOn;
	
	@Parameter
	private List<String> propertiesToDisplay;
	
	@Property
	@Persist
	private String entry;
	
	@Persist
	private String prevInput;
		
	@Component(id="modalbeaneditform")
	private ModalBeanEditForm<T> modalbeaneditform;
	
	private HashMap<String, T> map = Maps.newHashMap();
	
	@Component
	private TextField searchField;	
	
    @Component
    private Form form;
	
	@Inject
	private SessionManager sessionManager;
	
	@Parameter
	private Session session;
		
	@Property
	@Persist
	private EventBus eventBusRecorder;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;

    @InjectComponent
    private Zone zoneESBar;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
	private String id;
    
	private final List<String> matches = Lists.newArrayList();
	
	public String getClientId() {
		return id;
	}

	@BeginRender
	public void initialize() {
		if(session==null)
			session = sessionManager.create();	
		if(entry==null)
			entry = heroText; 	
	}
	
	@AfterRender
	public void initializeComponents(){
		modalbeaneditform.listenTo(this);
		eventBusRecorder = modalbeaneditform.getEventBusListener();
	}	
	
	 String[] onProvideCompletionsFromSearchField(String input)
	 {	
		if(prevInput==null)
			prevInput="";
		if(session==null)
			session = sessionManager.create();
		if(propertiesToDisplay==null)
			propertiesToDisplay = propertiesToSearchOn;
		
		String[] array = input.split("\\s+");
		
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		for(String part : array)
			for(String prop : propertiesToSearchOn)
				boolQuery.should(QueryBuilders.matchPhrasePrefixQuery(prop, part));		
		
		if(map.entrySet().size()>=limit 
				|| map.entrySet().size()==0 
				|| prevInput.length()>input.length() 
				|| input.length()>=minChars){
			try {
				Collection<T> results = session.get(new SearchQuery<>(type, boolQuery, limit));
				map.clear();
				matches.clear();
				for(T object : results){
					 String dataToDisplay="";
					 for(String prop : propertiesToDisplay)
						 dataToDisplay+= object.metadata().read(object, prop)+" ";
					 map.put(dataToDisplay, object);
				}

				matches.addAll(map.keySet());
				Collections.sort(matches);
				
			} catch (Exception e) {
	            form.recordError(searchField, 
	            		"You must configure ElasticsearchAdapter correctly to use the SearchBar.");
	            if(request.isXHR())
	            	ajaxResponseRenderer.addRender(zoneESBar);
			}
			
		}
		prevInput = input;
		return matches.toArray(new String[matches.size()]);
	}
	 
	void onSuccess(){
		 if(map.containsKey(entry)){
			 T object = map.get(entry);
			 eventBusRecorder.post(new DisplayEvent<AbstractEntity>(object, true));
			 map.remove(entry);
			 String dataToDisplay="";
			 for(String prop : propertiesToDisplay)
				 dataToDisplay+= object.metadata().read(object, prop)+" ";
			 map.put(dataToDisplay, object);
			 entry = dataToDisplay;
			 if(request.isXHR())
				 ajaxResponseRenderer
				 .addRender(modalbeaneditform.getZone())
				 .addRender(zoneESBar);
		 } 
	 }

}
