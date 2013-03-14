package nc.isi.fragaria_ui.utils.events.journal;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;

public class JournalCreateElementEvent extends AbstractEvent<JournalElement>{

	public JournalCreateElementEvent(JournalElement elt) {
		super(elt);
	}

}
