package nc.isi.fragaria_ui.utils.modalbeaneditform.events;

import nc.isi.fragaria_adapter_rewrite.entities.AbstractEntity;


public class DisplayEvent<T extends AbstractEntity> {
	private final T object;
	private final Boolean editable;

	public DisplayEvent(T object,Boolean editable) {
		this.object = object;
		this.editable = editable;
	}
	
	public T getObject() {
		return object;
	}

	public Boolean getEditable() {
		return editable;
	}
}
