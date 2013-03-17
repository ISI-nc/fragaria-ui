package nc.isi.fragaria_ui.utils.events.journal;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;
import nc.isi.fragaria_ui.utils.journal.classes.JournalGroup;

public class CreateUpdateElementEvent extends AbstractJournalEvent<JournalElement>{

	private final JournalGroup group;
	
	public CreateUpdateElementEvent(JournalElement elt,JournalGroup group) {
		super(elt);
		this.group = group;
	}

	public JournalGroup getGroup() {
		return group;
	}
	
	
}
