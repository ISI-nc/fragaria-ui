package nc.isi.fragaria_ui.utils.events.journal;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;

public class JournalDeleteElementEvent extends AbstractJournalEvent<JournalElement>{

	public JournalDeleteElementEvent(JournalElement elt) {
		super(elt);
	}

}
