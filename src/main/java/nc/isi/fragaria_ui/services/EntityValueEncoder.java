package nc.isi.fragaria_ui.services;

import nc.isi.fragaria_adapter_rewrite.dao.IdQuery;
import nc.isi.fragaria_adapter_rewrite.dao.Session;
import nc.isi.fragaria_adapter_rewrite.entities.Entity;

import org.apache.tapestry5.ValueEncoder;

public class EntityValueEncoder<V extends Entity> implements ValueEncoder<V> {

	private final Session session;

	private final Class<V> type;

	public EntityValueEncoder(Class<V> type, Session session) {
		this.session = session;
		this.type = type;
	}

	@Override
	public String toClient(V value) {
		return value.getId();
	}

	@Override
	public V toValue(String clientValue) {
		return session.getUnique(new IdQuery<>(type, clientValue));
	}

}
