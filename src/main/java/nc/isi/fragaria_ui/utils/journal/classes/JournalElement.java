package nc.isi.fragaria_ui.utils.journal.classes;


public class JournalElement extends AbstractJournal{

	private JournalGroup group;
	private Object wrappedObject;
	
	public JournalElement() {
		super();
	}
	
	public JournalElement(String id, String label, String summary) {
		super(id, label, summary);
	}

	
	public JournalElement(String id, String label,String summary, JournalGroup group) {
		super(id, label, summary);
		this.group = group;
	}
	
	public Object getWrappedObject() {
		return wrappedObject;
	}


	public void setWrappedObject(Object wrappedObject) {
		this.wrappedObject = wrappedObject;
	}

	public JournalGroup getGroup() {
		return group;
	}
	

	public void setGroup(JournalGroup group) {
		this.group = group;
	}
	
	public void unsetGroup() {
		this.group = null;
	}


	public void removeFromGroup() {
		group.remove(this);
	}
	
}
