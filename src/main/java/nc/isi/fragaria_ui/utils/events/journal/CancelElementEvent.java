package nc.isi.fragaria_ui.utils.events.journal;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;

public class CancelElementEvent extends AbstractJournalEvent<JournalElement>{

	public CancelElementEvent(JournalElement elt) {
		super(elt);
	}
}
