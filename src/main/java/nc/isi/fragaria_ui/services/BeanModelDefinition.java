package nc.isi.fragaria_ui.services;

import nc.isi.fragaria_adapter_rewrite.entities.AbstractEntity;

public interface BeanModelDefinition<T extends AbstractEntity> {
	
	String name();
	
	Class<T> beanClass();

	String[] add();

	String[] reOrder();

	String[] exclude();

}
