package nc.isi.fragaria_ui.utils.journal.events;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;

public class JournalEditEvent extends AbstractEvent{

	public JournalEditEvent(JournalElement elt) {
		super(elt);
	}

}
