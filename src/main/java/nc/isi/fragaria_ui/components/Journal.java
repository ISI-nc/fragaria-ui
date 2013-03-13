package nc.isi.fragaria_ui.components;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static com.mysema.query.collections.MiniApi.from;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;
import nc.isi.fragaria_ui.utils.journal.classes.JournalGroup;
import nc.isi.fragaria_ui.utils.journal.events.CancelEvent;
import nc.isi.fragaria_ui.utils.journal.events.CreateEvent;
import nc.isi.fragaria_ui.utils.journal.events.EditEvent;
import nc.isi.fragaria_ui.utils.journal.events.JournalCreateEvent;
import nc.isi.fragaria_ui.utils.journal.events.JournalDeleteEvent;
import nc.isi.fragaria_ui.utils.journal.events.JournalEditEvent;

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
	
	@Persist
	@Property
	private String elementEditedId;
	
	public String getColorForSelectedElement(){
		if(element.getId().equals(elementEditedId)){
			return "background-color:white;";
		}
		return "";
	}
	
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
		if(objectsToListenTo!=null)
			listenTo(objectsToListenTo);
	}
	
	public void listenTo(Collection<Object> objects) {
		for(Object object : objects)
			listenTo(object);
	}

	public void listenTo(Object...objects){
		if(objectsToListenTo == null)
			objectsToListenTo = Lists.newArrayList();
		for (Object object : objects){
			if(!objectsToListenTo.contains(object)){
				objectsToListenTo.add(object);
				eventBusListener.register(object);
			}
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
    
	@Subscribe public void recordCreateEvent(CreateEvent e) {
	    createElement(e.getElt(),e.getGroup());
	 }
	
	public void onCreateElement(String gpId){
		JournalElement elt = new JournalElement();
		getGroupFromGroupsList(gpId).add(elt);
		eventBusRecorder.post(new JournalCreateEvent(elt));
	}
	
	private void createElement(JournalElement elt,JournalGroup grp) {
		group = grp;
		if(elementDeletedList.contains(elt.getId()))
				elementDeletedList.remove(elt.getId());
		group.add(elt);
		element = elt;
		elementEditedId = element.getId();
		if (request.isXHR())
			ajaxResponseRenderer.addRender(journalZone);
	}

	@Subscribe public void recordCancelEvent(CancelEvent e) {
	    removeElement(e.getElt());  
	}
	
	public void onCancelElement(String eltId,String gpId){
		JournalElement elt = getElementFromGroup(eltId, getGroupFromGroupsList(gpId));
		removeElement(elt);
	}
	
	private void removeElement(JournalElement elt) {
		setCurrentEltAndGrp(elt, elt.getGroup());
		elt.removeFromGroup();
		elementDeletedList.add(elt.getId());
		if (request.isXHR())
			ajaxResponseRenderer.addRender(elementZone);
		eventBusRecorder.post(new JournalDeleteEvent(elt));
	}
	
	@Subscribe public void recordEditEvent(EditEvent e) {
		setCurrentEltAndGrp(e.getElt(),e.getElt().getGroup());
		if (request.isXHR())
			ajaxResponseRenderer.addRender(elementZone);
	}

	public void onEditElement(String eltId,String gpId) {
		JournalElement elt = getElementFromGroup(eltId, getGroupFromGroupsList(gpId));
		group = elt.getGroup();
		element = elt;
		eventBusRecorder.post(new JournalEditEvent(elt));
		elementEditedId = element.getId();
		if (request.isXHR())
			ajaxResponseRenderer.addRender(journalZone);
	}
	
	public void reset(){
		elementEditedId = null;
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
	
	public Boolean displaySummary(String eltId){
		return summaryList.contains(eltId);
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