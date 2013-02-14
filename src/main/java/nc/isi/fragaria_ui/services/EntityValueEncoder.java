package nc.isi.fragaria_ui.services;

import java.io.IOException;

import nc.isi.fragaria_adapter_rewrite.entities.Entity;
import nc.isi.fragaria_adapter_rewrite.entities.FragariaObjectMapper;

import org.apache.log4j.Logger;
import org.apache.tapestry5.ValueEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

public class EntityValueEncoder<V extends Entity> implements ValueEncoder<V> {
	private static final Logger LOGGER = Logger
			.getLogger(EntityValueEncoder.class);
	private final ObjectMapper objectMapper = FragariaObjectMapper.INSTANCE
			.get();

	private final Class<V> type;

	public EntityValueEncoder(Class<V> type) {
		this.type = type;
	}

	@Override
	public String toClient(V value) {
		LOGGER.info("string : " + value.toJSON().toString());
		return value.toJSON().toString();
	}

	@Override
	public V toValue(String clientValue) {
		try {
			LOGGER.info("clientValue : " + clientValue);
			return objectMapper.readValue(clientValue, type);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

}
