package nc.isi.fragaria_ui.utils.journal.classes;

import java.util.UUID;



public abstract class AbstractJournal {

	private String id;
	
	private String label;
	
	private String summary;
	
	private Object wrappedObject;
	
	public AbstractJournal() {
		super();
		this.id = UUID.randomUUID().toString();
		this.label = "";
		this.summary = "";
	}
	
	public AbstractJournal(String label, String summary) {
		super();
		this.id = UUID.randomUUID().toString();
		this.label = label;
		this.summary = summary;
	}
	
	public AbstractJournal(String label, String summary,Object wrappedObject) {
		super();
		this.id = UUID.randomUUID().toString();
		this.label = label;
		this.summary = summary;
		this.wrappedObject = wrappedObject;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public Object getWrappedObject() {
		return wrappedObject;
	}


	public void setWrappedObject(Object wrappedObject) {
		this.wrappedObject = wrappedObject;
	}
}
