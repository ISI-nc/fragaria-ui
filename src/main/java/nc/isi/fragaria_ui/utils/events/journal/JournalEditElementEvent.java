package nc.isi.fragaria_ui.utils.events.journal;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;

public class JournalEditElementEvent extends AbstractEvent<JournalElement>{

	public JournalEditElementEvent(JournalElement elt) {
		super(elt);
	}

}
