package nc.isi.fragaria_ui.components;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import nc.isi.fragaria_adapter_rewrite.entities.AbstractEntity;
import nc.isi.fragaria_ui.services.BeanModelBuilder;
import nc.isi.fragaria_ui.utils.events.modalbeaneditform.DisplayEvent;
import nc.isi.fragaria_ui.utils.events.modalbeaneditform.SuccessEvent;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@Import(module = "bootstrap",stylesheet="modalBeanEditForm.css")
public class ModalBeanEditForm<T extends AbstractEntity> implements ClientElement {

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Inject
	private BeanModelBuilder beanModelBuilder;

	@Inject
	private Messages messages;

	/**
	 * Where to search for property override blocks.
	 */
	@Parameter(value = "this", required = true, allowNull = false)
	@Property
	private PropertyOverrides overrides;
	
	@Parameter(required = true, allowNull = false)
	@Property
	private String label;

	/**
	 * The object to be edited. This will be read when the component renders and
	 * updated when the form for the component is submitted. Typically, the
	 * container will listen for a "prepare" event, in order to ensure that a
	 * non-null value is ready to be read or updated. Often, the BeanEditForm
	 * can create the object as needed (assuming a public, no arguments
	 * constructor). The object property defaults to a property with the same
	 * name as the component id.
	 */
	
	@Parameter
	@Property
	private T object;
	
	@Component(id = "infoForm")
	private Form form;
	
	@Parameter
	private List<Object> objectsToListenTo;
	
	@Parameter
	private String modelName;
	
	@Parameter(required=true)
	@Property
	private EventBus eventBusRecorder;
	
	@Persist
	private EventBus eventBusListener;
	
	@InjectComponent
	private Zone modalZone;
	
	@Persist
	@Property
	private String ariaHidden;
	
	@Persist
	@Property
	private String fade;
	
	@Persist
	@Property
	private String display;
	
	@Property
	@Persist
	private Boolean editable;
	

	@Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
	private String id;

	private final LoadingCache<Class<T>, BeanModel<T>> displayModelCache = CacheBuilder
			.newBuilder()
			.build(new CacheLoader<Class<T>, BeanModel<T>>() {

				@Override
				public BeanModel<T> load(Class<T> type) {
					return (BeanModel<T>) beanModelBuilder.
							createDisplayModel(type, messages,modelName);
				}

			});


	private final LoadingCache<Class<T>, BeanModel<T>> editModelCache = CacheBuilder
			.newBuilder()
			.build(new CacheLoader<Class<T>, BeanModel<T>>() {

				@Override
				public BeanModel<T> load(Class<T> type) {
					return (BeanModel<T>) beanModelBuilder.
							createEditModel(type, messages,modelName);
				}

			});

	
	@BeginRender
	public void initialize() {
		if(eventBusListener==null){
			eventBusListener=new EventBus();
			eventBusListener.register(this);
		}			
		if(ariaHidden==null)
			ariaHidden = "true";
		if(fade==null)
			fade = "";
		if(display==null)
			display = "none";
	}
	
	@Override
	public String getClientId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	public BeanModel<T> getModel(T object)
			throws ExecutionException {
		if(editable)
			return editModelCache.get((Class<T>)object.getClass());
		else
			return displayModelCache.get((Class<T>)object.getClass());
	}
	
	
	@Subscribe public void recordDisplayEvent(DisplayEvent<T> e) {
		object = e.getObject();
		ariaHidden = "false";
	    display = "block";
	    fade = "in";
		editable = e.getEditable();
	    if(request.isXHR())
	    	 ajaxResponseRenderer.addRender(modalZone);
	}
	
	public void onModalReset(){
		ariaHidden = "";
	    display = "none";
	    fade = "";
	    editable = false;
	    if(request.isXHR())
	    	 ajaxResponseRenderer.addRender(modalZone);
	}
	
	void onSuccess(){	
		object.getSession().post();
		eventBusRecorder.post(new SuccessEvent<T>(object));
	    onModalReset();
	}
	
	public EventBus getEventBusListener(){
		return eventBusListener;
	}
	
	public Zone getZone(){
		return modalZone;
	}
	
}
