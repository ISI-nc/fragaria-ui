package nc.isi.fragaria_ui.components;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import nc.isi.fragaria_ui.utils.events.ValidationEvent;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * 
 * @author brounchy
 *
 * Modal, inspired by bootstrap modal whith a tapestry logic
 */
@Import(module = "bootstrap",stylesheet="modal.css")
public class Modal implements ClientElement{

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Inject
	private Messages messages;

	@Parameter(required = true, allowNull = false)
	@Property
	private String label;
	
	@Parameter(value = "Create/Update",defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String labelOk;
	
	@Parameter(value = "Cancel",defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String labelCancel;
	
	@Parameter(value = "false")
	@Property
	private Boolean okBtnHidden;
	
	@Parameter(value = "false")
	@Property
	private Boolean cancelBtnHidden;
	
	@Parameter
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
	
	@Persist
	@Property
	private String idOperation;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
	private String id;
	
	@BeginRender
	public void initialize() {		
		if(ariaHidden==null)
			ariaHidden = "true";
		if(fade==null)
			fade = "";
		if(display==null)
			display = "none";
		if(eventBusListener==null){
			eventBusListener=new EventBus();
			eventBusListener.register(this);
		}
	}
	
	@Override
	public String getClientId() {
		return id;
	}
	
	public EventBus getEventBusListener(){
		return eventBusListener;
	}
	
	public void display(){
		ariaHidden = "false";
	    display = "block";
	    fade = "in";
	    if(request.isXHR())
	    	 ajaxResponseRenderer.addRender(modalZone);
	}
	
	public void hide(){
		ariaHidden = "";
	    display = "none";
	    fade = "";
	    idOperation = null;
	    if(request.isXHR())
	    	 ajaxResponseRenderer.addRender(modalZone);
	}
	
	@Subscribe public void recordValidationEvent(ValidationEvent e){
    	idOperation = e.getIdOperation();
    	display();
  	}
	
	public void onModalReset(){
		if(eventBusRecorder!=null && idOperation!=null)
			eventBusRecorder.post(new ValidationEvent(false,idOperation));
		hide();
	}
	
	public void onModalOK(){
		checkNotNull(eventBusRecorder, 
				"You have to provide an eventBusRecorder to the modal component to be able to handle the ValidationEvent raised by the ok button");
		if(idOperation!=null)
			eventBusRecorder.post(new ValidationEvent(true,idOperation));
		hide();
	}
	
	public Zone getZone(){
		return modalZone;
	}

}
