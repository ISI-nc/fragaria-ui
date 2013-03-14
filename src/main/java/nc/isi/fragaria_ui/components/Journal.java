package nc.isi.fragaria_ui.components;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static com.mysema.query.collections.MiniApi.from;

import java.util.LinkedList;
import java.util.List;

import nc.isi.fragaria_ui.utils.events.journal.CancelElementEvent;
import nc.isi.fragaria_ui.utils.events.journal.CancelGroupEvent;
import nc.isi.fragaria_ui.utils.events.journal.CreateElementEvent;
import nc.isi.fragaria_ui.utils.events.journal.CreateGroupEvent;
import nc.isi.fragaria_ui.utils.events.journal.EditElementEvent;
import nc.isi.fragaria_ui.utils.events.journal.EditGroupEvent;
import nc.isi.fragaria_ui.utils.events.journal.JournalCreateElementEvent;
import nc.isi.fragaria_ui.utils.events.journal.JournalCreateGroupEvent;
import nc.isi.fragaria_ui.utils.events.journal.JournalDeleteElementEvent;
import nc.isi.fragaria_ui.utils.events.journal.JournalDeleteGroupEvent;
import nc.isi.fragaria_ui.utils.events.journal.JournalEditElementEvent;
import nc.isi.fragaria_ui.utils.events.journal.JournalEditGroupEvent;
import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;
import nc.isi.fragaria_ui.utils.journal.classes.JournalGroup;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * 
 * @author bjonathas
 *
 *The parameters are : 
 *  - a list of journalGroups,
 *  - an eventBusRecorder in order to notifiy all the listeners a JournalElement has been 
 *  deleted or has to be edited through a JournalEditEvent
 *  - a list of objects to listen to (optional, they can be set after instanciation by 
 *  using the listenTo method)
 *  these objects will be able to post Create/Edit/Cancel event in order to create/edit/remove 
 *  a JournalElement from its group (which belongs to the list given as a parameter).
 *  
 */
@Import(module="bootstrap",stylesheet="journal.css")
public class Journal {
	
	@Parameter(required=true)
	@Property
	private LinkedList<JournalGroup> groups;
	
	@Parameter(required=true)
	@Property
	private EventBus eventBusRecorder;
	
	@Parameter
	private List<Object> objectsToListenTo;
	
	@Parameter(value="false")
	@Property
	private Boolean vertical;
	
