package nc.isi.fragaria_ui.utils.journal.events;

import nc.isi.fragaria_ui.utils.journal.classes.JournalElement;
import nc.isi.fragaria_ui.utils.journal.classes.JournalGroup;

public class CreateEvent extends AbstractEvent{

	private final JournalGroup group;
	
	public CreateEvent(JournalElement elt,JournalGroup group) {
		super(elt);
		this.group = group;
	}

	public JournalGroup getGroup() {
		return group;
	}
	
	
}
