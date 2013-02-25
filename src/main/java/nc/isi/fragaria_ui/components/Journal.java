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
 *  - an eventBusRecorder in order to publish to all its listener a JournalDeleteEvent 
 *  or a JournalEditEvent
 *  - a list of objects to listen to (optional, they can be set after instanciation by 
 *  using the listenTo method)
 *  these objects will be able to post Create/Edit/Cancel event in order to create/edit/remove 
 *  a JournalElement from its group (which belongs to the list given as a parameter).
 */
@Import(module="bootstrap",stylesheet="journal.css")
public class Journal {

	@Persist
	private List<String> summaryList;	
	
	@Parameter(required=true)
	@Property
	private EventBus eventBusRecorder;
	
	@Parameter()
	private List<Object> objectsToListenTo;
	
	@Parameter(required=true)
	@Property
	private LinkedList<JournalGroup> groups;
		
	@Persist
	private EventBus eventBusListener;
	
	@Property
	private JournalGroup group;	
	
	@Property
	private JournalElement element;
	
	@Inject 
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	
	@InjectComponent
	private Zone elementZone;	
	
	@InjectComponent
	private Zone groupZone;	
	
	@InjectComponent
	private Zone journalZone;
	
	private String deletedZoneId;
	
	@BeginRender
	public void initialize() {
		if(summaryList==null)
			summaryList = Lists.newArrayList();
		if(eventBusListener==null){
			eventBusListener=new EventBus();
			if(objectsToListenTo!=null)
				listenTo(objectsToListenTo);
		}
	}

	public void listenTo(Collection<Object> objects) {
		for(Object object : objects)
			listenTo(object);
	}

	public void listenTo(Object object){
		eventBusListener.register(object);
	}
	
    public String getElementZoneId()
    {
    	if(deletedZoneId==null)
    		return "element_"+groups.indexOf(group)+"_"+ group.getElements().indexOf(element);
    	else
    		return deletedZoneId;    	
    }
    
    public String getGroupZoneId()
    {
    	return "group_"+groups.indexOf(group);
    }
	
	@Subscribe public void recordCreateEvent(CreateEvent e) {
	    create(e.getElt());
	 }
	
	private void create(JournalElement elt) {
		setCurrentGroup(elt);
		group.add(elt);
		refreshZone(groupZone);
	}

	@Subscribe public void recordCancelEvent(CancelEvent e) {
	    setCurrentEltAndGrp(e.getElt().getGroup(), e.getElt());
		cancel();
	}
	  
	void onCancelElement(String eltId,String gpId) {
		setCurrentEltAndGrp(eltId, gpId);
		cancel();	  
	}
	
	@Subscribe public void recordEditEvent(EditEvent e) {
	    setCurrentEltAndGrp(e.getElt().getGroup(), e.getElt());
	    refreshZone(elementZone);
	}
	
	private void cancel() {
		deletedZoneId = getElementZoneId();
		element.removeFromGroup();
		JournalDeleteEvent journalDeleteEvent = new JournalDeleteEvent(element);
		element = null;
		refreshZone(elementZone);	
		eventBusRecorder.post(journalDeleteEvent);
	}
	
	public void onEditElement(String eltId,String gpId) {
		setCurrentEltAndGrp(eltId, gpId);
		eventBusRecorder.post(new JournalEditEvent(element));
	}

	void onDisplaySummary(String eltId,String gpId) {
		if(summaryList.contains(eltId))
			summaryList.remove(eltId);
		else
			summaryList.add(eltId);
		setCurrentEltAndGrp(eltId, gpId);
		refreshZone(elementZone);
	}

	void onDisplayAllSummaries() {	
    	for(JournalGroup grp : groups){
    		for(JournalElement elt : grp.getElements()){
    			if(!summaryList.contains(elt.getId()))
    				summaryList.add(elt.getId());
    		}		
    	}
    	refreshZone(journalZone);
	}
	
	void onHideAllSummaries() {    	
    	summaryList.clear();
    	refreshZone(journalZone);
	}
	
	public Boolean displaySummary(String eltId){
		return summaryList.contains(eltId);
	}
	
	private void refreshZone(Zone zone) {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(zone);
		}
	}
	  
	private void setCurrentEltAndGrp(String eltId, String gpId) {
		JournalGroup grp = getGroupFromGroupsList(gpId);
		JournalElement elt = getElementFromGroup(eltId, grp);
		setCurrentEltAndGrp(grp, elt);
	}

	private void setCurrentEltAndGrp(JournalGroup grp, JournalElement elt) {
		element = elt;
		group = grp;
	}

	private void setCurrentGroup(JournalElement elt) {
		JournalGroup g = alias(JournalGroup.class);
		group = from($(g),groups)
			  .where($(g.getId()).eq(elt.getGroup().getId()))
			  .uniqueResult($(g));
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