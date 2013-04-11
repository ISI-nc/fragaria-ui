package nc.isi.fragaria_ui.utils.journal.classes;


public class JournalElement extends AbstractJournal{

	private JournalGroup group;
	
	public JournalElement() {
		super();
	}
	
	public JournalElement( String label, String summary) {
		super( label, summary);
	}
	
	public JournalElement(String label, String summary,Object wrappedObject) {
		super(label, summary,wrappedObject);
	}

	
	public JournalElement(String id, String label,String summary, JournalGroup group) {
		super(id, label, summary);
		this.group = group;
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
