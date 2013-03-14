package nc.isi.fragaria_ui.utils.events.journal;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;
import nc.isi.fragaria_ui.utils.journal.classes.JournalGroup;

public class CreateElementEvent extends AbstractEvent<JournalElement>{

	private final JournalGroup group;
	
	public CreateElementEvent(JournalElement elt,JournalGroup group) {
		super(elt);
		this.group = group;
	}

	public JournalGroup getGroup() {
		return group;
	}
	
	
}
