package nc.isi.fragaria_ui.utils.events.journal;

import nc.isi.fragaria_ui.utils.events.AbstractObjectEvent;
import nc.isi.fragaria_ui.utils.journal.classes.AbstractJournal;


public class AbstractJournalEvent<T extends AbstractJournal> extends AbstractObjectEvent<T>{

	public AbstractJournalEvent(T object) {
		super(object);
	}

}
