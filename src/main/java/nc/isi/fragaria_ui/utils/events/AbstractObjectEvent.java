package nc.isi.fragaria_ui.utils.events;

public class AbstractObjectEvent<T> {
	private T object;

	public AbstractObjectEvent(T object) {
		super();
		this.object = object;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}
}
