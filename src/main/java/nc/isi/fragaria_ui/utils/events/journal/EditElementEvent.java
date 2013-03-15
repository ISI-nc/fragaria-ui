package nc.isi.fragaria_ui.utils.events.journal;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;

public class EditElementEvent extends AbstractJournalEvent<JournalElement>{

	public EditElementEvent(JournalElement elt) {
		super(elt);
	}

}
