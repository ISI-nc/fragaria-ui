package nc.isi.fragaria_ui.utils.journal.events;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;

public class JournalCreateEvent extends AbstractEvent{

	public JournalCreateEvent(JournalElement elt) {
		super(elt);
	}

}
