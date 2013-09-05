package nc.isi.fragaria_ui.components;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static com.mysema.query.collections.CollQueryFactory.from;

import java.util.LinkedList;
import java.util.List;

import nc.isi.fragaria_ui.utils.events.journal.CancelElementEvent;
import nc.isi.fragaria_ui.utils.events.journal.CancelGroupEvent;
import nc.isi.fragaria_ui.utils.events.journal.CreateUpdateElementEvent;
import nc.isi.fragaria_ui.utils.events.journal.CreateUpdateGroupEvent;
import nc.isi.fragaria_ui.utils.events.journal.JournalCreateElementEvent;
import nc.isi.fragaria_ui.utils.events.journal.JournalCreateGroupEvent;
import nc.isi.fragaria_ui.utils.events.journal.JournalDeleteElementEvent;
import nc.isi.fragaria_ui.utils.events.journal.JournalDeleteGroupEvent;
import nc.isi.fragaria_ui.utils.events.journal.JournalEditElementEvent;
import nc.isi.fragaria_ui.utils.events.journal.JournalEditGroupEvent;
import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;
import nc.isi.fragaria_ui.utils.journal.classes.JournalGroup;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
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
@Import(stylesheet="journal.css",module="bootstrap")
public class Journal {
	
	@Parameter(required=true)
	@Property
	private LinkedList<JournalGroup> groups;
	
	@Parameter(required=true)
	@Property
	private EventBus eventBusRecorder;
	
	@Parameter(value="false")
	@Property
	private Boolean vertical;
	
	@Parameter(value="New Group",defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String newGroupLabel;
	
	@Parameter(value="New Element",defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String newElementLabel;
		
	@Parameter(value="true")
	@Property
	private Boolean btnCreateGroupEnable;
	
	@Parameter(value="true")
	@Property
	private Boolean btnEditGroupEnable;
	
	@Parameter(value="true")
	@Property
	private Boolean btnRemoveGroupEnable;

	@Parameter(value="true")
	@Property
	private Boolean btnCreateElementEnable;
	
	@Parameter(value="true")
	@Property
	private Boolean btnEditElementEnable;
	
	@Parameter(value="true")
	@Property
	private Boolean btnRemoveElementEnable;

	@Parameter
	private int groupLabelCharMax;
	
	@Parameter
	private int elementLabelCharMax;
	
	@Parameter
	private int numberOfGroupByRow;
	
	@Parameter
	@Property
	private JournalGroup group;	
	
	@Parameter
	@Property
	private JournalElement element;
	

	@Persist
	private EventBus eventBusListener;

	@Persist
	private List<String> elementDeletedList;	
	
	@Persist
	private List<String> summaryList;	
	
	@Persist
	private List<String> groupList;	
	
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
				
	@Parameter(name = "elementsFooter", defaultPrefix = BindingConstants.LITERAL)
	@Property
	private Block elementsFooter;
	
	@Parameter(name = "elementSummary", defaultPrefix = BindingConstants.LITERAL)
	@Property
	private Block elementSummary;
		
	@BeginRender
	public void initialize() {
		if(groups==null)
			groups = Lists.newLinkedList();
		if(summaryList==null)
			summaryList = Lists.newArrayList();
		if(elementDeletedList==null)
			elementDeletedList = Lists.newArrayList();
		if(eventBusListener==null){
			eventBusListener=new EventBus();
			eventBusListener.register(this);
		}
		if(groupList==null)
			groupList = Lists.newArrayList();
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
    
    @Subscribe public void record(CreateUpdateGroupEvent e){
    	if(!groups.contains(e.getObject())){
    		groups.add(e.getObject());
        	if(request.isXHR())
        		ajaxResponseRenderer.addRender(journalZone);	
    	}else{
	    	group = e.getObject();
	    	if(request.isXHR())
	    		ajaxResponseRenderer.addRender(groupZone);
    	}
    }
    
	public void onCreateGroup(){
		JournalGroup grp = new JournalGroup();
		eventBusRecorder.post(
				new JournalCreateGroupEvent(grp));
	}
	
	
	public void onEditGroup(String gpId){
	eventBusRecorder.post(
			new JournalEditGroupEvent(getGroupFromGroupsList(gpId)));
	}
	
    @Subscribe public void record(CancelGroupEvent e){
    	if(groups.contains(e.getObject()))
    		groups.remove(e.getObject());
    	if(request.isXHR())
    		ajaxResponseRenderer.addRender(journalZone);
    }
	
	public void onCancelGroup(String gpId){
		eventBusRecorder.post(
				new JournalDeleteGroupEvent(getGroupFromGroupsList(gpId)));
	}

	@Subscribe public void record(CreateUpdateElementEvent e) {
		setCurrentEltAndGrp(e.getObject(), e.getGroup());
		if(elementDeletedList.contains(element.getId()))
				elementDeletedList.remove(element.getId());
		if(!group.getElements().contains(element)){
			group.add(element);
	    	if (request.isXHR())
	    		ajaxResponseRenderer.addRender(groupZone);
		}else{
	    	if (request.isXHR())
	    		ajaxResponseRenderer.addRender(elementZone);
		}
	}
	
	public void onCreateElement(String gpId){;
		JournalElement elt = new JournalElement();
		eventBusRecorder.post(new JournalCreateElementEvent(
				elt,getGroupFromGroupsList(gpId)));	
	}
	
	public void onEditElement(String eltId,String gpId) {
		eventBusRecorder.post(
				new JournalEditElementEvent(
						getElementFromGroup(eltId, getGroupFromGroupsList(gpId))));
	}

	@Subscribe public void record(CancelElementEvent e) {
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
	
	public void onDisplayElements(String gpId) {
		if(groupList.contains(gpId))
			groupList.remove(gpId);
		else
			groupList.add(gpId);
		group = getGroupFromGroupsList(gpId);
		if (request.isXHR())
			ajaxResponseRenderer.addRender(groupZone);
	}

	public Boolean elementDeleted(String eltId){
		return elementDeletedList.contains(eltId);
	}
	
	public Boolean displayElements(String id){
		return groupList.contains(id);
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
	  
	public JournalElement getElementFromGroup(String eltId, JournalGroup grp) {
		JournalElement e = alias(JournalElement.class);
		JournalElement elt = from($(e),grp.getElements())
			  .where($(e.getId()).eq(eltId))
			  .uniqueResult($(e));
		
		return elt;
	}

	public JournalGroup getGroupFromGroupsList(String gpId) {
		JournalGroup g = alias(JournalGroup.class);
		JournalGroup grp = from($(g),groups)
			  .where($(g.getId()).eq(gpId))
			  .uniqueResult($(g));
		return grp;
	}
	
	public String getSpan(){
		if(numberOfGroupByRow==0)
			return "";
		else
			return "span"+String.valueOf(12/numberOfGroupByRow);
	}
	
	public String getGroupDisplayLabel(){
		String label = group.getLabel();
		if(groupLabelCharMax!=0){
			if(label !=null && label.length()>=groupLabelCharMax){
				return label.substring(0, groupLabelCharMax-1)+"...";
			}
		}
		return label;
	}
	
	
	public String getElementDisplayLabel(){
		String label = element.getLabel();
		if(elementLabelCharMax!=0){
			if(label !=null && label.length()>=elementLabelCharMax){
				return label.substring(0, elementLabelCharMax-1)+"...";
			}
		}
		return label;
	}
	

}