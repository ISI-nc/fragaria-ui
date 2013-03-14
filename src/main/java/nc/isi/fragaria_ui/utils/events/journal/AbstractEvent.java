package nc.isi.fragaria_ui.utils.events.journal;

import nc.isi.fragaria_ui.utils.events.AbstractObjectEvent;
import nc.isi.fragaria_ui.utils.journal.classes.AbstractJournal;


public class AbstractEvent<T extends AbstractJournal> extends AbstractObjectEvent<T>{

	public AbstractEvent(T object) {
		super(object);
	}

}
