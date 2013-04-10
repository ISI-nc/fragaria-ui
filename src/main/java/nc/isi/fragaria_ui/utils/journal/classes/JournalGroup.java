package nc.isi.fragaria_ui.utils.journal.classes;

import java.util.LinkedList;

import com.google.common.collect.Lists;

public class JournalGroup extends AbstractJournal{

	private LinkedList<JournalElement> elements;

	public JournalGroup() {
		super();
		this.elements = Lists.newLinkedList();
	}
	
	public JournalGroup(String label, String summary,LinkedList<JournalElement> elements) {
		super(label, summary);
		this.elements = elements;
	}
	
	public JournalGroup(String label, String summary,LinkedList<JournalElement> elements,Object wrappedObject) {
		super(label, summary,wrappedObject);
		this.elements = elements;
	}
	
	public void remove(JournalElement elt) {
		elements.remove(elt);
		elt.unsetGroup();
	}
	
	public void add(JournalElement elt) {
		elements.add(elt);
		elt.setGroup(this);
	}
	
	public LinkedList<JournalElement> getElements() {
		return elements;
	}
	
	public void setElements(LinkedList<JournalElement> elements) {
		this.elements = elements;
	}
}
