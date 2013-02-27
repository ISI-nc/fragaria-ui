package nc.isi.fragaria_ui.utils.journal.events;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;

public class JournalDeleteEvent extends AbstractEvent{

	public JournalDeleteEvent(JournalElement elt) {
		super(elt);
	}

}
