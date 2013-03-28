package nc.isi.fragaria_ui.components;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import nc.isi.fragaria_adapter_rewrite.entities.AbstractEntity;
import nc.isi.fragaria_ui.services.BeanModelBuilder;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
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

@Import(module = "bootstrap",stylesheet="modalBeanEditForm.css")
public class ModalBeanEditForm<T extends AbstractEntity> implements ClientElement {
		
	@Parameter(required = true, allowNull = false)
	@Property
	private String label;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
	private String id;
	
	@Parameter
	@Property
	private T object;

	@Parameter
	private String modelName;
	
	
	@Parameter(value = "true")
	@Property
	private Boolean editable;
	
	@Component(id = "infoForm")
	private Form form;
	
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
	
	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Inject
	private BeanModelBuilder beanModelBuilder;

	@Inject
	private Messages messages;
	
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
			return editModelCache.get((Class<T>) object.getClass());
		else
			return displayModelCache.get((Class<T>)object.getClass());
	}
		
	public void display(){
		ariaHidden = "false";
	    display = "block";
	    fade = "in";
	}
	
	public void hide(){
		ariaHidden = "";
	    display = "none";
	    fade = "";
	}
	
	public void onModalReset(){
		ariaHidden = "";
	    display = "none";
	    fade = "";
	    if(request.isXHR())
	    	 ajaxResponseRenderer.addRender(modalZone);
	}
	
	public Zone getZone(){
		return modalZone;
	}
	
}
