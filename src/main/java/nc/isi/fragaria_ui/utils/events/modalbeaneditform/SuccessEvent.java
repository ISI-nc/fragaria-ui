package nc.isi.fragaria_ui.utils.events.modalbeaneditform;

import nc.isi.fragaria_ui.utils.events.AbstractObjectEvent;

public class SuccessEvent<T> extends AbstractObjectEvent<T>{

	public SuccessEvent(T object) {
		super(object);
	}

}