	@Parameter(value="New Group",defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String newGroupLabel;
	
	@Parameter(value="New Element",defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String newElementLabel;
	
	@Persist
	private EventBus eventBusListener;

	@Persist
	private List<String> elementDeletedList;	
	
	@Persist
	private List<String> summaryList;	
	
	@Property
	private JournalGroup group;	
	
	@Property
	private JournalElement element;
	
	@Inject 
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	
	@InjectComponent
	private Zone journalZone;
	
	@InjectComponent
	
	private Zone groupZone;
	
	@InjectComponent
	private Zone elementZone;
	
	@BeginRender
	public void initialize() {
		if(summaryList==null)
			summaryList = Lists.newArrayList();
		if(elementDeletedList==null)
			elementDeletedList = Lists.newArrayList();
		if(eventBusListener==null){
			eventBusListener=new EventBus();
			eventBusListener.register(this);
		}			
	}
	
	public EventBus getEventBusListener(){
		return eventBusListener;
	}
	
    public String getElementZoneId()
    {
    	return "element_"+element.getId();
    }
    
    public String getGroupZoneId()
    {
    	return "group_"+group.getId();
    }
    
    @Subscribe public void recordCreateEvent(CreateGroupEvent e){
    	if(!groups.contains(e.getObject()))
    		groups.add(e.getObject());
    	if(request.isXHR())
    		ajaxResponseRenderer.addRender(journalZone);
    }
    
	public void onCreateGroup(){
		eventBusRecorder.post(
				new JournalCreateGroupEvent(new JournalGroup()));
	}
	
    @Subscribe public void recordCancelEvent(CancelGroupEvent e){
    	if(!groups.contains(e.getObject()))
    		groups.remove(e.getObject());
    	if(request.isXHR())
    		ajaxResponseRenderer.addRender(journalZone);
    }
	
	public void onCancelGroup(String gpId){
		eventBusRecorder.post(
				new JournalDeleteGroupEvent(getGroupFromGroupsList(gpId)));
	}
		
    @Subscribe public void recordEditEvent(EditGroupEvent e){
		group = e.getObject();
    	if (request.isXHR())
			ajaxResponseRenderer.addRender(groupZone);
    }
	
	public void onEditGroup(String gpId){
		eventBusRecorder.post(
				new JournalEditGroupEvent(getGroupFromGroupsList(gpId)));
	}

	@Subscribe public void recordCreateEvent(CreateElementEvent e) {
		setCurrentEltAndGrp(e.getObject(), e.getGroup());
		if(elementDeletedList.contains(element.getId()))
				elementDeletedList.remove(element.getId());
		group.add(element);
	    if (request.isXHR())
			ajaxResponseRenderer.addRender(groupZone);
	}
	
	public void onCreateElement(String gpId){;
		JournalElement elt = new JournalElement();
		elt.setGroup(getGroupFromGroupsList(gpId));
		eventBusRecorder.post(new JournalCreateElementEvent(elt));
			
	}

	@Subscribe public void recordCancelEvent(CancelElementEvent e) {
		setCurrentEltAndGrp(e.getObject(), e.getObject().getGroup());
		e.getObject().removeFromGroup();
		elementDeletedList.add(e.getObject().getId());
		if (request.isXHR())
			ajaxResponseRenderer.addRender(elementZone); 
	}
	
	public void onCancelElement(String eltId,String gpId){
		eventBusRecorder.post(
				new JournalDeleteElementEvent(
						getElementFromGroup(eltId, getGroupFromGroupsList(gpId))));
	}
	
	@Subscribe public void recordEditEvent(EditElementEvent e) {
		setCurrentEltAndGrp(e.getObject(),e.getObject().getGroup());
		if (request.isXHR())
			ajaxResponseRenderer.addRender(elementZone);
	}

	public void onEditElement(String eltId,String gpId) {
		eventBusRecorder.post(
				new JournalEditElementEvent(
						getElementFromGroup(eltId, getGroupFromGroupsList(gpId))));
	}
	
	public void reset(){
		if (request.isXHR())
			ajaxResponseRenderer.addRender(journalZone);
	}

	public void onDisplaySummary(String eltId,String gpId) {
		if(summaryList.contains(eltId))
			summaryList.remove(eltId);
		else
			summaryList.add(eltId);
		setCurrentEltAndGrp(eltId, gpId);
		if (request.isXHR())
			ajaxResponseRenderer.addRender(elementZone);
	}
	
	public void onDisplaySummary(String gpId) {
		if(summaryList.contains(gpId))
			summaryList.remove(gpId);
		else
			summaryList.add(gpId);
		group = getGroupFromGroupsList(gpId);
		if (request.isXHR())
			ajaxResponseRenderer.addRender(groupZone);
	}
	

	public void onDisplayAll() {	
    	for(JournalGroup grp : groups){
    		for(JournalElement elt : grp.getElements()){
    			if(!summaryList.contains(elt.getId()))
    				summaryList.add(elt.getId());
    		}		
    	}
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(journalZone);
		}
	}
	
	public void onHideAll() {    	
    	summaryList.clear();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(journalZone);
		}
	}
	
	public Boolean elementDeleted(String eltId){
		return elementDeletedList.contains(eltId);
	}
	
	public Boolean displaySummary(String id){
		return summaryList.contains(id);
	}
	
	private void setCurrentEltAndGrp(String eltId, String gpId) {
		JournalGroup grp = getGroupFromGroupsList(gpId);
		setCurrentEltAndGrp(getElementFromGroup(eltId, grp),grp);
	}
	
	private void setCurrentEltAndGrp(JournalElement elt,JournalGroup grp) {
		group = grp;
		element = elt;
	}
	  
	private JournalElement getElementFromGroup(String eltId, JournalGroup grp) {
		JournalElement e = alias(JournalElement.class);
		JournalElement elt = from($(e),grp.getElements())
			  .where($(e.getId()).eq(eltId))
			  .uniqueResult($(e));
		
		return elt;
	}

	private JournalGroup getGroupFromGroupsList(String gpId) {
		JournalGroup g = alias(JournalGroup.class);
		JournalGroup grp = from($(g),groups)
			  .where($(g.getId()).eq(gpId))
			  .uniqueResult($(g));
		return grp;
	}
}