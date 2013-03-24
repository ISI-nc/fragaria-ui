package nc.isi.fragaria_ui.utils.events.journal;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;
import nc.isi.fragaria_ui.utils.journal.classes.JournalGroup;

public class JournalCreateElementEvent extends AbstractJournalEvent<JournalElement>{

	private final JournalGroup group;
	
	public JournalCreateElementEvent(JournalElement elt,
			JournalGroup group) {
		super(elt);
		this.group = group;
	}

}
