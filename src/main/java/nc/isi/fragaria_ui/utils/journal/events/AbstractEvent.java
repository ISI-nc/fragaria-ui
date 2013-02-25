package nc.isi.fragaria_ui.utils.journal.events;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;

public class AbstractEvent {
	private JournalElement elt;

	public AbstractEvent(JournalElement elt) {
		super();
		this.elt = elt;
	}

	public JournalElement getElt() {
		return elt;
	}

	public void setElt(JournalElement elt) {
		this.elt = elt;
	}
}
